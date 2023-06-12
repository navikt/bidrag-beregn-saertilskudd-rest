package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import java.util.ArrayList;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore;

public class SamvaersfradragCoreMapper extends CoreMapper {

  public BeregnSamvaersfradragGrunnlagCore mapSamvaersfradragGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      SjablonListe sjablonListe) {

    var samvaersklassePeriodeCoreListe = new ArrayList<SamvaersklassePeriodeCore>();

    // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til samværsfradrag core
    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      if (GrunnlagType.SAMVAERSKLASSE.equals(grunnlag.getType())) {
        Samvaersklasse samvaersklasse = grunnlagTilObjekt(grunnlag, Samvaersklasse.class);
        samvaersklassePeriodeCoreListe.add(samvaersklasse.tilCore(grunnlag.getReferanse()));
      }
    }

    // Henter aktuelle sjabloner
    var sjablonPeriodeCoreListe = new ArrayList<>(mapSjablonSamvaersfradrag(sjablonListe.getSjablonSamvaersfradragResponse(),
        beregnTotalSaertilskuddGrunnlag));

    return new BeregnSamvaersfradragGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(),
        beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(), samvaersklassePeriodeCoreListe, sjablonPeriodeCoreListe);
  }
}
