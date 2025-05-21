package com.izatec.pay;

import com.izatec.pay.core.acesso.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class App implements ApplicationRunner {
    @Autowired
    private AcessoService acessoService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //System.out.println(acessoService.criptografar("1234"));
    }
}
