package guru.sfg.brewery.web.controllers;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.sfg.brewery.domain.Beer;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
public class BeerControllerIT extends BaseIT{

  UUID uuid;

  @BeforeEach
  void setup(){
    final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
    uuid = UUID.fromString(id);
    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
  }
  @WithMockUser("spring")
  @Test
  void findBeers() throws Exception{
    mockMvc.perform(get("/beers/find"))
        .andExpect(status().isOk())
        .andExpect(view().name("beers/findBeers"))
        .andExpect(model().attributeExists("beer"));
  }

  @Test
  void initCreationForm() throws Exception{
    mockMvc.perform(get("/beers/new").with(httpBasic("user","password")))
        .andExpect(status().isOk())
        .andExpect(view().name("beers/createBeer"))
        .andExpect(model().attributeExists("beer"));
  }

  @Test
  void initCreationFormWithSpring() throws Exception{
    mockMvc.perform(get("/beers/new").with(httpBasic("spring","learning")))
        .andExpect(status().isOk())
        .andExpect(view().name("beers/createBeer"))
        .andExpect(model().attributeExists("beer"));
  }

  @Test
  void findBeersWithHttpBasic() throws Exception{
    mockMvc.perform(get("/beers/find").with(httpBasic("spring","learning")))
        .andExpect(status().isOk())
        .andExpect(view().name("beers/findBeers"))
        .andExpect(model().attributeExists("beer"));
  }
  @WithMockUser("spring")
  @Test
  void showBeer() throws Exception{

    when(beerRepository.findById(uuid)).thenReturn(Optional.of(Beer.builder().id(uuid).build()));
    mockMvc.perform(get("/beers/"+uuid))
        .andExpect(status().isOk())
        .andExpect(view().name("beers/beerDetails"))
        .andExpect(model().attribute("beer", hasProperty("id", is(uuid))));
  }
}
