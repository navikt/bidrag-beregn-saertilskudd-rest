package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.felles.dto.PeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Felles
@Schema(description = "Periode (fra-til dato)")
data class Periode(
        @Schema(description = "Fra-dato") var datoFom: LocalDate? = null,
        @Schema(description = "Til-dato") var datoTil: LocalDate? = null
) {

    constructor(periode: PeriodeCore) : this(
        datoFom = periode.periodeDatoFra,
        datoTil = periode.periodeDatoTil
    )

    fun tilCore(dataElement: String) = PeriodeCore(
        periodeDatoFra = datoFom ?: throw UgyldigInputException(dataElement + "DatoFra kan ikke være null"),
        periodeDatoTil = datoTil ?: throw UgyldigInputException(dataElement + "DatoTil kan ikke være null")
    )

    fun valider(dataElement: String) {
        if (datoFom == null) throw UgyldigInputException(dataElement + "DatoFra kan ikke være null")
        if (datoTil == null) throw UgyldigInputException(dataElement + "DatoTil kan ikke være null")
    }
}
