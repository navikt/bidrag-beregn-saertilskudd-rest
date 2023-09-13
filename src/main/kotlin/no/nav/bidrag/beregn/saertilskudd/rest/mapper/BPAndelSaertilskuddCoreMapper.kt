package no.nav.bidrag.beregn.saertilskudd.rest.mapper

import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.NettoSaertilskuddPeriodeCore
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektRolle
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt

object BPAndelSaertilskuddCoreMapper : CoreMapper() {
    fun mapBPsAndelSaertilskuddGrunnlagTilCore(
        beregnTotalSaertilskuddGrunnlag: BeregnTotalSaertilskuddGrunnlag, sjablontallMap: Map<String, SjablonTallNavn>, sjablonListe: SjablonListe
    ): BeregnBPsAndelSaertilskuddGrunnlagCore {
        val nettoSaertilskuddPeriodeListe = ArrayList<NettoSaertilskuddPeriodeCore>()
        val inntektBPPeriodeListe = ArrayList<InntektPeriodeCore>()
        val inntektBMPeriodeListe = ArrayList<InntektPeriodeCore>()
        val inntektBBPeriodeListe = ArrayList<InntektPeriodeCore>()

        // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til bidragsevne core
        for (grunnlag in beregnTotalSaertilskuddGrunnlag.grunnlagListe!!) {
            when (grunnlag.type) {
                GrunnlagType.INNTEKT -> {
                    val (rolle) = grunnlagTilObjekt(grunnlag, InntektRolle::class.java)
                    if (rolle == Rolle.BP) {
                        val bpInntekt = grunnlagTilObjekt(grunnlag, BPInntekt::class.java)
                        inntektBPPeriodeListe.add(bpInntekt.tilBPsAndelSaertilskuddCore(grunnlag.referanse!!))
                    } else if (rolle == Rolle.BM) {
                        val bmInntekt = grunnlagTilObjekt(grunnlag, BMInntekt::class.java)
                        inntektBMPeriodeListe.add(bmInntekt.tilCore(grunnlag.referanse!!))
                    } else if (rolle == Rolle.SB) {
                        val sbInntekt = grunnlagTilObjekt(grunnlag, SBInntekt::class.java)
                        inntektBBPeriodeListe.add(sbInntekt.tilCore(grunnlag.referanse!!))
                    }
                }

                GrunnlagType.NETTO_SAERTILSKUDD -> {
                    val nettoSaertilskudd = grunnlagTilObjekt(grunnlag, NettoSaertilskudd::class.java)
                    nettoSaertilskuddPeriodeListe.add(nettoSaertilskudd.tilCore(grunnlag.referanse!!))
                }

                else -> {}
            }
        }

        // Hent aktuelle sjabloner
        val sjablonPeriodeCoreListe = mapSjablonSjablontall(
            sjablonListe.sjablonSjablontallResponse, BP_ANDEL_SAERTILSKUDD,
            beregnTotalSaertilskuddGrunnlag, sjablontallMap
        )
        return BeregnBPsAndelSaertilskuddGrunnlagCore(
            beregnTotalSaertilskuddGrunnlag.beregnDatoFra!!,
            beregnTotalSaertilskuddGrunnlag.beregnDatoTil!!, nettoSaertilskuddPeriodeListe, inntektBPPeriodeListe, inntektBMPeriodeListe,
            inntektBBPeriodeListe, sjablonPeriodeCoreListe
        )
    }
}
