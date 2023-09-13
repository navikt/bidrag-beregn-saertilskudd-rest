package no.nav.bidrag.beregn.saertilskudd.rest.dto

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BarnIHusstand
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bostatus
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektBase
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.LopendeBidrag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Saerfradrag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Skatteklasse
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

@DisplayName("DtoTest")
internal class DtoTest {
    // BeregnTotalSaertilskuddGrunnlag
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når beregnDatoFra er null")
    fun skalKasteIllegalArgumentExceptionNaarBeregnDatoFraErNull() {
        val grunnlag = BeregnTotalSaertilskuddGrunnlag(null, LocalDate.parse("2021-08-18"), emptyList())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("beregnDatoFra kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når beregnDatoTil er null")
    fun skalKasteIllegalArgumentExceptionNaarBeregnDatoTilErNull() {
        val grunnlag = BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), null, emptyList())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("beregnDatoTil kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når grunnlagListe er null")
    fun skalKasteIllegalArgumentExceptionNaarGrunnlagListeErNull() {
        val grunnlag = BeregnTotalSaertilskuddGrunnlag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("grunnlagListe kan ikke være null")
    }

    // Grunnlag
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når referanse er null")
    fun skalKasteIllegalArgumentExceptionNaarGrunnlagReferanseErNull() {
        val grunnlag = Grunnlag(null, GrunnlagType.INNTEKT, jacksonObjectMapper().createObjectNode())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("referanse kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når type er null")
    fun skalKasteIllegalArgumentExceptionNaarGrunnlagTypeErNull() {
        val grunnlag = Grunnlag("TestReferanse", null, jacksonObjectMapper().createObjectNode())
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("type kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når innhold er null")
    fun skalKasteIllegalArgumentExceptionNaarGrunnlagInnholdErNull() {
        val grunnlag = Grunnlag("TestReferanse", GrunnlagType.INNTEKT, null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("innhold kan ikke være null")
    }

    // InntektBase
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når inntektType er null")
    fun skalKasteIllegalArgumentExceptionNaarInntektTypeErNull() {
        val grunnlag = InntektBase(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.BP, null, BigDecimal.valueOf(400000))
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.validerInntekt() }
            .withMessage("inntektType kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når belop er null")
    fun skalKasteIllegalArgumentExceptionNaarBelopErNull() {
        val grunnlag = InntektBase(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.BP, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER", null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.validerInntekt() }
            .withMessage("belop kan ikke være null")
    }

    //BMInntekt
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når deltFordel er null")
    fun skalKasteIllegalArgumentExceptionNaarDeltFordelErNull() {
        val grunnlag = BMInntekt(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER",
            BigDecimal.valueOf(400000), Rolle.BM, null, false
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("deltFordel kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når skatteklasse2 er null")
    fun skalKasteIllegalArgumentExceptionNaarSkatteklasse2ErNull() {
        val grunnlag = BMInntekt(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER",
            BigDecimal.valueOf(400000), Rolle.BM, false, null
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("skatteklasse2 kan ikke være null")
    }

    //SBInntekt
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
    fun skalKasteIllegalArgumentExceptionNaarSoknadsbarnErNull() {
        val grunnlag = SBInntekt(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), Rolle.SB, "INNTEKTSOPPLYSNINGER_ARBEIDSGIVER",
            BigDecimal.valueOf(400000), null
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("soknadsbarnId kan ikke være null")
    }

    //BarnIHusstand
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når antall er null")
    fun skalKasteIllegalArgumentExceptionNaarAntallErNull() {
        val grunnlag = BarnIHusstand(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("antall kan ikke være null")
    }

    //Bostatus
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når bostatusKode er null")
    fun skalKasteIllegalArgumentExceptionNaarBostatusKodeErNull() {
        val grunnlag = Bostatus(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("bostatusKode kan ikke være null")
    }

    //Saerfradrag
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når saerfradragKode er null")
    fun skalKasteIllegalArgumentExceptionNaarSaerfradragKodeErNull() {
        val grunnlag = Saerfradrag(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("saerfradragKode kan ikke være null")
    }

    //Skatteklasse
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når skatteklasseId er null")
    fun skalKasteIllegalArgumentExceptionNaarSkatteklasseIdErNull() {
        val grunnlag = Skatteklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("skatteklasseId kan ikke være null")
    }

    //NettoSaertilskudd
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når nettoSaertilskuddBelop er null")
    fun skalKasteIllegalArgumentExceptionNaarNettoSaertilskuddBelopErNull() {
        val grunnlag = NettoSaertilskudd(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("nettoSaertilskuddBelop kan ikke være null")
    }

    //Samvaersklasse
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
    fun skalKasteIllegalArgumentExceptionNaarSamvaersklasseSoknadsbarnIdErNull() {
        val grunnlag = Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null, LocalDate.parse("2008-08-18"), "01")
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("soknadsbarnId kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnFodselsdato er null")
    fun skalKasteIllegalArgumentExceptionNaarSamvaersklasseSoknadsbarnFodselsdatoErNull() {
        val grunnlag = Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, null, "01")
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("soknadsbarnFodselsdato kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når samvaersklasseId er null")
    fun skalKasteIllegalArgumentExceptionNaarSamvaersklasseSamvaersklasseIdErNull() {
        val grunnlag = Samvaersklasse(LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, LocalDate.parse("2008-08-18"), null)
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("samvaersklasseId kan ikke være null")
    }

    //LopendeBidrag
    @Test
    @DisplayName("Skal kaste IllegalArgumentException når soknadsbarnId er null")
    fun skalKasteIllegalArgumentExceptionNaarLopendeBidragSoknadsbarnIdErNull() {
        val grunnlag = LopendeBidrag(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), null, BigDecimal.valueOf(2000),
            BigDecimal.valueOf(1500), BigDecimal.valueOf(2000), BigDecimal.valueOf(1500)
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("soknadsbarnId kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når belop er null")
    fun skalKasteIllegalArgumentExceptionNaarLopendeBidragBelopErNull() {
        val grunnlag = LopendeBidrag(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, null, BigDecimal.valueOf(1500),
            BigDecimal.valueOf(2000), BigDecimal.valueOf(1500)
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("belop kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når opprinneligBPAndelUnderholdskostnadBelop er null")
    fun skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligBPAndelUnderholdskostnadBelopErNull() {
        val grunnlag = LopendeBidrag(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, BigDecimal.valueOf(2000), null,
            BigDecimal.valueOf(2000), BigDecimal.valueOf(1500)
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når opprinneligBidragBelop er null")
    fun skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligBidragBelopErNull() {
        val grunnlag = LopendeBidrag(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, BigDecimal.valueOf(2000),
            BigDecimal.valueOf(1500), null, BigDecimal.valueOf(1500)
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("opprinneligBidragBelop kan ikke være null")
    }

    @Test
    @DisplayName("Skal kaste IllegalArgumentException når opprinneligSamvaersfradragBelop er null")
    fun skalKasteIllegalArgumentExceptionNaarLopendeBidragOpprinneligSamvaersfradragBelopErNull() {
        val grunnlag = LopendeBidrag(
            LocalDate.parse("2021-08-18"), LocalDate.parse("2022-08-18"), 1, BigDecimal.valueOf(2000),
            BigDecimal.valueOf(1500), BigDecimal.valueOf(2000), null
        )
        Assertions.assertThatExceptionOfType(UgyldigInputException::class.java).isThrownBy { grunnlag.valider() }
            .withMessage("opprinneligSamvaersfradragBelop kan ikke være null")
    }
}
