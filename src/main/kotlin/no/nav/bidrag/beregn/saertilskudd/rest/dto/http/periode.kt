package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.felles.dto.PeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Felles
@ApiModel(value = "Periode (fra-til dato)")
data class Periode(
    @ApiModelProperty(value = "Fra-dato") var periodeDatoFra: LocalDate? = null,
    @ApiModelProperty(value = "Til-dato") var periodeDatoTil: LocalDate? = null
) {

  constructor(periode: PeriodeCore) : this(
      periodeDatoFra = periode.periodeDatoFra,
      periodeDatoTil = periode.periodeDatoTil
  )

  fun tilCore(dataElement: String) = PeriodeCore(
      periodeDatoFra = if (periodeDatoFra != null) periodeDatoFra!! else throw UgyldigInputException(
          dataElement + "DatoFra kan ikke være null"),
      periodeDatoTil = if (periodeDatoTil != null) periodeDatoTil!! else throw UgyldigInputException(
          dataElement + "DatoTil kan ikke være null")
  )

  fun valider(dataElement: String) {
    if (periodeDatoFra == null) throw UgyldigInputException(dataElement + "DatoFra kan ikke være null")
    if (periodeDatoTil == null) throw UgyldigInputException(dataElement + "DatoTil kan ikke være null")
  }
}
