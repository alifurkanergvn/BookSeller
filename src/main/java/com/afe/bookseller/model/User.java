package com.afe.bookseller.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "create_time", nullable = false)  //Log tutmak için yazıldı
    private LocalDateTime createTime;

    @Enumerated(EnumType.STRING)  //DB'de ENUM'ın saklanma şeklini belirttik.
    @Column(name = "role", nullable = false)
    private Role role;

    @Transient  //Geçici alan anlamında veritabanında saklanmayacaktır. Sadece anlık işlemker için
    private String token;
}
