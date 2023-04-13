package no.nav.bidrag.beregn.saertilskudd.rest;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import no.nav.bidrag.beregn.saertilskudd.rest.consumer.wiremock_stub.SjablonApiStub;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableJwtTokenValidation(ignore = {"org.springdoc", "org.springframework"})
@ComponentScan(excludeFilters = {@Filter(type = ASSIGNABLE_TYPE, value = BidragBeregnSaertilskudd.class)})
public class BidragBeregnSaertilskuddLocal {

  public static final String LOCAL = "local"; // Enable endpoint testing with Swagger locally, see application.yaml

  public static void main(String... args) {
    SpringApplication app = new SpringApplication(BidragBeregnSaertilskuddLocal.class);
    app.setAdditionalProfiles(LOCAL);
    ConfigurableApplicationContext context = app.run(args);

    // Making sure wiremock stubs are set up
    context.getBean(SjablonApiStub.class).settOppSjablonStub();
  }
}
