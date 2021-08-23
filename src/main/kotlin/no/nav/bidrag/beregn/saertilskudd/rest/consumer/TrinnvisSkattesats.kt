package no.nav.bidrag.beregn.saertilskudd.rest.consumer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class TrinnvisSkattesats(
    val datoFom: LocalDate? = null,
    val datoTom: LocalDate? = null,
    val inntektgrense: BigDecimal? = null,
    val sats: BigDecimal? = null
)
