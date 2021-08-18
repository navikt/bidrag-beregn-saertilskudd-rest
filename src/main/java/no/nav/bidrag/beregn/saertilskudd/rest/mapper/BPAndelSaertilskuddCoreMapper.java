package no.nav.bidrag.beregn.saertilskudd.rest.mapper;


import java.util.ArrayList;
import java.util.Map;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.InntektPeriodeCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.NettoSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BMInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPInntekt;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.InntektRolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.NettoSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Rolle;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SBInntekt;

public class BPAndelSaertilskuddCoreMapper extends CoreMapper {

  public BeregnBPsAndelSaertilskuddGrunnlagCore mapBPsAndelSaertilskuddGrunnlagTilCore(
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {
    var nettoSaertilskuddPeriodeListe = new ArrayList<NettoSaertilskuddPeriodeCore>();
    var inntektBPPeriodeListe = new ArrayList<InntektPeriodeCore>();
    var inntektBMPeriodeListe = new ArrayList<InntektPeriodeCore>();
    var inntektBBPeriodeListe = new ArrayList<InntektPeriodeCore>();

    // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til bidragsevne core
    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      switch (grunnlag.getType()) {
        case INNTEKT -> {
          InntektRolle inntektRolle = grunnlagTilObjekt(grunnlag, InntektRolle.class);
          Rolle rolle = inntektRolle.getRolle();
          if (rolle.equals(rolle.BP)) {
            BPInntekt bpInntekt = grunnlagTilObjekt(grunnlag, BPInntekt.class);
            inntektBPPeriodeListe.add(bpInntekt.tilBPsAndelSaertilskuddCore(grunnlag.getReferanse()));
          } else if (rolle.equals(rolle.BM)) {
            BMInntekt bmInntekt = grunnlagTilObjekt(grunnlag, BMInntekt.class);
            inntektBMPeriodeListe.add(bmInntekt.tilCore(grunnlag.getReferanse()));
          } else if (rolle.equals(rolle.SB)) {
            SBInntekt sbInntekt = grunnlagTilObjekt(grunnlag, SBInntekt.class);
            inntektBBPeriodeListe.add(sbInntekt.tilCore(grunnlag.getReferanse()));
          }
        }
        case NETTO_SAERTILSKUDD -> {
          NettoSaertilskudd nettoSaertilskudd = grunnlagTilObjekt(grunnlag, NettoSaertilskudd.class);
          nettoSaertilskuddPeriodeListe.add(nettoSaertilskudd.tilCore(grunnlag.getReferanse()));
        }
      }
    }

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), BP_ANDEL_SAERTILSKUDD,
        beregnTotalSaertilskuddGrunnlag, sjablontallMap);

    return new BeregnBPsAndelSaertilskuddGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(),
        beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(), nettoSaertilskuddPeriodeListe, inntektBPPeriodeListe, inntektBMPeriodeListe,
        inntektBBPeriodeListe, sjablonPeriodeCoreListe);
  }
}
