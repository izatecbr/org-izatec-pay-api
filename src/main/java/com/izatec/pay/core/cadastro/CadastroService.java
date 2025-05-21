package com.izatec.pay.core.cadastro;

import com.izatec.pay.core.cadastro.request.CadastroNotificacaoRequest;
import com.izatec.pay.core.cadastro.request.CadastroRequest;
import com.izatec.pay.core.cadastro.request.CadastroSimplesRequest;
import com.izatec.pay.core.cadastro.request.EnderecoRequest;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.business.BusinessException;
import com.izatec.pay.infra.business.PersistenciaException;
import com.izatec.pay.infra.business.RegistroNaoLocalizadoException;
import com.izatec.pay.infra.security.Criptografia;
import com.izatec.pay.infra.security.RequisicaoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CadastroService {
    private final String token = "cadastro";
    @Autowired
    private CadastroRepository repository;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    public Integer incluir(CadastroRequest requisicao){
        return salvar(null,requisicao,requisicaoInfo.getEmpresa());
    }
    public Integer incluir(CadastroSimplesRequest requisicao, Integer empresa){
        CadastroRequest request = new CadastroRequest();
        BeanUtils.copyProperties(requisicao, request);
        EnderecoRequest enderecoRequest = new EnderecoRequest();
        enderecoRequest.setLogradouro(requisicao.getEndereco());
        request.setEndereco(enderecoRequest);
        request.setNotificacao(new CadastroNotificacaoRequest());
        return salvar(null,request,empresa);
    }
    public Integer alterar(Integer id, CadastroRequest requisicao){
        return salvar(id, requisicao,requisicaoInfo.getEmpresa());
    }
    private Integer salvar(Integer id, CadastroRequest requisicao, Integer empresa) {
        Entidades entities = Entidades.CADASTRO;
        try {
            String documento = validarDocumento(requisicao.getDocumento());
            Cadastro entity = Optional.ofNullable(id).isPresent() ? repository.findById(id)
                    .orElseThrow(() -> new RegistroNaoLocalizadoException(entities, id))
                    : new Cadastro();
            if(entity.getEndereco()==null)
                entity.setEndereco(new Endereco());
            BeanUtils.copyProperties(requisicao, entity);
            BeanUtils.copyProperties(requisicao.getEndereco(), entity.getEndereco());
            BeanUtils.copyProperties(requisicao.getNotificacao(), entity.getNotificacao());
            entity.setDocumento(documento);
            if(id==null)
                entity.setEmpresa(empresa);
            repository.save(entity);
            return entity.getId();
        } catch (BusinessException ex) {
            log.warn(BusinessException.logMessage(ex));
            throw ex;
        } catch (Exception ex) {
            log.error(BusinessException.mensagemErroPersistencia(entities.getLegenda(), requisicao.getNomeCompleto()), ex);
            throw new PersistenciaException();
        }
    }

    public Parceiro atualizarCadastro(Integer empresa, Parceiro parceiro) {
        if (parceiro != null) {
            try {
                Cadastro cadastro = null;
                String documento = parceiro.getDocumento();
                if(parceiro.getId() != null)
                    cadastro = repository.findById(parceiro.getId()).orElse(null);
                else {
                    documento = analisarIdentificacao(parceiro);
                    cadastro = repository.findFirstByEmpresaAndDocumento(empresa, documento).orElse(new Cadastro());
                }
                cadastro.setNomeCompleto(nome(parceiro.getNomeCompleto()));
                if (parceiro.getEmail() != null)
                    cadastro.setEmail(parceiro.getEmail());
                if (parceiro.getWhatsapp() != null)
                    cadastro.setWhatsapp(parceiro.getWhatsapp());
                cadastro.setEmpresa(empresa);
                cadastro.setDocumento(documento);
                parceiro.setId(repository.save(cadastro).getId());
                parceiro.setDocumento(documento);
            }catch (Exception ex){
                log.error("Erro ao atualizar cadastro: ", ex);
            }
        }
        return parceiro;
    }
    private String analisarIdentificacao(Parceiro parceiro) throws Exception{
        String documento = parceiro.getDocumento();
        try {
            if(documento!=null && !documento.isBlank())
                return documento;
            else if(parceiro.getWhatsapp()!=null){
                String w = parceiro.getWhatsapp();
                documento= "W"+Criptografia.criptografar(w, token ).substring(0,Math.min(13,w.length()));
            }
            else if(parceiro.getEmail()!=null){
                String e = parceiro.getEmail();
                documento= "E"+Criptografia.criptografar(e, token ).substring(0,Math.min(13,e.length()));
            }else
                documento = validarDocumento(parceiro.getDocumento());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return documento;
    }

    public Cadastro buscar(Integer id) {
        return repository.findById(id).orElseThrow(RegistroNaoLocalizadoException::new);
    }


    public Cadastro buscar(String documento) {
        return repository.findFirstByEmpresaAndDocumento(requisicaoInfo.getEmpresa(), documento).orElse(null);
    }

    public List<Cadastro> listar(String filtro) {
        String nome = filtro == null || filtro.isBlank() ? null : nome(filtro);
        return repository.listar(requisicaoInfo.getEmpresa(), nome);
    }

    private String nome(String nomeCompleto) {
        return nomeCompleto ==null ? "": Normalizer.normalize(nomeCompleto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    public Cadastro buscar(Integer empresa, String whatsapp) {
        return repository.findFirstByEmpresaAndWhatsapp(empresa, whatsapp).orElse(null);
    }
    private String validarDocumento(String documento) throws Exception{
        if(documento == null || documento.isBlank() )
            return "T"+Criptografia.criptografar( System.currentTimeMillis(), token ).substring(0, 13).replaceAll("[./\\-+]", "");
        else{
            if (!documento.matches("\\d+")) {
                return documento.replaceAll("[./\\-+]", "");
            }else return documento;
        }
    }


}
