package com.zavod.shoppingcart.commons.security;

import org.springframework.beans.factory.annotation.Value;


/*
 * To use this class outside. You have to
 * 1. Define it as a bean, either by adding @Component or use @Bean to instantiate an object from it
 * 2. Use the @Autowire to ask spring to auto create it for you, and inject all the values.
 *
 * So, If you tried to create an instance manually (i.e. new JwtConfig()). This won't inject all the values.
 * Because you didn't ask Spring to do so (it's done by you manually!).
 * Also, if, at any time, you tried to instantiate an object that's not defined as a bean
 * Don't expect Spring will autowire the fields inside that class object.
 */
public class JwtConfig {

    @Value("${security.jwt.uri:/auth/**}")
    private String uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration}")
    private int expiration;

    @Value("${security.jwt.secret}")
    private String secret;

    public String getUri() {
        return uri;
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "JwtConfig{" +
                "uri='" + uri + '\'' + ", " +
                "header='" + header + '\'' + ", " +
                "prefix='" + prefix + '\'' + ", " +
                "expiration=" + expiration + ", " +
                "secret='" + secret + '\'' + '}';
    }
}
