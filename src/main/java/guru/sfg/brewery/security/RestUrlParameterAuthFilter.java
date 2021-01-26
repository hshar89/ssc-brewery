package guru.sfg.brewery.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class RestUrlParameterAuthFilter extends AbstractRestAuthFilter {

  public RestUrlParameterAuthFilter(
      RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  protected String getPassword(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getParameter("apiSecret");
  }

  protected String getUserName(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getParameter("apiKey");
  }


}
