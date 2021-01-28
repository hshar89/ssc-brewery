package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlParameterAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
    RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  public RestUrlParameterAuthFilter restUrlParameterAuthFilter(AuthenticationManager authenticationManager) {
    RestUrlParameterAuthFilter filter = new RestUrlParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
    filter.setAuthenticationManager(authenticationManager);
    return filter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(restUrlParameterAuthFilter(authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
    http
        .authorizeRequests(authorize -> {
          authorize
              .antMatchers("/h2-console/**").permitAll()
              .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
              .antMatchers("/beers/find", "/beers*").hasAnyRole("CUSTOMER","USER","ADMIN")
              .antMatchers("/brewery/breweries/**").hasAnyRole("CUSTOMER","ADMIN")
              .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("CUSTOMER","ADMIN","USER")
              //.mvcMatchers(HttpMethod.DELETE,"/api/v1/beer/**").hasRole("ADMIN")
              .mvcMatchers(HttpMethod.GET,"/brewery/api/v1/breweries/**").hasAnyRole("CUSTOMER","ADMIN")
              .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("CUSTOMER","USER","ADMIN");
        })
        .authorizeRequests()
        .anyRequest()
        .authenticated().and()
        .formLogin().and()
        .httpBasic();
    //h2 console config
    http.headers().frameOptions().sameOrigin();
  }

  //  @Override
//  @Bean
//  protected UserDetailsService userDetailsService() {
//    UserDetails admin = User.withDefaultPasswordEncoder()
//        .username("spring")
//        .password("learning")
//        .roles("ADMIN")
//        .build();
//    UserDetails user = User.withDefaultPasswordEncoder()
//        .username("user")
//        .password("password")
//        .roles("USER")
//        .build();
//    return new InMemoryUserDetailsManager(admin,user);
//  }
  //below is another way to do the same thing as above
//  @Autowired
//  JpaUserDetailsService jpaUserDetailsService;

//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      //The below needs to be done incase you are using a custome configuration for userdetailsservice
    //auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
//    auth.inMemoryAuthentication()
//        .withUser("spring")
//        .password("{bcrypt}$2a$10$9Pzp/gqchmsAU9vLD9d0a.jWmU6YDOyz6O5JUTjdnpDsXqHHLlctq")
//        .roles("ADMIN")
//        .and()
//        .withUser("user")
//        .password("{bcrypt}$2a$10$hWs7z/2/4oDkWM1cp2aAEeDP5b/wdXg22lgd1Fexq52IQ6IXa.1YW")
//        .roles("USER");
//  }
}
