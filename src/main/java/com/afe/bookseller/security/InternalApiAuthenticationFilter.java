package com.afe.bookseller.security;

import com.afe.bookseller.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//OncePerRequestFilter, doFilter daha önceden uygulanmamışsa bu method sayesinde filtreyi calıştırır
@Slf4j
public class InternalApiAuthenticationFilter extends OncePerRequestFilter {
    //Daha sonra internal anahtarımız olacak
    private final String accessKey;

    public InternalApiAuthenticationFilter(String accessKey) {
        this.accessKey = accessKey;
    }

    //OncePerRequestFilter,shouldNotFilter methodu saglar.Bununla filtre sınıflarımızdaki(SecurityConfig->configure) filtre yollarını kısıtlayabiliriz
    //Sadece /api/internal ile başlıyorsa filtrele
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().startsWith("/api/internal");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //Her şeyden önce başlıktan Kimlik dogrulama anahtarini çikaracagiz
            String requestKey = SecurityUtils.extractAuthTokenFromRequest(request);

            //Authorisation için gönderilen deger bizim önceden belirledigimiz accessKey ile aynı olmalıdır.Aynı degildse exception dondur
            if (requestKey == null || !requestKey.equals(accessKey)) {
                log.warn("Internal key endpoint requested without/wrong key uri: {}", request.getRequestURI());
                throw new RuntimeException("UNAUTHORIZED");
            }

            UserPrincipal user = UserPrincipal.createSuperUser();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
