package org.esteban.ldap;

import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapContextSourceFactory {

    public static ContextSource getLdapContextSource() throws Exception {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://ldap.forumsys.com:389");
        ldapContextSource.setBase("dc=example,dc=com");
        ldapContextSource.afterPropertiesSet();
        return ldapContextSource;
    }

}