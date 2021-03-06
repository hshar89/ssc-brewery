package guru.sfg.brewery.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public abstract class AbstractRestAuthFilter extends AbstractAuthenticationProcessingFilter {

  protected AbstractRestAuthFilter(
      RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Request is to process authentication");
    }
    try {
      Authentication authResult = this.attemptAuthentication(request, response);
      if (authResult != null) {
        this.successfulAuthentication(request, response, chain, authResult);
      } else {
        chain.doFilter(request, response);
      }
    } catch (AuthenticationException ex) {
      System.out.println("Authetication Failure: " + ex);
      unsuccessfulAuthentication(request, response, ex);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Authentication request failed: " + failed.toString(), failed);
      this.logger.debug("Updated SecurityContextHolder to contain null Authentication");
    }
    response.sendError(HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase());
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse)
      throws AuthenticationException, IOException, ServletException {
    String username = getUserName(httpServletRequest);
    String password = getPassword(httpServletRequest);
    if(username ==null){
      username = "";
    }
    if(password == null){
      password = "";
    }
    System.out.println("Authenticating user: "+username);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
    if(!StringUtils.isEmpty(username)) {
      return this.getAuthenticationManager().authenticate(token);
    }
    return null;
  }

  protected abstract String getPassword(HttpServletRequest httpServletRequest);

  protected abstract String getUserName(HttpServletRequest httpServletRequest);
}
