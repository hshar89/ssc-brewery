package guru.sfg.brewery.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;


public class RestHeaderAuthFilter extends AbstractRestAuthFilter {

  public RestHeaderAuthFilter(
      RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  protected String getPassword(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getHeader("Api-Secret");
  }

  protected String getUserName(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getHeader("Api-Key");
  }
}
