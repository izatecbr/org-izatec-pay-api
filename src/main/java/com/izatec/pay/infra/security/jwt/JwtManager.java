package com.izatec.pay.infra.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtManager {
    public static final String SECRET_KEY = "P9OkmsJkl66B5ZyMMBhfspTaPpOrUCka9WPzdNC/PdkDqatmwYkcFZxdr0VfeojUwj901CMG/EmBaKCKtXRj+w==";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    public static String create(String subject, String secretKey, Map<String, Object> claims,List<String> roles) {
        return create(subject, 1, secretKey, claims,roles);
    }

    public static String create(String subject, Integer expirationHours, String secretKey, Map<String, Object> claims,List<String> roles) {
        return create(subject,  LocalDateTime.now().plusHours(expirationHours), secretKey, claims,roles);
    }

    public static String create(String subject, LocalDateTime expiration, String secretKey, Map<String, Object> claims, List<String> roles) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(subject)
                .setIssuedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(java.sql.Timestamp.valueOf(expiration))
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .claim(ROLES_AUTHORITIES, roles)
                .compact();
    }

    public static String generateSecretKey() {
        return new String(Base64.getEncoder().encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
    }

    public static JwtObject create(String token,String key)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {

        JwtObject object = new JwtObject();
        //token = token.replace(prefix, "");

        //verifica as credencias, expiração e perfis do usuário que serão usados no etapa de filtro de requisição
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        object.setSubject(claims.getSubject());
        object.setExpiration(claims.getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        object.setIssuedAt(claims.getIssuedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        object.setRoles(checkRoles( (List) claims.get(ROLES_AUTHORITIES)));
        object.setClaims(claims);
        return object;

    }
    private static List<String> checkRoles( List<String> roles) {
        return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_",""))).collect(Collectors.toList());
    }
}