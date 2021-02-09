package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BreweriesRestControllerIT extends BaseIT{

  @Test
  void getBrewriesWithCustomerRole() throws Exception {
    mockMvc.perform(get("/brewery/api/v1/breweries")
        .with(httpBasic("scott","tiger")))
        .andExpect(status().isOk());
  }
  @Test
  void getBrewriesWithUserRole() throws Exception {
    mockMvc.perform(get("/brewery/api/v1/breweries")
        .with(httpBasic("user","password")))
        .andExpect(status().isForbidden());
  }
  @Test
  void getBrewriesWithUserAdmin() throws Exception {
    mockMvc.perform(get("/brewery/api/v1/breweries")
        .with(httpBasic("spring","guru")))
        .andExpect(status().is2xxSuccessful());
  }
  @Test
  void getBrewriesNoAuth() throws Exception {
    mockMvc.perform(get("/brewery/api/v1/breweries"))
        .andExpect(status().isUnauthorized());
  }
}

