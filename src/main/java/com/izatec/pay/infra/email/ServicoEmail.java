package com.izatec.pay.infra.email;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ServicoEmail {

    @Autowired
    private JavaMailSender mailSender;

    public ServicoEmail() {
        System.out.println("CRIANDO SEND EMAIL SERVICE REAL");
    }

    public void enviar(Mensagem mensagem) {
        try {
            enviar(mensagem, false);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void enviar(Mensagem mensagem, boolean html) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(mensagem.getRemetente());
            helper.setTo(mensagem.getDestinatario());
            helper.setSubject(mensagem.getTitulo());
            helper.setText(mensagem.getCorpo(), html); // Use this or above line.
            Thread sender = new Thread(process(mailSender, mimeMessage));
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Runnable process(JavaMailSender mailSender, MimeMessage msg) {
        return new Runnable() {
            @Override
            public void run() {
                mailSender.send(msg);
                log.info("E-mail enviado com sucesso");
            }
        };
    }

}