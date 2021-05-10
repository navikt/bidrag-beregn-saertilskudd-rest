package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.felles.dto.SjablonNavnVerdiCore
import java.math.BigDecimal

@Schema(description = "Sjabloner brukt i beregning")
data class Sjablon(
    @Schema(description = "Sjablonnavn") var sjablonNavn: String = "",
    @Schema(description = "Sjablonverdi") var sjablonVerdi: BigDecimal = BigDecimal.ZERO
) {

    constructor(sjablon: SjablonNavnVerdiCore) : this(
        sjablonNavn = sjablon.sjablonNavn,
        sjablonVerdi = sjablon.sjablonVerdi
    )
}