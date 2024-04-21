package com.afe.bookseller.security.jwt;

import com.afe.bookseller.security.UserPrincipal;
import com.afe.bookseller.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class JwtProvider implements IJwtProvider {
    //@Value anatasyonu ile application.propertiss dosyasindan JWT ozelliklerini cagiracagiz
    @Value("${app.jwt.secret}") // @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")  // @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;

    //UserPrincipal(oturum açmadan sonra oluşuyor) nesnelerini parametre olarak alıp token oluşturur
    @Override
    public String generateToken(UserPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)  //jwtbuilder claim,bir kimlik bilgisi parçasını (ör. bir kullanıcı adı, bir profil resmi veya bir e-posta adresi) güvenli bir şekilde talep etmenin bir yolunu sağlar.
                .claim("userId", auth.getId())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    //Yetkilendirme değerini http header'ından alacagız
    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null) {
            return null;
        }

        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);

        //Role değerinde Spring Security ve JWT için prefix olarak ROLE_ ile başlamalıdır.Bunu SecurityUtils te yaptık
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        //Kullanıcı bilgilerinden Kullanıcı ayrıntıları nesnesinin güvenliğini sağlayacağız.Kimlik dogrulama nesnesi olusturacagız
        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (username == null) {
            return null;
        }
        //Kimlik doğrulama nesnesi olusturmak için UsernamePasswordAuthenticationToken'ı kullanabiliriz
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Override
    public boolean validateToken(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null) {
            return false;
        }

        if (claims.getExpiration().before(new Date())) {
            return false;
        }
        return true;
    }

    private Claims extractClaims(HttpServletRequest request) {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if (token == null) {
            return null;
        }

        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
