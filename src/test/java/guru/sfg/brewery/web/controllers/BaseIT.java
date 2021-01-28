package guru.sfg.brewery.web.controllers;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseIT {

  @Autowired
  WebApplicationContext wac;

//  @MockBean
//  BeerRepository beerRepository;
//
//  @MockBean
//  BeerInventoryRepository beerInventoryRepository;
//
//  @MockBean
//  BreweryService breweryService;
//
//  @MockBean
//  CustomerRepository customerRepository;
//
//  @MockBean
//  BeerService beerService;

  MockMvc mockMvc;

  @BeforeEach
  void setup(){
    mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
  }

  public static Stream<Arguments> getStreamAllUsers() {
    return Stream.of(Arguments.of("spring" , "learning"),
        Arguments.of("scott", "password"),
        Arguments.of("user", "password"));
  }

  public static Stream<Arguments> getStreamNotAdmin() {
    return Stream.of(Arguments.of("scott", "password"),
        Arguments.of("user", "password"));
  }

  public static Stream<Arguments> getStreamAdminCustomer(){
    return Stream.of(Arguments.of("scott","password"),Arguments.of("spring","learning"));
  }
}
