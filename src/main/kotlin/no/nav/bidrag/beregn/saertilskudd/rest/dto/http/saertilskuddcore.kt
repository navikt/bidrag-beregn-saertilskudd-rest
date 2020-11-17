package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import java.time.LocalDate

// Grunnlag
data class BeregnSaertilskuddGrunnlagCore(
    val beregnDatoFra: LocalDate,
    val beregnDatoTil: LocalDate,
    val grunnlag: String
)

// Resultat
data class BeregnSaertilskuddResultatCore(
    val resultat: String
)
