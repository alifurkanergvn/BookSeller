package com.afe.bookseller.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//OncePerRequestFilter,doFilter methodu ile, "zaten filtrelenmiş" için bir istek attribute'u depolar ve attribute
// zaten mevcutsa yeniden filtrelemeden devam eder.
//Bu class Application Contex'inde değil SecurityConfig içerisine @Bean ile methodu oluşturulunca burası bir spring fieldı
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private IJwtProvider jwtProvider;

    //Internal API yolları dışında olan API'ler için çalışmalıdır.Bu nedenle istek yolu /api/internal ile başlıyorsa filtrelenmemelidir.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = jwtProvider.getAuthentication(request);

        if (authentication != null && jwtProvider.validateToken(request)) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
