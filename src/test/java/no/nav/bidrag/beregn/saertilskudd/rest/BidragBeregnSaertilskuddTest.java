package no.nav.bidrag.beregn.saertilskudd.rest;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootApplication
@ComponentScan(excludeFilters = {@Filter(type = ASSIGNABLE_TYPE, value = {BidragBeregnSaertilskudd.class, BidragBeregnSaertilskuddLocal.class})})
public class BidragBeregnSaertilskuddTest {

  public static void main(String... args) {
    SpringApplication app = new SpringApplication(BidragBeregnSaertilskuddTest.class);
    app.run(args);
  }
}