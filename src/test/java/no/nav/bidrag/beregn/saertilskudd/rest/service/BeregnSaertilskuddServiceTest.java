package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.commons.web.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

@DisplayName("BeregnSaertilskuddServiceTest")
class BeregnSaertilskuddServiceTest {

  @InjectMocks
  private BeregnSaertilskuddService beregnSaertilskuddService;

  @Mock
  private SjablonConsumer sjablonConsumerMock;
  @Mock
  private SaertilskuddCore saertilskuddCoreMock;

  @BeforeEach
  void initMocksOgSettOppSjablonMocks() {
    MockitoAnnotations.initMocks(this);
    when(sjablonConsumerMock.hentSjablonSjablontall()).thenReturn(HttpResponse.from(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()));
  }

  @Test
  @DisplayName("Skal beregne særtilskudd når retur fra consumer-moduler og core-moduler er OK")
  void skalBeregneBidragNaarReturFraConsumerOgCoreModulerErOk() {
    when(saertilskuddCoreMock.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCore());

    var beregnTotalSaertilskuddResultat = beregnSaertilskuddService.beregn(TestUtil.byggSaertilskuddGrunnlag());

    assertAll(
        () -> assertThat(beregnTotalSaertilskuddResultat.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnTotalSaertilskuddResultat.getResponseEntity().getBody()).isNotNull(),
        () -> assertThat(beregnTotalSaertilskuddResultat.getResponseEntity().getBody().getResultat()).isNotNull()
    );
  }

  @Test
  @DisplayName("Skal ha korrekt sjablon-grunnlag når beregningsmodulen kalles")
  void skalHaKorrektSjablonGrunnlagNaarBeregningsmodulenKalles() {
    var saertilskuddGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(BeregnSaertilskuddGrunnlagCore.class);

    when(saertilskuddCoreMock.beregnSaertilskudd(saertilskuddGrunnlagTilCoreCaptor.capture())).thenReturn(TestUtil.dummySaertilskuddResultatCore());

    var beregnSaertilskuddResultat = beregnSaertilskuddService.beregn(TestUtil.byggSaertilskuddGrunnlag());

    var saertilskuddGrunnlagTilCore = saertilskuddGrunnlagTilCoreCaptor.getValue();

    // For Sjablontall sjekkes at det er riktig type sjablontall. For alle sjabloner sjekkes det at datoen er innenfor beregn-fra-til-dato
    // For å finne riktig tall: Sjekk TestUtil.dummySjablonxxx; tell hvor mange sjabloner som er innefor dato og (for Sjablontall) av riktig type

    // Saertilskudd: Sjablontall (0021, 0022)
    var forventetAntallSjablonElementerSaertilskudd = 6;

    assertAll(
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getStatusCode()).isEqualTo(HttpStatus.OK),
        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getBody()).isNotNull(),

        () -> assertThat(beregnSaertilskuddResultat.getResponseEntity().getBody().getResultat()).isNotNull()
//        () -> assertThat(saertilskuddGrunnlagTilCore.getSjablonPeriodeListe().size()).isEqualTo(forventetAntallSjablonElementerSaertilskudd),

        // Sjekk at det mappes ut riktig antall for en gitt sjablon av type Sjablontall
//        () -> assertThat(underholdskostnadGrunnlagTilCore.getSjablonPeriodeListe().stream()
//            .filter(sjablonPeriodeCore -> sjablonPeriodeCore.getSjablonNavn().equals(SjablonTallNavn.ORDINAER_BARNETRYGD_BELOP.getNavn())).count())
//            .isEqualTo(TestUtil.dummySjablonSjablontallListe().stream()
//                .filter(sjablontall -> sjablontall.getTypeSjablon().equals("0001"))
//                .filter(sjablon -> (!(sjablon.getDatoFom().isAfter(LocalDate.parse("2020-01-01")) && (!(sjablon.getDatoTom()
//                    .isBefore(LocalDate.parse("2017-01-01"))))))).count()),

        // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type Sjablontall
//        () -> assertThat(underholdskostnadGrunnlagTilCore.getSjablonPeriodeListe().stream()
//            .filter(sjablonPeriodeCore -> (sjablonPeriodeCore.getSjablonNavn().equals(SjablonTallNavn.ORDINAER_BARNETRYGD_BELOP.getNavn())) &&
//                (sjablonPeriodeCore.getSjablonPeriodeDatoFraTil().getPeriodeDatoFra().equals(LocalDate.parse("2019-07-01"))))
//            .map(SjablonPeriodeCore::getSjablonInnholdListe)
//            .flatMap(Collection::stream)
//            .findFirst()
//            .map(SjablonInnholdCore::getSjablonInnholdVerdi)
//            .orElse(BigDecimal.ZERO))
//            .isEqualTo(BigDecimal.valueOf(1054)),
    );
  }

//  @Test
//  @DisplayName("Skal kaste UgyldigInputException ved feil retur fra SaertilskuddCore")
//  void skalKasteUgyldigInputExceptionVedFeilReturFraSaertilskuddCore() {
//    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
//    when(nettoBarnetilsynCoreMock.beregnNettoBarnetilsyn(any())).thenReturn(TestUtil.dummyNettoBarnetilsynResultatCore());
//    when(underholdskostnadCoreMock.beregnUnderholdskostnad(any())).thenReturn(TestUtil.dummyUnderholdskostnadResultatCore());
//    when(bpAndelUnderholdskostnadCoreMock.beregnBPsAndelUnderholdskostnad(any())).thenReturn(TestUtil.dummyBPsAndelUnderholdskostnadResultatCore());
//    when(samvaersfradragCoreMock.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCore());
//    when(kostnadsberegnetBidragCoreMock.beregnKostnadsberegnetBidrag(any())).thenReturn(TestUtil.dummyKostnadsberegnetBidragResultatCore());
//    when(saertilskuddCoreMock.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCoreMedAvvik());
//
//    assertThatExceptionOfType(UgyldigInputException.class)
//        .isThrownBy(() -> beregnSaertilskuddService.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()))
//        .withMessageContaining("Ugyldig input ved beregning av saertilskudd. Følgende avvik ble funnet:")
//        .withMessageContaining("beregnDatoFra kan ikke være null")
//        .withMessageContaining("periodeDatoTil må være etter periodeDatoFra");
//  }

//  @Test
//  @DisplayName("Skal kaste UgyldigInputException ved validering av inputdata - saertilskudd")
//  void skalKasteUgyldigInputExceptionVedValideringAvInputdataSaertilskudd() {
//    when(bidragsevneCoreMock.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore());
//    when(nettoBarnetilsynCoreMock.beregnNettoBarnetilsyn(any())).thenReturn(TestUtil.dummyNettoBarnetilsynResultatCore());
//    when(underholdskostnadCoreMock.beregnUnderholdskostnad(any())).thenReturn(TestUtil.dummyUnderholdskostnadResultatCore());
//    when(bpAndelUnderholdskostnadCoreMock.beregnBPsAndelUnderholdskostnad(any())).thenReturn(TestUtil.dummyBPsAndelUnderholdskostnadResultatCore());
//    when(samvaersfradragCoreMock.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCore());
//    when(kostnadsberegnetBidragCoreMock.beregnKostnadsberegnetBidrag(any())).thenReturn(TestUtil.dummyKostnadsberegnetBidragResultatCore());
//    assertThatExceptionOfType(UgyldigInputException.class)
//        .isThrownBy(() -> beregnSaertilskuddService.beregn(
//            new BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2017-01-01"), LocalDate.parse("2020-01-01"), TestUtil.byggSoknadsbarnGrunnlag(),
//                TestUtil.byggInntektGrunnlag(), TestUtil.byggBidragsevneGrunnlag(), TestUtil.byggNettoBarnetilsynGrunnlag(),
//                TestUtil.byggUnderholdskostnadGrunnlag(), TestUtil.byggSamvaersfradragGrunnlag(),
//                TestUtil.byggSaertilskuddGrunnlagUtenBarnetilleggBMBruttoBelop())))
//        .withMessageContaining("BM barnetilleggBruttoBelop kan ikke være null");
//  }
}
