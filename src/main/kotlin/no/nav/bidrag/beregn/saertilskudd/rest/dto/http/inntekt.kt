package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.math.BigDecimal

// Grunnlag
@Schema(description = "Grunnlagsdata for bidragspliktiges og bidragsmottakers inntekt")
data class InntektBPBMGrunnlag(
        @Schema(description = "Periodisert liste over bidragspliktiges inntekt") val inntektBPPeriodeListe: List<InntektPeriode>? = null,
        @Schema(description = "Periodisert liste over bidragsmottakers inntekt") val inntektBMPeriodeListe: List<InntektBMPeriode>? = null
)

@Schema(description = "Inntekt periode")
data class InntektPeriode(
        @Schema(description = "Inntekt fra-til dato") var inntektDatoFraTil: Periode? = null,
        @Schema(description = "Inntekt type") var inntektType: String? = null,
        @Schema(description = "Inntekt beløp") var inntektBelop: BigDecimal? = null
) {

    fun tilCoreBidragsevne() = no.nav.bidrag.beregn.bidragsevne.dto.InntektPeriodeCore(
            referanse = "",
            periodeDatoFraTil = if (inntektDatoFraTil != null) inntektDatoFraTil!!.tilCore(
                    "BP inntekt"
            ) else throw UgyldigInputException("BP inntektDatoFraTil kan ikke være null"),
            inntektType = if (inntektType != null) inntektType!! else throw UgyldigInputException("BP inntektType kan ikke være null"),
            inntektBelop = if (inntektBelop != null) inntektBelop!! else throw UgyldigInputException("BP inntektBelop kan ikke være null")
    )

    fun tilCoreBPAndelSaertilskudd(dataElement: String) = no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
            referanse = "",
            periodeDatoFraTil = if (inntektDatoFraTil != null) inntektDatoFraTil!!.tilCore(
                    "$dataElement inntekt"
            ) else throw UgyldigInputException("$dataElement inntektDatoFraTil kan ikke være null"),
            inntektType = if (inntektType != null) inntektType!! else throw UgyldigInputException("$dataElement inntektType kan ikke være null"),
            inntektBelop = if (inntektBelop != null) inntektBelop!! else throw UgyldigInputException("$dataElement inntektBelop kan ikke være null"),
            deltFordel = false,
            skatteklasse2 = false
    )

    fun validerInntekt(dataElement: String) {
        if (inntektDatoFraTil != null) inntektDatoFraTil!!.valider("$dataElement inntekt") else throw UgyldigInputException(
                "$dataElement inntektDatoFraTil kan ikke være null"
        )
        if (inntektType == null) throw UgyldigInputException("$dataElement inntektType kan ikke være null")
        if (inntektBelop == null) throw UgyldigInputException("$dataElement inntektBelop kan ikke være null")
    }
}

@Schema(description = "Inntekt bidragsmottaker periode")
data class InntektBMPeriode(
        @Schema(description = "Inntekt fra-til dato") var inntektDatoFraTil: Periode? = null,
        @Schema(description = "Inntekt type") var inntektType: String? = null,
        @Schema(description = "Inntekt beløp") var inntektBelop: BigDecimal? = null,
        @Schema(description = "Delt fordel (utvidet barnetrygd)") var deltFordel: Boolean? = null,
        @Schema(description = "Skatteklasse 2") var skatteklasse2: Boolean? = null
) {

    fun tilCore() = no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore(
            referanse = "",
            periodeDatoFraTil = if (inntektDatoFraTil != null) inntektDatoFraTil!!.tilCore("BM inntekt") else throw UgyldigInputException(
                    "BM inntektDatoFraTil kan ikke være null"
            ),
            inntektType = if (inntektType != null) inntektType!! else throw UgyldigInputException("BM inntektType kan ikke være null"),
            inntektBelop = if (inntektBelop != null) inntektBelop!! else throw UgyldigInputException("BM inntektBelop kan ikke være null"),
            deltFordel = if (deltFordel != null) deltFordel!! else throw UgyldigInputException("BM deltFordel kan ikke være null"),
            skatteklasse2 = if (skatteklasse2 != null) skatteklasse2!! else throw UgyldigInputException("BM skatteklasse2 kan ikke være null")
    )

    fun validerInntekt() {
        if (inntektDatoFraTil != null) inntektDatoFraTil!!.valider("BM inntekt") else throw UgyldigInputException(
                "BM inntektDatoFraTil kan ikke være null"
        )
        if (inntektType == null) throw UgyldigInputException("BM inntektType kan ikke være null")
        if (inntektBelop == null) throw UgyldigInputException("BM inntektBelop kan ikke være null")
        if (deltFordel == null) throw UgyldigInputException("BM deltFordel kan ikke være null")
        if (skatteklasse2 == null) throw UgyldigInputException("BM skatteklasse2 kan ikke være null")
    }
}


// Resultat
@Schema(description = "Inntekttype og -beløp")
data class Inntekt(
        @Schema(description = "Inntekt type") var inntektType: String = "",
        @Schema(description = "Inntekt beløp") var inntektBelop: BigDecimal = BigDecimal.ZERO
) {

    constructor(inntekt: no.nav.bidrag.beregn.bidragsevne.dto.InntektCore) : this(
            inntektType = inntekt.inntektType,
            inntektBelop = inntekt.inntektBelop
    )

    constructor(inntekt: no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektCore) : this(
            inntektType = inntekt.inntektType,
            inntektBelop = inntekt.inntektBelop
    )
}

@Schema(description = "Inntekt bidragsmottaker")
data class InntektBM(
        @Schema(description = "Inntekt type") var inntektType: String = "",
        @Schema(description = "Inntekt beløp") var inntektBelop: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Delt fordel (utvidet barnetrygd)") var deltFordel: Boolean = false,
        @Schema(description = "Skatteklasse 2") var skatteklasse2: Boolean = false
) {

    constructor(inntekt: no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektCore) : this(
            inntektType = inntekt.inntektType,
            inntektBelop = inntekt.inntektBelop,
            deltFordel = inntekt.deltFordel,
            skatteklasse2 = inntekt.skatteklasse2
    )
}
