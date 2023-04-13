package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import no.nav.bidrag.beregn.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.BPsAndelSaertilskuddCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddTest;
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.samvaersfradrag.SamvaersfradragCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore;
import no.nav.bidrag.commons.web.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@SpringBootTest(classes = BidragBeregnSaertilskuddTest.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("BeregnSaertilskuddServiceTest")
class BeregnSaertilskuddServiceTest {

  @Autowired
  private BeregnSaertilskuddService beregnSaertilskuddService;

  @MockBean
  private SjablonConsumer sjablonConsumerMock;
  @MockBean
  private BidragsevneCore bidragsevneCoreMock;
  @MockBean
  private BPsAndelSaertilskuddCore bpAndelSaertilskuddCoreMock;
  @MockBean
  private SamvaersfradragCore samvaersfradragCoreMock;
  @MockBean
  private SaertilskuddCore saertilskuddCoreMock;

  @BeforeEach
  void settOppSjablonMocks() {
    MockitoAnnotations.initMocks(this);
    when(sjablonConsumerMock.hentSjablonSjablontall())
        .thenReturn(HttpResponse.Companion.from(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()));
    when(sjablonConsumerMock.hentSjablonSamvaersfradrag())
        .thenReturn(HttpResponse.Companion.from(HttpStatus.OK, TestUtil.dummySjablonSamvaersfradragListe()));
    when(sjablonConsumerMock.hentSjablonBidragsevne())
        .thenReturn(HttpResponse.Companion.from(HttpStatus.OK, TestUtil.dummySjablonBidragsevneListe()));
    when(sjablonConsumerMock.hentSjablonTrinnvisSkattesats())
        .thenReturn(HttpResponse.Companion.from(HttpStatus.OK, TestUtil.dummySjablonTrinnvisSkattesatsListe()));
  }

  @Test
  @DisplayName("Skal ha korrekt sjablon-grunnlag når beregningsmodulen kalles")
  void skalHaKorrektSjablonGrunnlagNaarBeregningsmodulenKalles() {
    var bidragsevneGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnBidragsevneGrunnlagCore.class);
    var samvaersfradragGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnSamvaersfradragGrunnlagCore.class);
    var bpAndelSaertilskuddGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnBPsAndelSaertilskuddGrunnlagCore.class);
    var saertilskuddGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnSaertilskuddGrunnlagCore.class);

    when(bidragsevneCoreMock.beregnBidragsevne(bidragsevneGrunnlagTilCoreCaptor.capture()))
        .thenReturn(TestUtil.dummyBidragsevneResultatCore());
    when(bpAndelSaertilskuddCoreMock.beregnBPsAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCoreCaptor.capture()))
        .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore());
    when(samvaersfradragCoreMock.beregnSamvaersfradrag(samvaersfradragGrunnlagTilCoreCaptor.capture()))
        .thenReturn(TestUtil.dummySamvaersfradragResultatCore());
    when(saertilskuddCoreMock.beregnSaertilskudd(saertilskuddGrunnlagTilCoreCaptor.capture()))
        .thenReturn(TestUtil.dummySaertilskuddResultatCore());

    var beregnTotalSaertilskuddResultat = beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag());

    var bidragsevneGrunnlagTilCore = bidragsevneGrunnlagTilCoreCaptor.getValue();
    var bpAndelSaertilskuddGrunnlagTilCore = bpAndelSaertilskuddGrunnlagTilCoreCaptor.getValue();
    var samvaersfradragGrunnlagTilCore = samvaersfradragGrunnlagTilCoreCaptor.getValue();
    var saertilskuddGrunnlagTilCore = saertilskuddGrunnlagTilCoreCaptor.getValue();

    // For Sjablontall sjekkes at det er riktig type sjablontall. For alle sjabloner sjekkes det at datoen er innenfor beregn-fra-til-dato
    // For å finne riktig tall: Sjekk TestUtil.dummySjablonxxx; tell hvor mange sjabloner som er innenfor dato og (for Sjablontall) av riktig type

    // Bidragsevne: Sjablontall (0004, 0017, 0019, 0023, 0025, 0027, 0028, 0039, 0040) + Bidragsevne + TrinnvisSkattesats
    var forventetAntallSjablonElementerBidragsevne = 9 + 2 + 4;
    // BPs andel særtilskudd: Sjablontall (0004, 0005, 0030, 0031, 0039)
    var forventetAntallSjablonElementerBPsAndelSaertilskudd = 5;
    // Samværsfradrag: Samvaersfradrag
    var forventetAntallSjablonElementerSamvaersfradrag = 21;
    // Saertilskudd: Ingen sjabloner
    var forventetAntallSjablonElementerSaertilskudd = 0;

    assertAll(
        () -> assertThat(beregnTotalSaertilskuddResultat.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnTotalSaertilskuddResultat.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(bidragsevneGrunnlagTilCore.getSjablonPeriodeListe()).hasSize(forventetAntallSjablonElementerBidragsevne),
        () -> assertThat(bpAndelSaertilskuddGrunnlagTilCore.getSjablonPeriodeListe()).hasSize(forventetAntallSjablonElementerBPsAndelSaertilskudd),
        () -> assertThat(samvaersfradragGrunnlagTilCore.getSjablonPeriodeListe()).hasSize(forventetAntallSjablonElementerSamvaersfradrag),
        () -> assertThat(saertilskuddGrunnlagTilCore.getSjablonPeriodeListe()).hasSize(forventetAntallSjablonElementerSaertilskudd),

        // Sjekk at det mappes ut riktig antall for en gitt sjablon av type Sjablontall
        () -> assertThat(bidragsevneGrunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getNavn().equals(SjablonTallNavn.TRYGDEAVGIFT_PROSENT.getNavn())).count())
            .isEqualTo(TestUtil.dummySjablonSjablontallListe().stream()
                .filter(sjablontall -> sjablontall.getTypeSjablon().equals("0017"))
                .filter(sjablon -> ((!(sjablon.getDatoFom().isAfter(LocalDate.parse("2020-09-01")))) && (!(sjablon.getDatoTom()
                    .isBefore(LocalDate.parse("2020-08-01")))))).count()),

        // Sjekk at det mappes ut riktig antall sjabloner av type Bidragsevne
        () -> assertThat(bidragsevneGrunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getNavn().equals(SjablonNavn.BIDRAGSEVNE.getNavn())).count())
            .isEqualTo(TestUtil.dummySjablonBidragsevneListe().stream()
                .filter(sjablon -> ((!(sjablon.getDatoFom().isAfter(LocalDate.parse("2020-09-01")))) && (!(sjablon.getDatoTom()
                    .isBefore(LocalDate.parse("2020-08-01")))))).count()),

        // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type Sjablontall
        () -> assertThat(bidragsevneGrunnlagTilCore.getSjablonPeriodeListe().stream()
            .filter(sjablonPeriodeCore -> (sjablonPeriodeCore.getNavn().equals(SjablonTallNavn.TRYGDEAVGIFT_PROSENT.getNavn())) &&
                (sjablonPeriodeCore.getPeriode().getDatoFom().equals(LocalDate.parse("2014-01-01"))))
            .map(SjablonPeriodeCore::getInnholdListe)
            .flatMap(Collection::stream)
            .findFirst()
            .map(SjablonInnholdCore::getVerdi)
            .orElse(BigDecimal.ZERO))
            .isEqualTo(BigDecimal.valueOf(8.2)));

  }

  @Test
  @DisplayName("Skal beregne særtilskudd")
  void skalBeregneSaertilskudd() {
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
    when(bpAndelSaertilskuddCoreMock.beregnBPsAndelSaertilskudd(any())).thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore());
    when(samvaersfradragCoreMock.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCore());
    when(saertilskuddCoreMock.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCore());

    var beregnSaertilskuddResultat = beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag());
    assertAll(
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getBody().getBeregnetSaertilskuddPeriodeListe()).isNotNull(),
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getBody().getBeregnetSaertilskuddPeriodeListe()).hasSize(1)
    );
  }

  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BidragsevneCore")
  void skalKasteUgyldigInputExceptionVedFeilReturFraBidragsevneCore() {
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()))
        .withMessageContaining("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet:")
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }

  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BPsAndelSaertilskuddCore")
  void skalKasteUgyldigInputExceptionVedFeilReturFraBPsAndelSaertilskuddCore() {
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
    when(bpAndelSaertilskuddCoreMock.beregnBPsAndelSaertilskudd(any()))
        .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()))
        .withMessageContaining("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet:")
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }

  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra SamvaersfradragCore")
  void skalKasteUgyldigInputExceptionVedFeilReturFraSamvaersfradragCore() {
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
    when(bpAndelSaertilskuddCoreMock.beregnBPsAndelSaertilskudd(any())).thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore());
    when(samvaersfradragCoreMock.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()))
        .withMessageContaining("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet:")
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }

  @Test
  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra SaertilskuddCore")
  void skalKasteUgyldigInputExceptionVedFeilReturFraSaertilskuddCore() {
    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
    when(bpAndelSaertilskuddCoreMock.beregnBPsAndelSaertilskudd(any())).thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore());
    when(samvaersfradragCoreMock.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCore());
    when(saertilskuddCoreMock.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCoreMedAvvik());

    assertThatExceptionOfType(UgyldigInputException.class)
        .isThrownBy(() -> beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()))
        .withMessageContaining("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet:")
        .withMessageContaining("beregnDatoFra kan ikke være null")
        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
  }
}
