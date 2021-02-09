package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("Getting user info via jpa");
    User user = userRepository.findByUsername(username).orElseThrow(()->{
      return new UsernameNotFoundException("Username: "+username+" not found");
    });

    return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
        user.isEnabled(), user.isAccountNotExpired(), user.isCredentialsNonExpired(),user.isAccountNotLocked(),
        convertToSpringAuthorities(user.getAuthorities()));
  }

  private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
    if(authorities!=null && authorities.size()>0){
      return authorities.stream()
          .map(Authority::getPermission)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    }
    return new HashSet<>();
  }
}
