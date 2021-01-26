package guru.sfg.brewery.web.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTest {

  static final String PASSWORD = "password";

  @Test
  void testBcrypt(){
    PasswordEncoder bcrypt = new BCryptPasswordEncoder();
    System.out.println(bcrypt.encode(PASSWORD));
    System.out.println(bcrypt.encode("learning"));
  }

  @Test
  void testSha256(){
    PasswordEncoder sha = new StandardPasswordEncoder();
    System.out.println(sha.encode(PASSWORD));
    String encoded = sha.encode(PASSWORD);
    assertTrue(sha.matches(PASSWORD,encoded));
  }

  @Test
  void testLdap(){
    PasswordEncoder ldap = new LdapShaPasswordEncoder();
    System.out.println(ldap.encode(PASSWORD));
    System.out.println(ldap.encode(PASSWORD));
    String encodePasswrd = ldap.encode(PASSWORD);
    assertTrue(ldap.matches(PASSWORD,encodePasswrd));
  }

  @Test
  void testNoOp(){
    PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
    System.out.println(noOp.encode(PASSWORD));
  }

  @Test
  void hashingExample(){
    System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
    String salted = PASSWORD+"ThisIsMySaltValue";
    System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
  }
}
