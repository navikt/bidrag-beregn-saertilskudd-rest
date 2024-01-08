package no.nav.bidrag.beregn.saertilskudd.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class Sjablontall(
    val typeSjablon: String? = null,
    val datoFom: LocalDate? = null,
    val datoTom: LocalDate? = null,
    val verdi: BigDecimal? = null,
)
