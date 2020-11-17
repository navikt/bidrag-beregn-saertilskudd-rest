package no.nav.bidrag.beregn.saertilskudd.rest;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import no.nav.bidrag.commons.web.test.HttpHeaderTestRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(excludeFilters = {@Filter(type = ASSIGNABLE_TYPE, value = BidragBeregnSaertilskudd.class)})
public class BidragBeregnSaertilskuddLocal {

  public static final String LOCAL = "local"; // Enable endpoint testing with Swagger locally, see application.yaml

  public static void main(String... args) {
    SpringApplication app = new SpringApplication(BidragBeregnSaertilskuddLocal.class);
    app.setAdditionalProfiles(LOCAL);
    app.run(args);
  }

  @Configuration
  public static class TestRestTemplateConfiguration {

    @Bean
    HttpHeaderTestRestTemplate httpHeaderTestRestTemplate(TestRestTemplate testRestTemplate) {
      return new HttpHeaderTestRestTemplate(testRestTemplate);
    }
  }
}
