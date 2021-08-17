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
import org.apache.tomcat.jni.Local
import java.math.BigDecimal
import java.time.LocalDate

interface IPeriode {
    val datoFom: LocalDate;
    val datoTil: LocalDate;
}

//open class BasePeriode {
//    val datoFom: LocalDate;
//    val datoTil: LocalDate;
//
//    constructor(datoFom: LocalDate, datoTil: LocalDate) {
//        this.datoFom = datoFom;
//        this.datoTil = datoTil;
//    }
//
//    fun TilCore() = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null");
//}
//
//open class InntektBase : BasePeriode {
//    val inntektType: String;
//    val belop: Int;
//
//    constructor(datoFom: LocalDate, datoTil: LocalDate, inntektType: String, belop: Int) : super(datoFom, datoTil) {
//        this.inntektType = inntektType;
//        this.belop = belop;
//    }
//}
//
//class BPInntekt2 : InntektBase {
//    constructor(datoFom: LocalDate, datoTil: LocalDate, inntektType: String, belop: Int) : super(datoFom, datoTil, inntektType, belop)
//
//    fun TilCore(referanse: String) = InntektPeriodeCore(
//            referanse,
//            periodeDatoFraTil = ((BasePeriode) this).TilCore(),
//    )
//}

interface IInntekt {
    val rolle: Rolle;
    val inntektType: String;
    val belop: Int;
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

interface IRolle {
    val rolle: Rolle;
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class InntektRolle(
    override val rolle: Rolle
) : IRolle {
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

data class SBInntekt(
        override val rolle: Rolle,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        override val inntektType: String,
        override val belop: Int,
        val soknadsBarnId: Int,
) : IPeriode, IInntekt

data class BMInntekt(
        override val rolle: Rolle,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        override val inntektType: String,
        override val belop: Int,
        val deltFordel: Boolean,
        val skatteklasse2: Boolean,
) : IPeriode, IInntekt {
    fun TilCore(referanse: String) = no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            inntektType = if (inntektType != null) inntektType else throw UgyldigInputException("inntektType kan ikke være null"),
            inntektBelop = if (belop != null) BigDecimal(belop) else throw UgyldigInputException("belop kan ikke være null"),
            deltFordel = if (deltFordel != null) deltFordel else throw UgyldigInputException("DeltFordel kan ikke være null"),
            skatteklasse2 = if (skatteklasse2 != null) skatteklasse2 else throw UgyldigInputException("Skatteklasse2 kan ikke være null")
    )
}

data class BPInntekt(
        override val rolle: Rolle,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        override val inntektType: String,
        override val belop: Int,
) : IPeriode, IInntekt {
    fun TilCore(referanse: String) = InntektPeriodeCore (
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            inntektType = if (inntektType != null) inntektType else throw UgyldigInputException("inntektType kan ikke være null"),
            inntektBelop = if (belop != null) BigDecimal(belop) else throw UgyldigInputException("belop kan ikke være null")
            )
    fun TilBPsAndelSaertilskuddCore(referanse: String) = no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            inntektType = if (inntektType != null) inntektType else throw UgyldigInputException("inntektType kan ikke være null"),
            inntektBelop = if (belop != null) BigDecimal(belop) else throw UgyldigInputException("belop kan ikke være null"),
            deltFordel = false,
            skatteklasse2 = false
    )
}

data class BarnIHusstand(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val antall: Int,
) : IPeriode {
    fun TilCore(referanse: String) = AntallBarnIEgetHusholdPeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            antallBarn = if (antall != null) BigDecimal(antall) else throw UgyldigInputException("Antall barn i husstand kan ikke være null"),
    )
}

data class Bostatus(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val bostatusKode: String,
) : IPeriode {
    fun TilCore(referanse: String) = BostatusPeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            bostatusKode = if (bostatusKode != null) bostatusKode else throw UgyldigInputException("BostatusKode kan ikke være null")
            )
}

data class Saerfradrag(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val saerfradragKode: String,
) : IPeriode {
    fun TilCore(referanse: String) = SaerfradragPeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            saerfradragKode = if (saerfradragKode != null) saerfradragKode else throw UgyldigInputException("Saerfradragskode kan ikke være null")
            )
}

data class Skatteklasse(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val skatteklasseId: Int,
) : IPeriode {
    fun TilCore(referanse: String) = SkatteklassePeriodeCore(
            referanse,
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            skatteklasse = if (skatteklasseId != null) skatteklasseId else throw UgyldigInputException("SkatteklasseId kan ikke være null")
    )
}

data class NettoSaertilskudd(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val nettoSaertilskuddBelop: Int,
) : IPeriode {
    fun TilCore(referanse: String) = NettoSaertilskuddPeriodeCore(
            periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            nettoSaertilskuddBelop = if (nettoSaertilskuddBelop != null) BigDecimal(nettoSaertilskuddBelop) else throw UgyldigInputException("nettoSaertilskuddBelop kan ikke være null")
    )
}

data class Samvaersklasse(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val soknadsbarnId: Int,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val soknadsbarnFodselsdato: LocalDate,
        val samvaersklasseId: String,
) : IPeriode {
    fun TilCore(referanse: String) = SamvaersklassePeriodeCore(
            samvaersklassePeriodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
            barnPersonId = if (soknadsbarnId != null) soknadsbarnId else throw UgyldigInputException("SoknadsbarnId kan ikke være null i samvaersklasse"),
            barnFodselsdato = if (soknadsbarnFodselsdato != null) soknadsbarnFodselsdato else throw UgyldigInputException("BarnFodselsDato kan ikke være null i Samvaersklasse"),
            samvaersklasse = if (samvaersklasseId != null) samvaersklasseId else throw UgyldigInputException("SamvaersklasseId kan ikke være null i Samvaersklasse")
    )
}

data class LopendeBidrag(
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoFom: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        override val datoTil: LocalDate,
        val soknadsbarnId: Int,
        val belop: Int,
        val opprinneligBPAndelUnderholdskostnadBelop: Int,
        val opprinneligBidragBelop: Int,
        val opprinneligSamvaersfradragBelop: Int
        ) : IPeriode {
            fun TilCore(referanse: String) = LopendeBidragPeriodeCore(
                    referanse,
                    periodeDatoFraTil = if (datoFom != null && datoTil != null) PeriodeCore(datoFom, datoTil) else throw UgyldigInputException("datoFom og datoTil kan ikke være null"),
                    barnPersonId = if (soknadsbarnId != null) soknadsbarnId else throw UgyldigInputException("SoknadsbarnId kan ikke være null"),
                    lopendeBidragBelop = if (belop != null) BigDecimal(belop) else throw UgyldigInputException("Belop kan ikke være null"),
                    opprinneligBPsAndelUnderholdskostnadBelop = if (opprinneligBPAndelUnderholdskostnadBelop != null) BigDecimal(opprinneligBPAndelUnderholdskostnadBelop) else throw UgyldigInputException("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null"),
                    opprinneligBidragBelop = if (opprinneligBidragBelop != null) BigDecimal(opprinneligBidragBelop) else throw UgyldigInputException("opprinneligBidragBelop kan ikke være null"),
                    opprinneligSamvaersfradragBelop = if (opprinneligSamvaersfradragBelop != null) BigDecimal(opprinneligSamvaersfradragBelop) else throw UgyldigInputException("opprinneligSamvaersfradragBelop kan ikke være null")
                    )
        }