package guru.sfg.brewery.bootstrap.Security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapUserAndAuthority implements CommandLineRunner {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthorityRepository authorityRepository;
  private final PasswordEncoder passwordEncoder;

  public BootstrapUserAndAuthority(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   AuthorityRepository authorityRepository,
                                   PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
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

    //beer auths
    Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
    Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
    Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
    Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

    //customer auths
    Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
    Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
    Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
    Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

    //customer brewery
    Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
    Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
    Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
    Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());


    Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
    Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
    Role userRole = roleRepository.save(Role.builder().name("USER").build());

    adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, readBeer, deleteBeer, createCustomer, readCustomer,
        updateCustomer, deleteCustomer, createBrewery, readBrewery, updateBrewery, deleteBrewery)));

    customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readCustomer, readBrewery)));

    userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

    roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

    System.out.println("Authority Repo: "+authorityRepository.count());
    System.out.println("Role repo: "+roleRepository.count());

    User adminUser = User.builder()
        .username("spring")
        .password(passwordEncoder.encode("guru"))
        .build();
    userRepository.save(adminUser);
    User userUser = User.builder()
        .username("user")
        .password(passwordEncoder.encode("password"))
        .build();
    userRepository.save(userUser);
    User customerUser = User.builder()
        .username("scott")
        .password(passwordEncoder.encode("tiger"))
        .build();
    userRepository.save(customerUser);
    adminUser.setRoles(new HashSet<>(Set.of(adminRole)));
    userUser.setRoles(new HashSet<>(Set.of(userRole)));
    customerUser.setRoles(new HashSet<>(Set.of(customerRole)));
    userRepository.saveAll(Arrays.asList(adminUser,customerUser,userUser));

    System.out.println("Users Loaded: " + userRepository.count());
  }
}
