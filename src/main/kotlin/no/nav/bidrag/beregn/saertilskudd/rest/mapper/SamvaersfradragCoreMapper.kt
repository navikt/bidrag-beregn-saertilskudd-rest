package no.nav.bidrag.beregn.saertilskudd.rest.mapper

import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore

object SamvaersfradragCoreMapper : CoreMapper() {
    fun mapSamvaersfradragGrunnlagTilCore(
        beregnTotalSaertilskuddGrunnlag: BeregnTotalSaertilskuddGrunnlag,
        sjablonListe: SjablonListe
    ): BeregnSamvaersfradragGrunnlagCore {
        val samvaersklassePeriodeCoreListe = ArrayList<SamvaersklassePeriodeCore>()

        // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til samværsfradrag core
        for (grunnlag in beregnTotalSaertilskuddGrunnlag.grunnlagListe!!) {
            if (GrunnlagType.SAMVAERSKLASSE == grunnlag.type) {
                val samvaersklasse = grunnlagTilObjekt(grunnlag, Samvaersklasse::class.java)
                samvaersklassePeriodeCoreListe.add(samvaersklasse.tilCore(grunnlag.referanse!!))
            }
        }

        // Henter aktuelle sjabloner
        val sjablonPeriodeCoreListe = ArrayList(
            mapSjablonSamvaersfradrag(
                sjablonListe.sjablonSamvaersfradragResponse,
                beregnTotalSaertilskuddGrunnlag
            )
        )
        return BeregnSamvaersfradragGrunnlagCore(
            beregnTotalSaertilskuddGrunnlag.beregnDatoFra!!,
            beregnTotalSaertilskuddGrunnlag.beregnDatoTil!!, samvaersklassePeriodeCoreListe, sjablonPeriodeCoreListe
        )
    }
}
