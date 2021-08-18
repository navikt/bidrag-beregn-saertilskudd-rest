package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

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
