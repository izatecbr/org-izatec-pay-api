package com.izatec.pay.core.acesso;

import com.izatec.pay.core.empresa.Empresa;
import com.izatec.pay.core.empresa.EmpresaRepository;
import com.izatec.pay.core.empresa.configuracao.EmpresaConfiguracao;
import com.izatec.pay.core.empresa.configuracao.EmpresaConfiguracaoRepository;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.business.LoginInvalidoException;
import com.izatec.pay.infra.business.RegistroNaoLocalizadoException;
import com.izatec.pay.infra.security.Criptografia;
import com.izatec.pay.infra.security.jwt.JwtManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AcessoService {
    @Value("${criptografia.senha}")
    private String senha;
    @Autowired
    private EmpresaConfiguracaoRepository integracaoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    public Sessao autenticarIntegracao(Login login){
        try {
            EmpresaConfiguracao configuracao = integracaoRepository.findById(login.getUsuario()).orElseThrow(()-> new RegistroNaoLocalizadoException(Entidades.EMPRESA));
            String senha = criptografar(login.getUsuario());
            if (login.getSenha().equals(senha))
                return sessao(configuracao.getEmpresa(), configuracao.getId(), "INTEGRACAO");
            else
                throw new LoginInvalidoException();
        }catch (Exception exception){
            log.error("ERRO AO TENTAR REALIZAR O LOGIN DE INTEGRACAO", exception);
            throw new LoginInvalidoException();
        }
    }
    public Sessao autenticarEmpresa(Login login){
        try {
            Empresa empresa = empresaRepository.findByCpfCnpj(login.getUsuario()).orElseThrow(()-> new RegistroNaoLocalizadoException(Entidades.EMPRESA));
            List<EmpresaConfiguracao> configs = integracaoRepository.listarPorEmpresa(empresa.getId());
            String senha = criptografar(login.getSenha());
            if (empresa.getSenha().equals(senha)) {
                String codigoIdentificacao = configs.size() >0 ? configs.get(0).getId() : "";
                return sessao(empresa,codigoIdentificacao, "EMPRESA");
            }else
                throw new LoginInvalidoException();
        }catch (Exception exception){
            log.error("ERRO AO TENTAR REALIZAR O LOGIN DE INTEGRACAO", exception);
            throw new LoginInvalidoException();
        }
    }
    private Sessao sessao(Empresa empresa, String codigoIdentificacao, String perfil){
        Sessao sessao = new Sessao();
        sessao.setDataHoraExpiracao(LocalDateTime.now().plusHours(4));
        String jwt = JwtManager.create(empresa.getNomeFantasia(), LocalDateTime.now().plusHours(4), JwtManager.SECRET_KEY, Map.of(
                "codigoIntegracao", codigoIdentificacao,
                "empresa", empresa.getId(),
                "cpfCnpj", empresa.getCpfCnpj()
        ), List.of(perfil));
        sessao.setCnpj(empresa.getCpfCnpj());
        sessao.setNomeFantasia(empresa.getNomeFantasia());
        sessao.setToken(jwt);
        return sessao;
    }


    public String criptografar(String texto) throws Exception{
        return Criptografia.criptografar(texto,senha);
    }
}
