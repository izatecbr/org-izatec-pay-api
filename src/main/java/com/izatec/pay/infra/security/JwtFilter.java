package com.izatec.pay.infra.security;
import com.izatec.pay.infra.security.jwt.JwtManager;
import com.izatec.pay.infra.security.jwt.JwtObject;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.ExpiredJwtException;

//https://blog.stackademic.com/how-to-overcome-spring-request-scope-issue-for-child-threads-ad3e2a30bf42
@Service
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //obtem o token da request com AUTHORIZATION
        String token =  request.getHeader(JwtManager.HEADER_AUTHORIZATION);
        //esta implementação só esta validando a integridade do token
        try {
            if(token!=null && !token.isEmpty()) {
                JwtObject tokenObject  = JwtManager.create(token,JwtManager.SECRET_KEY);
                List<SimpleGrantedAuthority> authorities = authorities(tokenObject.getRoles());
                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(
                                tokenObject.getSubject(),
                                null,
                                authorities);
                SecurityContextHolder.getContext().setAuthentication(userToken);
                requisicaoInfo.setEmpresa(Integer.valueOf(tokenObject.getClaim("empresa").toString()));
                requisicaoInfo.setCpfCnpj(tokenObject.getClaim("cpfCnpj").toString());
            }else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }
    private List<SimpleGrantedAuthority> authorities(List<String> roles){
        return roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}