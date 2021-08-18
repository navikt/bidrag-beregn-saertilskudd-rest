package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import no.nav.bidrag.beregn.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.BostatusPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.InntektPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SaerfradragPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SkatteklassePeriodeCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.NettoSaertilskuddPeriodeCore
import no.nav.bidrag.beregn.felles.dto.PeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore
import java.math.BigDecimal
import java.time.LocalDate

open class BasePeriode {
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val datoFom: LocalDate?;

    @JsonDeserialize(using = LocalDateDeserializer::class)
    val datoTil: LocalDate?;

    constructor(datoFom: LocalDate, datoTil: LocalDate) {
        this.datoFom = datoFom;
        this.datoTil = datoTil;
    }

    private fun valider() {
        if (datoFom == null) throw UgyldigInputException("datoFom kan ikke være null")
        if (datoTil == null) throw UgyldigInputException("datoTil kan ikke være null")
    }

    fun tilPeriodeCore(): PeriodeCore {
        valider()
        return PeriodeCore(datoFom!!, datoTil)
    }
}

open class InntektBase(datoFom: LocalDate, datoTil: LocalDate, val rolle: Rolle, val inntektType: String?, val belop: Int?) : BasePeriode(datoFom, datoTil) {

    fun validerInntekt() {
        if (inntektType == null) throw UgyldigInputException("inntektType kan ikke være null")
        if (belop == null) throw UgyldigInputException("belop kan ikke være null")
    }

    fun tilInntektPeriodeCore(referanse: String): InntektPeriodeCore {
        validerInntekt()
        return InntektPeriodeCore(
                referanse,
                tilPeriodeCore(),
                inntektType!!,
                BigDecimal(belop!!),
        )
    }
}

class BPInntekt(datoFom: LocalDate, datoTil: LocalDate, rolle: Rolle, inntektType: String?, belop: Int?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

    fun tilCore(referanse: String): InntektPeriodeCore = tilInntektPeriodeCore(referanse)

    fun tilBPsAndelSaertilskuddCore(referanse: String): no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore {
        validerInntekt()
        return no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
                referanse,
                tilPeriodeCore(),
                inntektType!!,
                BigDecimal(belop!!),
                false,
                false
        )
    }
}

class BMInntekt(datoFom: LocalDate, datoTil: LocalDate, inntektType: String?, belop: Int?, rolle: Rolle, val deltFordel: Boolean?, val skatteklasse2: Boolean?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

    private fun valider() {
        validerInntekt()
        if (deltFordel == null) throw UgyldigInputException("deltFordel kan ikke være null")
        if (skatteklasse2 == null) throw UgyldigInputException("skatteklasse2 kan ikke være null")
    }

    fun tilCore(referanse: String): no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore {
        valider()
        return no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
                referanse,
                tilPeriodeCore(),
                inntektType!!,
                BigDecimal(belop!!),
                deltFordel!!,
                skatteklasse2!!
        )
    }
}

class SBInntekt(datoFom: LocalDate, datoTil: LocalDate, rolle: Rolle, inntektType: String?, belop: Int?, val soknadsbarnId: Int?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

    fun validerSBInntekt() {
        validerInntekt()
        if (soknadsbarnId == null) throw UgyldigInputException("deltFordel kan ikke være null")
    }
}

class BarnIHusstand(datoFom: LocalDate, datoTil: LocalDate, val antall: Int?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (antall == null) throw UgyldigInputException("antall kan ikke være null")
    }

    fun tilCore(referanse: String): AntallBarnIEgetHusholdPeriodeCore {
        valider()
        return AntallBarnIEgetHusholdPeriodeCore(
                referanse,
                tilPeriodeCore(),
                BigDecimal(antall!!)
        )
    }
}

class Bostatus(datoFom: LocalDate, datoTil: LocalDate, val bostatusKode: String?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (bostatusKode == null) throw UgyldigInputException("bostatusKode kan ikke være null")
    }
    fun tilCore(referanse: String): BostatusPeriodeCore {
        valider()
        return BostatusPeriodeCore(
                referanse,
                tilPeriodeCore(),
                bostatusKode!!
        )
    }
}

class Saerfradrag(datoFom: LocalDate, datoTil: LocalDate, val saerfradragKode: String?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (saerfradragKode == null) throw UgyldigInputException("saerfradragKode kan ikke være null")
    }

    fun tilCore(referanse: String): SaerfradragPeriodeCore {
        valider()
        return SaerfradragPeriodeCore(
                referanse,
                tilPeriodeCore(),
                saerfradragKode!!
        )
    }
}

class Skatteklasse(datoFom: LocalDate, datoTil: LocalDate, val skatteklasseId: Int?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (skatteklasseId == null) throw UgyldigInputException("skatteklasseId kan ikke være null")
    }

    fun tilCore(referanse: String): SkatteklassePeriodeCore {
        valider()
        return SkatteklassePeriodeCore(
                referanse,
                tilPeriodeCore(),
                skatteklasseId!!
        )
    }
}

class NettoSaertilskudd(datoFom: LocalDate, datoTil: LocalDate, val nettoSaertilskuddBelop: Int?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (nettoSaertilskuddBelop == null) throw UgyldigInputException("nettoSaertilskuddBelop kan ikke være null")
    }

    fun tilCore(referanse: String): NettoSaertilskuddPeriodeCore {
        valider()
        return NettoSaertilskuddPeriodeCore(
                referanse,
                tilPeriodeCore(),
                BigDecimal(nettoSaertilskuddBelop!!)
        )
    }
}

class Samvaersklasse(datoFom: LocalDate, datoTil: LocalDate, val soknadsbarnId: Int?, @JsonDeserialize(using = LocalDateDeserializer::class) val soknadsbarnFodselsdato: LocalDate?, val samvaersklasseId: String?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (soknadsbarnId == null) throw UgyldigInputException("soknadsbarnId kan ikke være null")
        if (soknadsbarnFodselsdato == null) throw UgyldigInputException("soknadsbarnFodselsdato kan ikke være null")
        if (samvaersklasseId == null) throw UgyldigInputException("samvaersklasseId kan ikke være null")
    }

    fun tilCore(referanse: String): SamvaersklassePeriodeCore {
        valider()
        return SamvaersklassePeriodeCore(
                referanse,
                tilPeriodeCore(),
                soknadsbarnId!!,
                soknadsbarnFodselsdato!!,
                samvaersklasseId!!
        )
    }
}

class LopendeBidrag(datoFom: LocalDate, datoTil: LocalDate, val soknadsbarnId: Int?, val belop: Int?, val opprinneligBPAndelUnderholdskostnadBelop: Int?, val opprinneligBidragBelop: Int?, val opprinneligSamvaersfradragBelop: Int?) : BasePeriode(datoFom, datoTil) {

    private fun valider() {
        if (soknadsbarnId == null) throw UgyldigInputException("soknadsbarnId kan ikke være null")
        if (belop == null) throw UgyldigInputException("belop kan ikke være null")
        if (opprinneligBPAndelUnderholdskostnadBelop == null) throw UgyldigInputException("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null")
        if (opprinneligBidragBelop == null) throw UgyldigInputException("opprinneligBidragBelop kan ikke være null")
        if (opprinneligSamvaersfradragBelop == null) throw UgyldigInputException("opprinneligSamvaersfradragBelop kan ikke være null")
    }

    fun tilCore(referanse: String): LopendeBidragPeriodeCore {
        valider()
        return LopendeBidragPeriodeCore(
                referanse,
                tilPeriodeCore(),
                soknadsbarnId!!,
                BigDecimal(belop!!),
                BigDecimal(opprinneligBPAndelUnderholdskostnadBelop!!),
                BigDecimal(opprinneligBidragBelop!!),
                BigDecimal(opprinneligSamvaersfradragBelop!!)
        )
    }
}

enum class Rolle(@get:JsonValue val value: String) {
    BP("BP"),
    BM("BM"),
    SB("SB"),
    BB("BB");

    companion object {

        @JsonCreator
        fun fromString(value: String): Rolle? {
            for (rolle in Rolle.values()) {
                if (rolle.name.equals(value, true)) {
                    return rolle;
                }
            }
            return null
        }
    }
}

enum class GrunnlagType(@get:JsonValue val value: String) {
    SAERFRADRAG("Saerfradrag"),
    SOKNADSBARN_INFO("SoknadsbarnInfo"),
    SKATTEKLASSE("Skatteklasse"),
    BARN_I_HUSSTAND("BarnIHusstand"),
    BOSTATUS("Bostatus"),
    INNTEKT("Inntekt"),
    NETTO_SAERTILSKUDD("NettoSaertilskudd"),
    SAMVAERSKLASSE("Samvaersklasse"),
    LOPENDE_BIDRAG("LopendeBidrag");

    companion object {

        @JsonCreator
        fun fromString(value: String): GrunnlagType? {
            for (grunnlagType in GrunnlagType.values()) {
                if (grunnlagType.name.equals(value, true)) {
                    return grunnlagType;
                }
            }
            return null
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class InntektRolle(
        val rolle: Rolle
) {
    fun valider() {
        if (rolle == null) {
            throw UgyldigInputException("Rolle kan ikke være null for inntektsgrunnlag")
        }
    }
}

data class SoknadsBarnInfo(
        val id: Int,
        val fodselsdato: LocalDate
)
