package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddLocal;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.wiremock_stub.SjablonApiStub;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddResultat;
import no.nav.bidrag.commons.web.test.HttpHeaderTestRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BidragBeregnSaertilskuddLocal.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@AutoConfigureWireMock(port = 8096)
public class BeregnSaertilskuddControllerIntegrationTest {

  @Autowired
  private HttpHeaderTestRestTemplate httpHeaderTestRestTemplate;

  @Autowired
  private SjablonApiStub sjablonApiStub;

  @LocalServerPort
  private int port;

  private String url;
  private String filnavn;

  private BigDecimal forventetBidragsevneBelop;
  private BigDecimal forventetBPAndelSaertilskuddProsentBarn1;
  private BigDecimal forventetBPAndelSaertilskuddBelopBarn1;
  private BigDecimal forventetBPAndelSaertilskuddBarnetErSelvforsorgetBarn1;
  private BigDecimal forventetSamvaersfradragBelopBarn1;
  private BigDecimal forventetSaertilskuddBelopBarn1;
  private String forventetSaertilskuddResultatkodeBarn1;
  private BigDecimal forventetBPAndelSaertilskuddBelopBarn2;
  private BigDecimal forventetBPAndelSaertilskuddProsentBarn2;
  private BigDecimal forventetBPAndelSaertilskuddBarnetErSelvforsorgetBarn2;
  private BigDecimal forventetSamvaersfradragBelopBarn2;
  private BigDecimal forventetSaertilskuddBelopBarn2;
  private String forventetSaertilskuddResultatkodeBarn2;


  @BeforeEach
  void init() {
    // Sett opp wiremock mot sjablon-tjenestene
    sjablonApiStub.settOppSjablonStub();

    // Bygg opp url
    url = "http://localhost:" + port + "/bidrag-beregn-saertilskudd-rest/beregn/saertilskudd";
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 1")
  void skalKalleCoreOgReturnereEtResultat_Eksempel01() {
    // Enkel beregning med full evne, ett barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel1.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(11069);
    forventetBPAndelSaertilskuddProsentBarn1 = BigDecimal.valueOf(60.6);
    forventetBPAndelSaertilskuddBelopBarn1 = BigDecimal.valueOf(4242);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(457);
    forventetSaertilskuddBelopBarn1 = BigDecimal.valueOf(4242);
    forventetSaertilskuddResultatkodeBarn1 = "SAERTILSKUDD_INNVILGET";

    utfoerBeregningerOgEvaluerResultat_EttSoknadsbarn();
  }



  private void utfoerBeregningerOgEvaluerResultat_EttSoknadsbarn() {
    var request = lesFilOgByggRequest(filnavn);

    // Kall rest-API for saertilskudd
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        // Sjekk BeregnBPBidragsevneResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatEvneBelop()).isEqualTo(forventetBidragsevneBelop),

        // Sjekk BeregnBPAndelSaertilskuddResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelProsent()).isEqualTo(forventetBPAndelSaertilskuddProsentBarn1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelBelop()).isEqualTo(forventetBPAndelSaertilskuddBelopBarn1),

        // Sjekk BeregnBPSamvaersfradragResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0)
            .getResultatBeregningListe().get(0).getResultatBelop()).isEqualTo(forventetSamvaersfradragBelopBarn1),

        // Sjekk BeregnSaertilskuddResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0)
            .getResultatBeregning().getResultatBelop().compareTo(forventetSaertilskuddBelopBarn1)).isZero(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0)
            .getResultatBeregning().getResultatKode()).isEqualTo(forventetSaertilskuddResultatkodeBarn1)
    );
  }

  private void utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn() {
    var request = lesFilOgByggRequest(filnavn);

    // Kall rest-API for Saertilskudd
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        // Sjekk BeregnBPBidragsevneResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPBidragsevneResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatEvneBelop()).isEqualTo(forventetBidragsevneBelop),

        // Sjekk BeregnBPAndelSaertilskuddResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().size()).isEqualTo(2),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelBelop()).isEqualTo(forventetBPAndelSaertilskuddBelopBarn1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatAndelProsent()).isEqualTo(forventetBPAndelSaertilskuddProsentBarn1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(1).getResultatBeregning()
            .getResultatAndelBelop()).isEqualTo(forventetBPAndelSaertilskuddBelopBarn2),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPAndelSaertilskuddResultat().getResultatPeriodeListe().get(1).getResultatBeregning()
            .getResultatAndelProsent()).isEqualTo(forventetBPAndelSaertilskuddProsentBarn2),


        // Sjekk BeregnBPSamvaersfradragResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().size()).isEqualTo(2),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(0).getResultatBeregningListe()
            .get(0).getResultatBelop()).isEqualTo(forventetSamvaersfradragBelopBarn1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnBPSamvaersfradragResultat().getResultatPeriodeListe().get(1).getResultatBeregningListe()
            .get(0).getResultatBelop()).isEqualTo(forventetSamvaersfradragBelopBarn2),

        // Sjekk BeregnSaertilskuddResultat
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().size()).isEqualTo(1),

        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatBelop().compareTo(forventetSaertilskuddBelopBarn1)).isZero(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnSaertilskuddResultat().getResultatPeriodeListe().get(0).getResultatBeregning()
            .getResultatKode()).isEqualTo(forventetSaertilskuddResultatkodeBarn1)

    );
  }

  private HttpEntity<String> lesFilOgByggRequest(String filnavn) {
    var json = "";

    // Les inn fil med request-data (json)
    try {
      json = Files.readString(Paths.get(filnavn));
    } catch (Exception e) {
      fail("Klarte ikke å lese fil: " + filnavn);
    }

    // Lag request
    return initHttpEntity(json);
  }

  private <T> HttpEntity<T> initHttpEntity(T body) {
    var httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(body, httpHeaders);
  }

}
