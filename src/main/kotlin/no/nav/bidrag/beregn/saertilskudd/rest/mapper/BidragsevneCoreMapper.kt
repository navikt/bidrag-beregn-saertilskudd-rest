package no.nav.bidrag.beregn.saertilskudd.rest.mapper

import no.nav.bidrag.beregn.bidragsevne.dto.AntallBarnIEgetHusholdPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore
import no.nav.bidrag.beregn.bidragsevne.dto.BostatusPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.InntektPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SaerfradragPeriodeCore
import no.nav.bidrag.beregn.bidragsevne.dto.SkatteklassePeriodeCore
import no.nav.bidrag.beregn.felles.dto.PeriodeCore
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn
import no.nav.bidrag.beregn.felles.enums.SjablonNavn
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BarnIHusstand
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bostatus
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektRolle
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Saerfradrag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Skatteklasse
import java.util.*

object BidragsevneCoreMapper : CoreMapper() {
    fun mapBidragsevneGrunnlagTilCore(
        beregnTotalSaertilskuddGrunnlag: BeregnTotalSaertilskuddGrunnlag,
        sjablontallMap: Map<String, SjablonTallNavn>, sjablonListe: SjablonListe
    ): BeregnBidragsevneGrunnlagCore {
        val inntektBPPeriodeCoreListe = ArrayList<InntektPeriodeCore>()
        val skatteklassePeriodeCoreListe = ArrayList<SkatteklassePeriodeCore>()
        val bostatusPeriodeCoreListe = ArrayList<BostatusPeriodeCore>()
        val antallBarnIEgetHusholdPeriodeCoreListe = ArrayList<AntallBarnIEgetHusholdPeriodeCore>()
        val saerfradragPeriodeCoreListe = ArrayList<SaerfradragPeriodeCore>()
        val sjablonPeriodeCoreListe = ArrayList<SjablonPeriodeCore>()

        // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til bidragsevne core
        for (grunnlag in beregnTotalSaertilskuddGrunnlag.grunnlagListe!!) {
            when (grunnlag.type) {
                GrunnlagType.INNTEKT -> {
                    val (rolle) = grunnlagTilObjekt(grunnlag, InntektRolle::class.java)
                    if (rolle == Rolle.BP) {
                        val bpInntekt = grunnlagTilObjekt(grunnlag, BPInntekt::class.java)
                        inntektBPPeriodeCoreListe.add(bpInntekt.tilCore(grunnlag.referanse!!))
                    }
                }

                GrunnlagType.BARN_I_HUSSTAND -> {
                    val barnIHusstand = grunnlagTilObjekt(grunnlag, BarnIHusstand::class.java)
                    antallBarnIEgetHusholdPeriodeCoreListe.add(barnIHusstand.tilCore(grunnlag.referanse!!))
                }

                GrunnlagType.BOSTATUS -> {
                    val bostatus = grunnlagTilObjekt(grunnlag, Bostatus::class.java)
                    bostatusPeriodeCoreListe.add(bostatus.tilCore(grunnlag.referanse!!))
                }

                GrunnlagType.SAERFRADRAG -> {
                    val saerfradrag = grunnlagTilObjekt(grunnlag, Saerfradrag::class.java)
                    saerfradragPeriodeCoreListe.add(saerfradrag.tilCore(grunnlag.referanse!!))
                }

                GrunnlagType.SKATTEKLASSE -> {
                    val skatteklasse = grunnlagTilObjekt(grunnlag, Skatteklasse::class.java)
                    skatteklassePeriodeCoreListe.add(skatteklasse.tilCore(grunnlag.referanse!!))
                }

                else -> {}
            }
        }
        // Hent aktuelle sjabloner
        sjablonPeriodeCoreListe.addAll(
            mapSjablonSjablontall(sjablonListe.sjablonSjablontallResponse, BIDRAGSEVNE, beregnTotalSaertilskuddGrunnlag, sjablontallMap)
        )
        sjablonPeriodeCoreListe.addAll(mapSjablonBidragsevne(sjablonListe.sjablonBidragsevneResponse, beregnTotalSaertilskuddGrunnlag))
        sjablonPeriodeCoreListe
            .addAll(mapSjablonTrinnvisSkattesats(sjablonListe.sjablonTrinnvisSkattesatsResponse, beregnTotalSaertilskuddGrunnlag))
        return BeregnBidragsevneGrunnlagCore(
            beregnTotalSaertilskuddGrunnlag.beregnDatoFra!!, beregnTotalSaertilskuddGrunnlag.beregnDatoTil!!,
            inntektBPPeriodeCoreListe, skatteklassePeriodeCoreListe, bostatusPeriodeCoreListe, antallBarnIEgetHusholdPeriodeCoreListe,
            saerfradragPeriodeCoreListe, sjablonPeriodeCoreListe
        )
    }

    private fun mapSjablonBidragsevne(
        sjablonBidragsevneListe: List<Bidragsevne>,
        beregnTotalSaertilskuddGrunnlag: BeregnTotalSaertilskuddGrunnlag
    ): List<SjablonPeriodeCore> {
        val beregnDatoFra = beregnTotalSaertilskuddGrunnlag.beregnDatoFra
        val beregnDatoTil = beregnTotalSaertilskuddGrunnlag.beregnDatoTil
        return sjablonBidragsevneListe
            .stream()
            .filter { (_, datoFom, datoTom): Bidragsevne -> datoFom!!.isBefore(beregnDatoTil) && !datoTom!!.isBefore(beregnDatoFra) }
            .map { (bostatus, datoFom, datoTom, belopBoutgift, belopUnderhold): Bidragsevne ->
                SjablonPeriodeCore(
                    PeriodeCore(datoFom!!, datoTom),
                    SjablonNavn.BIDRAGSEVNE.navn,
                    listOf(SjablonNokkelCore(SjablonNokkelNavn.BOSTATUS.navn, bostatus!!)),
                    Arrays.asList(
                        SjablonInnholdCore(SjablonInnholdNavn.BOUTGIFT_BELOP.navn, belopBoutgift!!),
                        SjablonInnholdCore(SjablonInnholdNavn.UNDERHOLD_BELOP.navn, belopUnderhold!!)
                    )
                )
            }
            .toList()
    }
}
