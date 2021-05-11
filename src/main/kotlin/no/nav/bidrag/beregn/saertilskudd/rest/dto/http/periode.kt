package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.felles.dto.PeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Felles
@Schema(description = "Periode (fra-til dato)")
data class Periode(
    @Schema(description = "Fra-dato") var periodeDatoFra: LocalDate? = null,
    @Schema(description = "Til-dato") var periodeDatoTil: LocalDate? = null
) {

    constructor(periode: PeriodeCore) : this(
        periodeDatoFra = periode.periodeDatoFra,
        periodeDatoTil = periode.periodeDatoTil
    )

    fun tilCore(dataElement: String) = PeriodeCore(
        periodeDatoFra = periodeDatoFra ?: throw UgyldigInputException(dataElement + "DatoFra kan ikke være null"),
        periodeDatoTil = periodeDatoTil ?: throw UgyldigInputException(dataElement + "DatoTil kan ikke være null")
    )

    fun valider(dataElement: String) {
        if (periodeDatoFra == null) throw UgyldigInputException(dataElement + "DatoFra kan ikke være null")
        if (periodeDatoTil == null) throw UgyldigInputException(dataElement + "DatoTil kan ikke være null")
    }
}
