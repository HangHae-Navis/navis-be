package com.hanghae.navis.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.navis.common.dto.Message;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);
        log.warn("URL : " + request.getRequestURL() + ", METHOD : " + request.getMethod());

        if(token != null) {
            try{
                if(!jwtUtil.validateToken(token)){
                    jwtExceptionHandler(response, "토큰이 만료되었거나 잘못된 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
                    return;
                }
            }
            catch (UsernameFromTokenException ex){
                jwtExceptionHandler(response, ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json; charset=utf-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new Message(false, msg, null));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
