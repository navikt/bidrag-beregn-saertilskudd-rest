package no.nav.bidrag.beregn.saertilskudd.rest;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootApplication
@ComponentScan(excludeFilters = {@Filter(type = ASSIGNABLE_TYPE, value = BidragBeregnSaertilskudd.class)})
public class BidragBeregnSaertilskuddLocal {

  public static final String LOCAL = "local"; // Enable endpoint testing with Swagger locally, see application.yaml

  public static void main(String... args) {
    SpringApplication app = new SpringApplication(BidragBeregnSaertilskuddLocal.class);
    app.setAdditionalProfiles(LOCAL);
    app.run(args);
  }
}
