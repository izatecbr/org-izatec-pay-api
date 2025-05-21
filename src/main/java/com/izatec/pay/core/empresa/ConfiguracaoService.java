package com.izatec.pay.core.empresa;

import com.izatec.pay.core.empresa.configuracao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class ConfiguracaoService {
    @Autowired
    private EmpresaConfiguracaoRepository repository;
    private Map<String, Configuracao> estabelecimentos = new LinkedHashMap<>();
    public Configuracao get(String codigoIdentificacao){
        Configuracao integracao = estabelecimentos.get(codigoIdentificacao);
        if(integracao==null){
            integracao = new Configuracao();
            EmpresaConfiguracao configuracao = repository.findById(codigoIdentificacao).orElse(null);
            Empresa empresa = configuracao.getEmpresa();
            integracao.setCnpj(empresa.getCpfCnpj());
            integracao.setCodigoIdentificacao(codigoIdentificacao);
            integracao.setNomeFantasia(empresa.getNomeFantasia());
            integracao.setCertificado(ConfiguracaoCertificado.of(configuracao.getCertificado().getNome(), configuracao.getCertificado().getSenha()));
            integracao.setCredencial(ConfiguracaoCredencial.of(configuracao.getIntermediador().getId(), configuracao.getIntermediador().getSenha()));
            integracao.setChavePix(configuracao.getIntermediador().getChavePix());
            integracao.setCustoIntegracao(configuracao.getCustoIntegracao());
            integracao.setEmail(empresa.getEmail());
            integracao.setEmpresa(empresa.getId());
            integracao.setIntermediador(Intermediadores.valueOf(configuracao.getIntermediador().getSigla()));
            estabelecimentos.put(codigoIdentificacao, integracao);
            log.info("Adicionando integracao ao cache, integracao:{}", codigoIdentificacao);
        }
        return integracao;
    }
    public EmpresaConfiguracao buscarWhatsappConfiguracao(Integer empresa) {
        return repository.findById(String.format("WMSG%d", empresa)).orElse(null);
    }
    public EmpresaConfiguracao buscarEmailConfiguracao(Integer empresa) {
        return repository.findById(String.format("EMAIL%d", empresa)).orElse(null);
    }
}
