package com.zavod.shoppingcart.authservice.security.repository;

import com.zavod.shoppingcart.authservice.security.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {

    UserAuthentication findUserAuthenticationByUsername(String username);

}
