package com.zavod.shoppingcart.authservice.security.service;

import com.zavod.shoppingcart.authservice.security.entity.UserAuthentication;
import com.zavod.shoppingcart.authservice.security.repository.UserAuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LOGGER.info("Get UserAuthentication by username=<{}>", username);
        UserAuthentication userAuthentication = userAuthenticationRepository.findUserAuthenticationByUsername(username);

        if(userAuthentication != null && userAuthentication.isEnabled()) {
            LOGGER.info("UserAuthentication with username=<{}> is loaded and is enabled", username);
            List<GrantedAuthority> grantedAuthorities = userAuthentication.getRoles().stream().map(
                    userAuthorization -> (GrantedAuthority) userAuthorization::getRole).collect(Collectors.toList());

            // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
            // And used by auth manager to verify and check user authentication.
            return new User(userAuthentication.getUsername(), encoder.encode(userAuthentication.getPassword()), grantedAuthorities);
        }

        LOGGER.error("Not found enabled UserAuthentication with username=<{}>", username);
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }
}
