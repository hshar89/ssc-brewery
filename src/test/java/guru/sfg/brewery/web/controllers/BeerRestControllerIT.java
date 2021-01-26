package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT{

  @Test
  void findBeers() throws Exception{
    mockMvc.perform(get("/api/v1/beer/"))
        .andExpect(status().isOk());
  }
  @Test
  void findBeerById() throws Exception{
    mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
        .andExpect(status().isOk());
  }
  @Test
  void findBeerByUPC() throws Exception{
    mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteBeer() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
        .header("Api-Key","spring").header("Api-Secret","learning"))
        .andExpect(status().isOk());
  }
  @Test
  void deleteBeerHttpBasic() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
        .with(httpBasic("spring","learning")))
        .andExpect(status().is2xxSuccessful());
  }
  @Test
  void deleteBeerNoAuth() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteBeerBadCred() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
        .header("Api-Key","spring").header("Api-Secret","test1234"))
        .andExpect(status().isUnauthorized());
  }
  @Test
  void deleteBeerUrlParameterAuth() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
        .param("apiKey","spring").param("apiSecret","learning"))
        .andExpect(status().isOk());
  }

}
