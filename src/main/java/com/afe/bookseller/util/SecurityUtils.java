package com.afe.bookseller.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtils {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTH_HEADER = "authorization";  //Yetkilendirme başlığı(header) nın key değeri
    public static final String AUTH_TOKEN_TYPE = "Bearer";  //Bearer token(JWT tokeninin gönderileceği yetkilendirme tipi) için sabit degisken tanımladık
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";  //(Bearer token'in sunucu tarafından göndereleceği fortmalı hal)

    //Kullanıcı rolünden bir yetki listesine dönüştürmek için method oluşturacağız ancak SpringSecurity de ROLE değer
    //ROLE_ ön ekiyle(prefix) başlamalıdır. Role prefixi yoksa yetkiye bunu eklemeliyiz.İlk bir ROLE sabiti oluşturup
    //ardından formatlanmış rolümüzü oluşturalım.Biçimlendirilmiş ROLE'den yetki listesi oluşturalım.
    public static SimpleGrantedAuthority convertToAuthority(String role) {
        String formattedRole = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;

        return new SimpleGrantedAuthority(formattedRole);
    }

    //Istekten yetkilendirme header'ını çıkarma
    public static String extractAuthTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
