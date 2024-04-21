package com.afe.bookseller.repository;

import com.afe.bookseller.model.Role;
import com.afe.bookseller.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface IUserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);

   //Özel bir kullanıcı rolü güncelleme sorgusu oluşturmak istiyoruz.
   //@Param ile parametre olarak username ile özel bir role bilgisi girilmesini bekliyoruz
   // : ile @Paramdan gelen değeri eşleştiriyoruz
   @Modifying  //Bu sorgunun modifiye edici(update) sorgu olduğunu belirtmek için ekliyoruz. Select olsa gerek yoktu
   @Query("update User set role = :role where username = :username")
   void updateUserRole(@Param("username") String username, @Param("role") Role role);
}
