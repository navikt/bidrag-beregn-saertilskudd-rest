package no.nav.bidrag.beregn.saertilskudd.rest.service

import no.nav.bidrag.beregn.bidragsevne.BidragsevneCore
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.BPsAndelSaertilskuddCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.saertilskudd.rest.BidragBeregnSaertilskuddTest
import no.nav.bidrag.beregn.saertilskudd.rest.TestUtil
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.SamvaersfradragCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore
import no.nav.bidrag.commons.web.HttpResponse.Companion.from
import no.nav.bidrag.domain.enums.sjablon.SjablonNavn
import no.nav.bidrag.domain.enums.sjablon.SjablonTallNavn
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest(classes = [BidragBeregnSaertilskuddTest::class], webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension::class)
@DisplayName("BeregnSaertilskuddServiceTest")
@Disabled
internal class BeregnSaertilskuddServiceTest {
    @InjectMocks
    private val beregnSaertilskuddService: BeregnSaertilskuddService? = null

    @Mock
    private val sjablonConsumerMock: SjablonConsumer? = null

    @Mock
    private val bidragsevneCoreMock: BidragsevneCore? = null

    @Mock
    private val bpAndelSaertilskuddCoreMock: BPsAndelSaertilskuddCore? = null

    @Mock
    private val samvaersfradragCoreMock: SamvaersfradragCore? = null

    @Mock
    private val saertilskuddCoreMock: SaertilskuddCore? = null
    @BeforeEach
    fun settOppSjablonMocks() {
        `when`(sjablonConsumerMock?.hentSjablonSjablontall())
            .thenReturn(from(HttpStatus.OK, TestUtil.dummySjablonSjablontallListe()))
        `when`(sjablonConsumerMock?.hentSjablonSamvaersfradrag())
            .thenReturn(from(HttpStatus.OK, TestUtil.dummySjablonSamvaersfradragListe()))
        `when`(sjablonConsumerMock?.hentSjablonBidragsevne())
            .thenReturn(from(HttpStatus.OK, TestUtil.dummySjablonBidragsevneListe()))
        `when`(sjablonConsumerMock?.hentSjablonTrinnvisSkattesats())
            .thenReturn(from(HttpStatus.OK, TestUtil.dummySjablonTrinnvisSkattesatsListe()))
    }

    @Test
    @DisplayName("Skal ha korrekt sjablon-grunnlag når beregningsmodulen kalles")
    fun skalHaKorrektSjablonGrunnlagNaarBeregningsmodulenKalles() {
        val bidragsevneGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(
            BeregnBidragsevneGrunnlagCore::class.java
        )
        val samvaersfradragGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(
            BeregnSamvaersfradragGrunnlagCore::class.java
        )
        val bpAndelSaertilskuddGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(
            BeregnBPsAndelSaertilskuddGrunnlagCore::class.java
        )
        val saertilskuddGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(
            BeregnSaertilskuddGrunnlagCore::class.java
        )
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(bidragsevneGrunnlagTilCoreCaptor.capture()))
            .thenReturn(TestUtil.dummyBidragsevneResultatCore())
        `when`(bpAndelSaertilskuddCoreMock!!.beregnBPsAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCoreCaptor.capture()))
            .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore())
        `when`(samvaersfradragCoreMock!!.beregnSamvaersfradrag(samvaersfradragGrunnlagTilCoreCaptor.capture()))
            .thenReturn(TestUtil.dummySamvaersfradragResultatCore())
        `when`(saertilskuddCoreMock!!.beregnSaertilskudd(saertilskuddGrunnlagTilCoreCaptor.capture()))
            .thenReturn(TestUtil.dummySaertilskuddResultatCore())
        val beregnTotalSaertilskuddResultat = beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag())
        val (_, _, _, _, _, _, _, sjablonPeriodeListe) = bidragsevneGrunnlagTilCoreCaptor.value
        val (_, _, _, _, _, _, sjablonPeriodeListe1) = bpAndelSaertilskuddGrunnlagTilCoreCaptor.value
        val (_, _, _, sjablonPeriodeListe2) = samvaersfradragGrunnlagTilCoreCaptor.value
        val (_, _, _, _, _, _, _, sjablonPeriodeListe3) = saertilskuddGrunnlagTilCoreCaptor.value

        // For Sjablontall sjekkes at det er riktig type sjablontall. For alle sjabloner sjekkes det at datoen er innenfor beregn-fra-til-dato
        // For å finne riktig tall: Sjekk TestUtil.dummySjablonxxx; tell hvor mange sjabloner som er innenfor dato og (for Sjablontall) av riktig type

        // Bidragsevne: Sjablontall (0004, 0017, 0019, 0023, 0025, 0027, 0028, 0039, 0040) + Bidragsevne + TrinnvisSkattesats
        val forventetAntallSjablonElementerBidragsevne = 9 + 2 + 4
        // BPs andel særtilskudd: Sjablontall (0004, 0005, 0030, 0031, 0039)
        val forventetAntallSjablonElementerBPsAndelSaertilskudd = 5
        // Samværsfradrag: Samvaersfradrag
        val forventetAntallSjablonElementerSamvaersfradrag = 21
        // Saertilskudd: Ingen sjabloner
        val forventetAntallSjablonElementerSaertilskudd = 0
        assertAll(
            Executable { assertThat(beregnTotalSaertilskuddResultat.responseEntity.statusCode).isEqualTo(HttpStatus.OK) },
            Executable { assertThat(beregnTotalSaertilskuddResultat.responseEntity.body).isNotNull() },
            Executable { assertThat(sjablonPeriodeListe).hasSize(forventetAntallSjablonElementerBidragsevne) },
            Executable { assertThat(sjablonPeriodeListe1).hasSize(forventetAntallSjablonElementerBPsAndelSaertilskudd) },
            Executable { assertThat(sjablonPeriodeListe2).hasSize(forventetAntallSjablonElementerSamvaersfradrag) },
            Executable {
                assertThat(sjablonPeriodeListe3).hasSize(forventetAntallSjablonElementerSaertilskudd)
            },  // Sjekk at det mappes ut riktig antall for en gitt sjablon av type Sjablontall
            Executable {
                assertThat(sjablonPeriodeListe.stream()
                    .filter { (_, navn): SjablonPeriodeCore -> navn == SjablonTallNavn.TRYGDEAVGIFT_PROSENT.navn }.count()
                )
                    .isEqualTo(TestUtil.dummySjablonSjablontallListe()
                        .filter { (typeSjablon): Sjablontall -> typeSjablon == "0017" }.count { (_, datoFom, datoTom): Sjablontall ->
                            !datoFom!!.isAfter(LocalDate.parse("2020-09-01")) && !datoTom
                                ?.isBefore(LocalDate.parse("2020-08-01"))!!
                        }
                    )
            },  // Sjekk at det mappes ut riktig antall sjabloner av type Bidragsevne
            Executable {
                assertThat(sjablonPeriodeListe.stream()
                    .filter { (_, navn): SjablonPeriodeCore -> navn == SjablonNavn.BIDRAGSEVNE.navn }.count()
                )
                    .isEqualTo(TestUtil.dummySjablonBidragsevneListe().stream()
                        .filter { (_, datoFom, datoTom): Bidragsevne ->
                            !datoFom!!.isAfter(LocalDate.parse("2020-09-01")) && !datoTom
                                ?.isBefore(LocalDate.parse("2020-08-01"))!!
                        }.count()
                    )
            },  // Sjekk at det mappes ut riktig verdi for en gitt sjablon av type Sjablontall
            Executable {
                assertThat(sjablonPeriodeListe.stream()
                    .filter { (periode, navn): SjablonPeriodeCore ->
                        navn == SjablonTallNavn.TRYGDEAVGIFT_PROSENT.navn && periode.datoFom == LocalDate.parse(
                            "2014-01-01"
                        )
                    }
                    .flatMap { it.innholdListe.stream() }
                    .findFirst()
                    .map{ it.verdi }
                    .orElse(BigDecimal.ZERO))
                    .isEqualTo(BigDecimal.valueOf(8.2))
            })
    }

    @Test
    @DisplayName("Skal beregne særtilskudd")
    fun skalBeregneSaertilskudd() {
        val bidragsevneGrunnlagTilCoreCaptor = ArgumentCaptor.forClass(
            BeregnBidragsevneGrunnlagCore::class.java
        )
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore())
        `when`(bpAndelSaertilskuddCoreMock!!.beregnBPsAndelSaertilskudd(any())).thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore())
        `when`(samvaersfradragCoreMock!!.beregnSamvaersfradrag(any())).thenReturn(TestUtil.dummySamvaersfradragResultatCore())
        `when`(saertilskuddCoreMock!!.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCore())

        val beregnSaertilskuddResultat = beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag())
        assertAll(
            { assertEquals(HttpStatus.OK, beregnSaertilskuddResultat.responseEntity.statusCode) },
            { assertNotNull(beregnSaertilskuddResultat.responseEntity.body) },
            { assertNotNull(beregnSaertilskuddResultat.responseEntity.body?.beregnetSaertilskuddPeriodeListe) },
            { assertEquals(1, beregnSaertilskuddResultat.responseEntity.body?.beregnetSaertilskuddPeriodeListe?.size) }
        )
    }

    @Test
    @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BidragsevneCore")
    fun skalKasteUgyldigInputExceptionVedFeilReturFraBidragsevneCore() {
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCoreMedAvvik())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java)
            .isThrownBy { beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()) }
            .withMessageContaining("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet:")
            .withMessageContaining("beregnDatoFra kan ikke være null")
            .withMessageContaining("periodeDatoTil må være etter periodeDatoFra")
    }

    @Test
    @DisplayName("Skal kaste UgyldigInputException ved feil retur fra BPsAndelSaertilskuddCore")
    fun skalKasteUgyldigInputExceptionVedFeilReturFraBPsAndelSaertilskuddCore() {
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore())
        `when`(bpAndelSaertilskuddCoreMock!!.beregnBPsAndelSaertilskudd(any()))
            .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCoreMedAvvik())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java)
            .isThrownBy { beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()) }
            .withMessageContaining("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet:")
            .withMessageContaining("beregnDatoFra kan ikke være null")
            .withMessageContaining("periodeDatoTil må være etter periodeDatoFra")
    }

    @Test
    @DisplayName("Skal kaste UgyldigInputException ved feil retur fra SamvaersfradragCore")
    fun skalKasteUgyldigInputExceptionVedFeilReturFraSamvaersfradragCore() {
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore())
        `when`(bpAndelSaertilskuddCoreMock!!.beregnBPsAndelSaertilskudd(ArgumentMatchers.any()))
            .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore())
        `when`(samvaersfradragCoreMock!!.beregnSamvaersfradrag(ArgumentMatchers.any()))
            .thenReturn(TestUtil.dummySamvaersfradragResultatCoreMedAvvik())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java)
            .isThrownBy { beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()) }
            .withMessageContaining("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet:")
            .withMessageContaining("beregnDatoFra kan ikke være null")
            .withMessageContaining("periodeDatoTil må være etter periodeDatoFra")
    }

    @Test
    @DisplayName("Skal kaste UgyldigInputException ved feil retur fra SaertilskuddCore")
    fun skalKasteUgyldigInputExceptionVedFeilReturFraSaertilskuddCore() {
        `when`(bidragsevneCoreMock!!.beregnBidragsevne(any())).thenReturn(TestUtil.dummyBidragsevneResultatCore())
        `when`(bpAndelSaertilskuddCoreMock!!.beregnBPsAndelSaertilskudd(any()))
            .thenReturn(TestUtil.dummyBPsAndelSaertilskuddResultatCore())
        `when`(samvaersfradragCoreMock!!.beregnSamvaersfradrag(any()))
            .thenReturn(TestUtil.dummySamvaersfradragResultatCore())
        `when`(saertilskuddCoreMock!!.beregnSaertilskudd(any())).thenReturn(TestUtil.dummySaertilskuddResultatCoreMedAvvik())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java)
            .isThrownBy { beregnSaertilskuddService!!.beregn(TestUtil.byggTotalSaertilskuddGrunnlag()) }
            .withMessageContaining("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet:")
            .withMessageContaining("beregnDatoFra kan ikke være null")
            .withMessageContaining("periodeDatoTil må være etter periodeDatoFra")
    }
}
