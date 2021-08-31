package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.HttpStatus.OK;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import no.nav.bidrag.beregn.saertilskudd.rest.SaertilskuddDelberegningResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddLocal;
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.wiremock_stub.SjablonApiStub;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
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
  private BigDecimal forventetBPAndelSaertilskuddProsentBarn;
  private BigDecimal forventetBPAndelSaertilskuddBelopBarn;
  private BigDecimal forventetSamvaersfradragBelopBarn1;
  private BigDecimal forventetSamvaersfradragBelopBarn2;
  private BigDecimal forventetSaertilskuddBelopBarn;
  private String forventetSaertilskuddResultatkodeBarn;


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
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(60.6);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(4242);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(457);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(4242);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_INNVILGET";

    utfoerBeregningerOgEvaluerResultat_EttSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 2")
  void skalKalleCoreOgReturnereEtResultat_Eksempel02() {
    // Enkel beregning med full evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel2.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(6696);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(49.7);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(2982);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(2982);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_INNVILGET";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 3")
  void skalKalleCoreOgReturnereEtResultat_Eksempel03() {
    // Enkel beregning med full evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel3.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(6149);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(55.7);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_INNVILGET";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 4")
  void skalKalleCoreOgReturnereEtResultat_Eksempel04() {
    // Beregning med manglende evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel4.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(6149);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(55.7);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_IKKE_FULL_BIDRAGSEVNE";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 5")
  void skalKalleCoreOgReturnereEtResultat_Eksempel05() {
    // Beregning med manglende evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel5.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(9962);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(62.8);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(7536);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(7536);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_IKKE_FULL_BIDRAGSEVNE";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 6")
  void skalKalleCoreOgReturnereEtResultat_Eksempel06() {
    // Enkel beregning med full evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel6.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(10891);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(55.1);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(6612);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(6612);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_INNVILGET";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 7")
  void skalKalleCoreOgReturnereEtResultat_Eksempel07() {
    // Beregning med manglende evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel7.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(6149);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(55.7);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_IKKE_FULL_BIDRAGSEVNE";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }

  @Test
  @DisplayName("skal kalle core og returnere et resultat - eksempel 8")
  void skalKalleCoreOgReturnereEtResultat_Eksempel08() {
    // Beregning med manglende evne, to barn
    filnavn = "src/test/resources/testfiler/saertilskudd_eksempel8.json";

    forventetBidragsevneBelop = BigDecimal.valueOf(6149);
    forventetBPAndelSaertilskuddProsentBarn = BigDecimal.valueOf(55.7);
    forventetBPAndelSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSamvaersfradragBelopBarn1 = BigDecimal.valueOf(1513);
    forventetSamvaersfradragBelopBarn2 = BigDecimal.valueOf(1513);
    forventetSaertilskuddBelopBarn = BigDecimal.valueOf(6684);
    forventetSaertilskuddResultatkodeBarn = "SAERTILSKUDD_IKKE_FULL_BIDRAGSEVNE";

    utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn();
  }


  private void utfoerBeregningerOgEvaluerResultat_EttSoknadsbarn() {
    var request = lesFilOgByggRequest(filnavn);

    // Kall rest-API for saertilskudd
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnetTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    var saertilskuddDelberegningResultat = new SaertilskuddDelberegningResultat(totalSaertilskuddResultat);

    var alleReferanser = TestUtil.hentAlleReferanser(totalSaertilskuddResultat);
    var referanserIGrunnlagListe = totalSaertilskuddResultat.getGrunnlagListe().stream().map(grunnlag -> grunnlag.getReferanse()).toList();

    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe()).isNotNull(),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().size()).isEqualTo(1),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat().getBelop()).isEqualTo(forventetSaertilskuddBelopBarn),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat().getKode()).isEqualTo(forventetSaertilskuddResultatkodeBarn),
        () -> assertThat(saertilskuddDelberegningResultat.bidragsevneListe).size().isEqualTo(1),
        () -> assertThat(saertilskuddDelberegningResultat.bidragsevneListe.get(0).getBelop()).isEqualTo(forventetBidragsevneBelop),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe).size().isEqualTo(1),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getBelop()).isEqualTo(forventetBPAndelSaertilskuddBelopBarn),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getProsent()).isEqualTo(forventetBPAndelSaertilskuddProsentBarn),
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe).size().isEqualTo(1),
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.get(0).getBelop()).isEqualTo(forventetSamvaersfradragBelopBarn1),
        () -> assertThat(referanserIGrunnlagListe.containsAll(alleReferanser)).isTrue()
    );
  }

  private void utfoerBeregningerOgEvaluerResultat_ToSoknadsbarn() {
    var request = lesFilOgByggRequest(filnavn);

    // Kall rest-API for Saertilskudd
    var responseEntity = httpHeaderTestRestTemplate.exchange(url, HttpMethod.POST, request, BeregnetTotalSaertilskuddResultat.class);
    var totalSaertilskuddResultat = responseEntity.getBody();

    var saertilskuddDelberegningResultat = new SaertilskuddDelberegningResultat(totalSaertilskuddResultat);

    var alleReferanser = TestUtil.hentAlleReferanser(totalSaertilskuddResultat);
    var referanserIGrunnlagListe = totalSaertilskuddResultat.getGrunnlagListe().stream().map(grunnlag -> grunnlag.getReferanse()).toList();


    assertAll(
        () -> assertThat(responseEntity.getStatusCode()).isEqualTo(OK),
        () -> assertThat(totalSaertilskuddResultat).isNotNull(),

        // Sjekk BeregnBPBidragsevneResultat
        () -> assertThat(saertilskuddDelberegningResultat.bidragsevneListe.size()).isEqualTo(1),
        () -> assertThat(saertilskuddDelberegningResultat.bidragsevneListe.get(0).getBelop()).isEqualTo(forventetBidragsevneBelop),

        // Sjekk BeregnBPAndelSaertilskuddResultat
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.size()).isEqualTo(1),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getBelop()).isEqualTo(forventetBPAndelSaertilskuddBelopBarn),
        () -> assertThat(saertilskuddDelberegningResultat.bpsAndelSaertilskuddListe.get(0).getProsent()).isEqualTo(forventetBPAndelSaertilskuddProsentBarn),

        // Sjekk BeregnBPSamvaersfradragResultat
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.size()).isEqualTo(2),
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.get(0).getBelop()).isEqualTo(forventetSamvaersfradragBelopBarn1),
        () -> assertThat(saertilskuddDelberegningResultat.samvaersfradragListe.get(1).getBelop()).isEqualTo(forventetSamvaersfradragBelopBarn2),

        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().size()).isEqualTo(1),

        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat().getBelop()).isEqualTo(forventetSaertilskuddBelopBarn),
        () -> assertThat(totalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe().get(0).getResultat()
            .getKode()).isEqualTo(forventetSaertilskuddResultatkodeBarn),
        () -> assertThat(referanserIGrunnlagListe.containsAll(alleReferanser)).isTrue()
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
