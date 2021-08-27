package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
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

@Schema(description = "Totalgrunnlaget for en særtilskuddsberegning")
data class BeregnTotalSaertilskuddGrunnlag(
    @Schema(description = "Beregn fra-dato") val beregnDatoFra: LocalDate? = null,
    @Schema(description = "Beregn til-dato") val beregnDatoTil: LocalDate? = null,
    @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<Grunnlag>? = null,
) {

  fun valider() {
    if (beregnDatoFra == null) throw UgyldigInputException("beregnDatoFra kan ikke være null")
    if (beregnDatoTil == null) throw UgyldigInputException("beregnDatoTil kan ikke være null")
    if (grunnlagListe != null) grunnlagListe.map { it.valider() } else throw UgyldigInputException("grunnlagListe kan ikke være null")
  }
}

@Schema(description = "Grunnlag")
data class Grunnlag(
    @Schema(description = "Referanse") val referanse: String? = null,
    @Schema(description = "Type") val type: GrunnlagType?,
    @Schema(description = "Innhold") val innhold: JsonNode? = null
) {
  fun valider() {
    if (referanse == null) throw UgyldigInputException("referanse kan ikke være null")
    if (type == null) throw UgyldigInputException("type kan ikke være null")
    if (innhold == null) throw UgyldigInputException("innhold kan ikke være null")
  }
}

open class BasePeriode {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer::class)
  @JsonSerialize(using = LocalDateSerializer::class)
  val datoFom: LocalDate?;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer::class)
  @JsonSerialize(using = LocalDateSerializer::class)
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
    return PeriodeCore(datoFom!!, datoTil!!)
  }
}

open class InntektBase(datoFom: LocalDate, datoTil: LocalDate, val rolle: Rolle, val inntektType: String?, val belop: BigDecimal?) : BasePeriode(datoFom, datoTil) {

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
        belop!!,
    )
  }

  fun tilInntektPeriodeCoreBPsAndelSaertilskudd(referanse: String): no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore {
    validerInntekt()
    return no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
        referanse,
        tilPeriodeCore(),
        inntektType!!,
        belop!!,
        deltFordel = false,
        skatteklasse2 = false
    )
  }
}

class BPInntekt(datoFom: LocalDate, datoTil: LocalDate, rolle: Rolle, inntektType: String?, belop: BigDecimal?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

  fun tilCore(referanse: String): InntektPeriodeCore = tilInntektPeriodeCore(referanse)

  fun tilBPsAndelSaertilskuddCore(referanse: String) = tilInntektPeriodeCoreBPsAndelSaertilskudd(referanse)
}

class BMInntekt(datoFom: LocalDate, datoTil: LocalDate, inntektType: String?, belop: BigDecimal?, rolle: Rolle, val deltFordel: Boolean?, val skatteklasse2: Boolean?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

  fun valider() {
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
        belop!!,
        deltFordel!!,
        skatteklasse2!!
    )
  }
}

class SBInntekt(datoFom: LocalDate, datoTil: LocalDate, rolle: Rolle, inntektType: String?, belop: BigDecimal?, val soknadsbarnId: Int?) : InntektBase(datoFom, datoTil, rolle, inntektType, belop) {

  fun valider() {
    validerInntekt()
    if (soknadsbarnId == null) throw UgyldigInputException("soknadsbarnId kan ikke være null")
  }

  fun tilCore(referanse: String): no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore {
    valider()
    return tilInntektPeriodeCoreBPsAndelSaertilskudd(referanse);
  }
}

class BarnIHusstand(datoFom: LocalDate, datoTil: LocalDate, val antall: Double?) : BasePeriode(datoFom, datoTil) {

  fun valider() {
    if (antall == null) throw UgyldigInputException("antall kan ikke være null")
  }

  fun tilCore(referanse: String): AntallBarnIEgetHusholdPeriodeCore {
    valider()
    return AntallBarnIEgetHusholdPeriodeCore(
        referanse,
        tilPeriodeCore(),
        antall!!
    )
  }
}

class Bostatus(datoFom: LocalDate, datoTil: LocalDate, val bostatusKode: String?) : BasePeriode(datoFom, datoTil) {

  fun valider() {
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

  fun valider() {
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

  fun valider() {
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

class NettoSaertilskudd(datoFom: LocalDate, datoTil: LocalDate, val nettoSaertilskuddBelop: BigDecimal?) : BasePeriode(datoFom, datoTil) {

  fun valider() {
    if (nettoSaertilskuddBelop == null) throw UgyldigInputException("nettoSaertilskuddBelop kan ikke være null")
  }

  fun tilCore(referanse: String): NettoSaertilskuddPeriodeCore {
    valider()
    return NettoSaertilskuddPeriodeCore(
        referanse,
        tilPeriodeCore(),
        nettoSaertilskuddBelop!!
    )
  }
}

class Samvaersklasse(datoFom: LocalDate, datoTil: LocalDate, val soknadsbarnId: Int?, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
@JsonDeserialize(using = LocalDateDeserializer::class) @JsonSerialize(using = LocalDateSerializer::class)
val soknadsbarnFodselsdato: LocalDate?, val samvaersklasseId: String?) : BasePeriode(datoFom, datoTil) {

  fun valider() {
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

class LopendeBidrag(datoFom: LocalDate, datoTil: LocalDate, val soknadsbarnId: Int?, val belop: BigDecimal?, val opprinneligBPAndelUnderholdskostnadBelop: BigDecimal?, val opprinneligBidragBelop: BigDecimal?, val opprinneligSamvaersfradragBelop: BigDecimal?) : BasePeriode(datoFom, datoTil) {

  fun valider() {
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
        belop!!,
        opprinneligBPAndelUnderholdskostnadBelop!!,
        opprinneligBidragBelop!!,
        opprinneligSamvaersfradragBelop!!
    )
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class InntektRolle(
    val rolle: Rolle
)

data class SoknadsBarnInfo(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val fodselsdato: LocalDate
)