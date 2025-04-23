package com.izatec.pay.infra.business;

import org.springframework.http.HttpStatus;

public enum BusinessMessage {
    E100("100", "Usuário ou senha inválida","Verifique se os campos foram digitados corretamente"),
    E101("101", "Campo obrigatório: %s","Preencha o campo obrigatório"),
    E102("102", "Já existe um registro com %s igual a(o) %s","O registro deve ser único"),
    E404("404", "Não existe um registro de %s com o %s: %s informado","O registro deve existir previamente"){
        @Override
        public int getHttpStatus() {
            return HttpStatus.NOT_FOUND.value();
        }
    },
    E204("204", "Consulta sem registros","Realize uma operação de cadastro"){
        @Override
        public int getHttpStatus() {
            return HttpStatus.NOT_FOUND.value();
        }
    },
    E108("108", "Senha expirada","É necessário você alterar a senha"),
    E109("109", "Usuário bloqueado","Favor entre em contato com o administrador do sistema"),
    E110("110", "Usuário ou Senha Inválida","Revise os dados inseridos na autenticação"),
    E111("111", "Usuário %s","Consulta seu acesso junto ao suporte"),
    E116("116", "O campo %s não contem o tamanho mínimo de %s caracteres","Preencha o campo com a quantidade mínima de caracteres"),
    E117("117", "O campo %s ultrapassa o tamanho máximo de %s caracteres","Preencha o campo com a quantidade máxima de caracteres"),
    E118("118", "O campo %s não contem o tamanho mínimo de %s e máximo de %s caracteres","Preencha o campo com a quantidade mínima e máxima de caracteres"),
    E119("119", "O campo %s não pode ser alterado","Contacte o administrador do sistema"),

    E127("127", "%s","O campo precisa atender aos requisitos de negócio"),
    E128("128", "%s","Para maiores informações, consulte suporte"),

    E134("134", "O campo %s %s","O campo precisa ser preenchido conforme regra(s) de negócio"),
    E135("135", "%s","O campo precisa ser preenchido conforme regra(s) de negócio"),

    E141("141", "Erro ao consultra dados do IP","Reporte ao suporte técnico"),
    E199("199", "Erro na tentativa de concluir a transação de persistência %s","Reporte ao suporte técnico"),
    E198("198", "Erro na tentativa de realizar a consulta de %s","Reporte ao suporte técnico"),

    E181("181", "Brinde indisponível, aguarde mais algumas horas","Aguarde nossa próxima comemoração"),

    E900("900", "Token inválido ou expirado","Realize uma nova autenticação"),

    E501("501", "Erro ao tentar acessar o recurso","Contacte o Suporte Técnico"){

        @Override
        public int getHttpStatus() {
            return 500;
        }
    },
    E502("502", "Método não implementado","Contacte o Suporte Técnico"){
        @Override
        public int getHttpStatus() {
            return 500;
        }
    },
    E500("500", "Erro não mapeado","Contacte o Suporte Técnico"){

        @Override
        public int getHttpStatus() {
            return 500;
        }
    },
    ;
    private final String code;
    private final String message;
    private final String suggestion;

    private int httpStatus;

    private BusinessMessage(String code, String mensagem, String suggestion) {
        this.code = code;
        this.message = mensagem;
        this.suggestion = suggestion;
    }

    public int getHttpStatus() {
        return HttpStatus.CONFLICT.value();
    }

    public String getSuggestion() {
        return suggestion;
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
