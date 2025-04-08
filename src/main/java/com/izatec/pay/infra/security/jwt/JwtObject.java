package com.izatec.pay.infra.security.jwt;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class JwtObject {
    private String subject; //nome do usuario
    private LocalDateTime issuedAt; //data de criação do token
    private LocalDateTime expiration; // data de expiração do token
    private List<String> roles = new ArrayList<>(); //perfis de acesso
    private Map<String,Object> claims = new LinkedHashMap<>();
    private static JwtObject build=null;
    public JwtObject issuedAt(LocalDateTime issuedAt) {
        this.build.issuedAt = issuedAt;
        return this;
    }
    public JwtObject issuedAt() {
        this.build.issuedAt = LocalDateTime.now();
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public JwtObject expirationHours(long hours) {
        this.build.expiration = this.build.issuedAt.plusHours(hours);
        return this;
    }
    public JwtObject expiration(LocalDateTime expiration) {
        this.build.expiration = expiration;
        return this;
    }


    public List<String> getRoles() {
        return roles;
    }

    public JwtObject roles(List<String> roles) {
        this.build.roles = roles;
        return this;
    }

    public JwtObject roles(String... roles){
        this.build.roles = Arrays.asList(roles);
        return this;
    }
    public Object getClaim(String key){
        return claims.get(key);
    }
}
