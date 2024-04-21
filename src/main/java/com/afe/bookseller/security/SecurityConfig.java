package com.afe.bookseller.security;

import com.afe.bookseller.model.Role;
import com.afe.bookseller.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //Bean tanımlaması yapacağımız için
@EnableWebSecurity //Spring security'e bu sınıfın  web güvenliği yapılandırma sınıfı oludğunu söyler
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${authentication.internal-api-key}")  //@Value("${authentication.internal-api-key}")
    private String internalApiKey;

    //Kullanıcıların sistemden nasıl alınacağını belirtmek için uyguladık
    @Autowired
    private CustomUserDetailsService userDetailsService;


    //Kimlik doğrulamasından sorumludur.İçerisinde kimlik doğrulama türü belirtilir.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //Yetkilendirme yapılandırma roller için rolün yapabileceklerini sınırlandırma yapılıyor.
    //Tanımlı olmayan roller dışında kimse o rollere erişemez. Önce izin verilen yollar tanımlanmalı ardından kısıtlanan yollar tanımlanmalıdır
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors(); //Backend'te ve Frontend'te farklı portlar kullancağız o yüzden cors'u aktif hale getirdik. Kaynaklar arası kaynak paylaşımıdır
        http.csrf().disable(); //session kimlik doğrulaması kullanamayacağımız için disable ettik
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //Stateless seçtik bu uygulamanın herhangi bir oturum oluşturmayacağının garantisini verir.
        // Bu nedenle her bir isteğin yeniden doğrulanması gerekir.Çünkü biz session id kullanmayacağız kullanmayacağız onun yerine JWT’yi kimlik doğrulama olarak kullanacağız..

        http.authorizeRequests()
                .antMatchers("/api/authentication/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/book").permitAll()
                .antMatchers("/api/book/**").hasRole(Role.ADMIN.name())
                .antMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
                .anyRequest().authenticated();

        //jwt filter
        //internal > jwt > authentication  filtre sıralaması ve filtre eklemeyi gerçekleştiriyoruz
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);
    }

    @Bean  //InternalApiAuthenticationFilter, AppContex'e eklenmiş oldu
    public InternalApiAuthenticationFilter internalApiAuthenticationFilter() {
        return new InternalApiAuthenticationFilter(internalApiKey);
    }

    @Bean  //JwtAuthorizationFilter, AppContex'e eklenmiş oldu
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //CORS configurationlarımızı yaptığımız sınıf.Test için tüm kaynaklara izin vermiş olduk
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
}
