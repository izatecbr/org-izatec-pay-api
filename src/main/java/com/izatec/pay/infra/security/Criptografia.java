package com.izatec.pay.infra.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class Criptografia {
    public static String criptografar(Object texto, String senha) throws Exception {
        SecretKey chave = gerarChave(senha);
        IvParameterSpec iv = gerarIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, chave, iv);
        byte[] textoCriptografado = cipher.doFinal(texto.toString().getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }
    public static String descriptografar(String textoCriptografado, String senha) throws Exception {
        SecretKey chave = gerarChave(senha);
        IvParameterSpec iv = gerarIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, chave, iv);
        byte[] textoCriptografadoBytes = Base64.getDecoder().decode(textoCriptografado);
        byte[] textoOriginal = cipher.doFinal(textoCriptografadoBytes);
        return new String(textoOriginal, "UTF-8");
    }
    private static SecretKey gerarChave(String senha) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] chaveBytes = digest.digest(senha.getBytes("UTF-8"));
        return new SecretKeySpec(chaveBytes, 0, 16, "AES");  // Utilizando os primeiros 128 bits da hash
    }
    private static IvParameterSpec gerarIV() {
        byte[] iv = new byte[16];
        return new IvParameterSpec(iv);
    }
}
