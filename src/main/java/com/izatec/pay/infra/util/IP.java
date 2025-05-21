package com.izatec.pay.infra.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class IP {
    //como eu poderia deixar esse código mais elegante usando ternario ou optional ?
    public static String pegarIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("x-forwarded-for"))
                .or(() -> Optional.ofNullable(request.getHeader("X_FORWARDED_FOR")))
                .orElse(request.getRemoteAddr());

        /**
         * String ip = request.getHeader("x-forwarded-for");
         *         if (ip == null) {
         *             ip = request.getHeader("X_FORWARDED_FOR");
         *             if (ip == null){
         *                 ip = request.getRemoteAddr();
         *             }
         *         }
         *
         *         return ip;
         */
    }
}
