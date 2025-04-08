package com.izatec.pay.integracao.chatpro;


import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatProTest {
    public static void main(String[] args) {
        send();
    }
    public static void send(){
        Map<String,String> nomes = new HashMap<>();

        nomes.put("11958940362","Gleyson Sampaio");

        RestTemplate restTemplate = new RestTemplate();
        String root = "https://v5.chatpro.com.br/";
        //WhatsMessageBody body = new WhatsMessageBody();
        Iterator it = nomes.keySet().iterator();
        while (it.hasNext()) {
            Object key   =  it.next();
            Object value =  nomes.get(key);

            StringBuilder sb = new StringBuilder();
            sb.append("Olá ");
            sb.append(value.toString() + ",\n");
            sb.append("Aqui é o setor financeiro da Iza Pay\n" +
                    "Passando aqui para informar que agora nosso serviço de pagamento da mensalidade será realizado via WhatsApp através do link abaixo."
            );

            //body.setNumber(key.toString());
            //body.setMessage(sb.toString());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "e57e69a");
            String url = root.concat("chatpro-").concat("/api/v1/send_message");
            //HttpEntity<WhatsMessageBody> request = new HttpEntity<>(body, headers);
            //WhatsMessageResponse response = restTemplate.postForObject(url, request, WhatsMessageResponse.class);
            System.out.println("Eviando para: " + key.toString() + " " + value.toString()  );
        }
    }

}