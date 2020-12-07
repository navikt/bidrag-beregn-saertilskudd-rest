package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.felles.dto.SjablonNavnVerdiCore
import java.math.BigDecimal

@ApiModel(value = "Sjabloner brukt i beregning")
data class Sjablon(
    @ApiModelProperty(value = "Sjablonnavn") var sjablonNavn: String,
    @ApiModelProperty(value = "Sjablonverdi") var sjablonVerdi: BigDecimal = BigDecimal.ZERO
) {

  constructor(sjablon: SjablonNavnVerdiCore) : this(
      sjablonNavn = sjablon.sjablonNavn,
      sjablonVerdi = sjablon.sjablonVerdi
  )
}