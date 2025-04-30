package com.izatec.pay.infra.util;

import jakarta.servlet.http.HttpServletRequest;

public class Ip {

    public static String pegarIpUsuario(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null) {
            ip = request.getHeader("X_FORWARDED_FOR");
            if (ip == null){
                ip = request.getRemoteAddr();
            }
        }

        return ip;
    }
}
