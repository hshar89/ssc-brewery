package guru.sfg.brewery.bootstrap.Security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapUserAndAuthority implements CommandLineRunner {
  private final UserRepository userRepository;
  private final AuthorityRepository authorityRepository;
  private final PasswordEncoder passwordEncoder;

  public BootstrapUserAndAuthority(UserRepository userRepository,
                                   AuthorityRepository authorityRepository,
                                   PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authorityRepository = authorityRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {

    if (authorityRepository.count() == 0) {
      System.out.println("Loading user and authorities");
      loadData();
    }
  }

  private void loadData() {
    Authority admin = Authority.builder().role("ROLE_ADMIN").build();
    Authority userRole = Authority.builder().role("ROLE_USER").build();
    Authority customer = Authority.builder().role("ROLE_CUSTOMER").build();

    User spring = User.builder().authority(admin).username("spring")
        .password(passwordEncoder.encode("learning")).build();
    User userUser = User.builder().authority(userRole).username("user")
        .password(passwordEncoder.encode("password")).build();
    User scott = User.builder().authority(customer).username("scott")
        .password(passwordEncoder.encode("password")).build();
    authorityRepository.save(admin);
    authorityRepository.save(userRole);
    authorityRepository.save(customer);

    userRepository.save(spring);
    userRepository.save(userUser);
    userRepository.save(scott);
    System.out.println("Loaded Users: "+userRepository.count());
    System.out.println("Loaded authorities: "+authorityRepository.count());
  }
}
