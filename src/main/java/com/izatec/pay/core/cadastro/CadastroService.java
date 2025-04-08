package com.izatec.pay.core.cadastro;

import com.izatec.pay.core.cadastro.request.CadastroNotificacaoRequest;
import com.izatec.pay.core.cadastro.request.CadastroRequest;
import com.izatec.pay.core.cadastro.request.CadastroSimplesRequest;
import com.izatec.pay.core.cadastro.request.EnderecoRequest;
import com.izatec.pay.infra.Atributos;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.business.BusinessException;
import com.izatec.pay.infra.business.PersistenciaException;
import com.izatec.pay.infra.business.RegistroNaoLocalizadoException;
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
            String documento = requisicao.getDocumento();
            //validar e não permitir alterar o cpf/cnpj
            Cadastro entity = Optional.ofNullable(id).isPresent() ? repository.findById(id)
                    .orElseThrow(() -> new RegistroNaoLocalizadoException(entities, Atributos.ID))
                    : new Cadastro();

            BeanUtils.copyProperties(requisicao, entity);
            BeanUtils.copyProperties(requisicao.getEndereco(), entity.getEndereco());
            BeanUtils.copyProperties(requisicao.getNotificacao(), entity.getNotificacao());
            if(id==null)
                entity.setIdentificacao(empresa, documento);
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
        if (parceiro != null && parceiro.getDocumento() != null) {
            String documento = parceiro.getDocumento();
            Cadastro cadastro = repository.findFirstByEmpresaAndDocumento(empresa, documento).orElse(new Cadastro());
            cadastro.setIdentificacao(empresa, documento);
            cadastro.setNomeCompleto(nome(parceiro.getNomeCompleto()));
            if (parceiro.getEmail() != null)
                cadastro.setEmail(parceiro.getEmail());
            if (parceiro.getWhatsapp() != null)
                cadastro.setWhatsapp(parceiro.getWhatsapp());
            parceiro.setId(repository.save(cadastro).getId());
            parceiro.setDocumento(documento);
        }
        return parceiro;
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
        return Normalizer.normalize(nomeCompleto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    /*@Transactional
    public void cadastrar(String codigoIdentificacao, CadastroBotRequest cadastroBotRequest) {
        try {
            EmpresaConfiguracao configuracao = empresaConfiguracaoRepository.findById(codigoIdentificacao).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
            Integer empresa = configuracao.getEmpresa().getId();

            Cadastro cadastro = repository.findFirstByEmpresaAndWhatsapp(empresa, cadastroBotRequest.getWhatsapp()).orElse(new Cadastro());
            cadastro.setNomeCompleto(nome(cadastroBotRequest.getNome()));
            cadastro.setIdentificadao(Criptografia.criptografar(cadastroBotRequest.getWhatsapp().toString(), "whatsapp").substring(0, 11), empresa);
            Endereco endereco = new Endereco();
            endereco.setNumero("");
            endereco.setCep("");
            endereco.setLogradouro(cadastroBotRequest.getEndereco());
            cadastro.setEmpresa(empresa);
            cadastro.setEndereco(endereco);
            cadastro.setWhatsapp(cadastroBotRequest.getWhatsapp());
            repository.save(cadastro);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    public Cadastro buscar(Integer empresa, String whatsapp) {
        return repository.findFirstByEmpresaAndWhatsapp(empresa, whatsapp).orElse(null);
    }
    private String removerSimbolosCpfCnpj(String documento){
        if(documento == null )
            return null;
        else if (documento.length() == 11)
            return documento.replaceAll("[.\\-]", "");
        else if (documento.length() == 14)
            return documento.replaceAll("[.\\-/]", "");
        else return "";
    }

}
