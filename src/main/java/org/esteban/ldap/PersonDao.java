package org.esteban.ldap;

import java.util.List;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;

public class PersonDao {

    private LdapTemplate ldapTemplate;

    private static class PersonAttributMapper implements AttributesMapper {

        public Person mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
            Person p = new Person();
            p.setFirstName(attrs.get("cn").get().toString());
            p.setLastName(attrs.get("sn").get().toString());
            p.setUid(attrs.get("uid").get().toString());
            if (attrs.get("mail") != null) {
                p.setEmail(attrs.get("mail").get().toString());
            }
            return p;
        }

    }

    public Person findByPrimaryKey(String uid) {
        Name dn = buildDn(uid);
        return (Person) ldapTemplate.lookup(dn, new PersonAttributMapper());
    }

    private Name buildDn(String uid) {
        DistinguishedName dn = new DistinguishedName();
        //dn.add("ou", "scientists");
        //dn.add("ou", "People");
        dn.add("uid", uid);
        return dn;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List getPersonNamesByLastName(String lastName) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "inetOrgPerson"));
        filter.and(new LikeFilter("uid", lastName));
        return ldapTemplate.search("", filter.encode(), new PersonAttributMapper());
    }

    public List getAllPersonNames() {
        return ldapTemplate.search("", "(objectClass=inetOrgPerson)", new AttributesMapper() {
            public Object mapFromAttributes(Attributes attrs) throws NamingException {
                return attrs.get("cn").get();
            }
        });
    }

}