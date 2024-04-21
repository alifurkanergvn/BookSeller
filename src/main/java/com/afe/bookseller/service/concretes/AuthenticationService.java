package com.afe.bookseller.service.concretes;

import com.afe.bookseller.model.User;
import com.afe.bookseller.security.UserPrincipal;
import com.afe.bookseller.security.jwt.IJwtProvider;
import com.afe.bookseller.service.abstracts.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements IAuthenticationService {

    //. Authentication Manager, kullanıcı ile Spring Security arasındaki köprüyü sağlayacaktır.
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IJwtProvider jwtProvider;

    @Override
    public User signInAndReturnJWT(User signInRequest) {
        //SpringSecurty, kimlik bilgileri için belirli sınıfa sahiptir.Bu UsernamePasswordAuthenticationToken'dır
        //Burada onu dogruladigimizda bir authentication nesnesi elde edilir.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        //Arka planda(CustomUserDetailsService) UserDetailsService'ten loadByUserName'i cagirmaktir.
        // Dolayısıyla authentication nesnemiz UserPrincipal nesnesini içerir
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal); //Token hazır bunu User nesnesi ile sunacagiz.

        User signInUser = userPrincipal.getUser();
        signInUser.setToken(jwt);

        return signInUser;
    }
}
