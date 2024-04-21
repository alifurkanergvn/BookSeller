package com.afe.bookseller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
//Uygulamayı baslatirken herhangi bir aktif profil belirtmezsek spring'in varsayilan profili default olacak
//Spring 2.5 ten sonra Spring otomatik olarak aktif profili kendisi seçer
public class BookSellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSellerApplication.class, args);
    }

}
