package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersklasse;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore;

public class SamvaersfradragCoreMapper extends CoreMapper {

  public BeregnSamvaersfradragGrunnlagCore mapSamvaersfradragGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      SjablonListe sjablonListe) {

    var samvaersklassePeriodeCoreListe = new ArrayList<SamvaersklassePeriodeCore>();

    // Løper gjennom alle grunnlagene og identifiserer de som skal mappes til samværsfradrag core
    for (Grunnlag grunnlag : beregnTotalSaertilskuddGrunnlag.getGrunnlagListe()) {
      if (SAMVAERSKLASSE_TYPE.equals(grunnlag.getType())) {
        Samvaersklasse samvaersklasse = jsonNodeTilObjekt(grunnlag.getInnhold(), Samvaersklasse.class);
        samvaersklassePeriodeCoreListe.add(samvaersklasse.TilCore(grunnlag.getReferanse()));
//        samvaersklassePeriodeCoreListe.add(mapSamvaersklasse(grunnlag));
      }
    }

    // Henter aktuelle sjabloner
    var sjablonPeriodeCoreListe = new ArrayList<>(mapSjablonSamvaersfradrag(sjablonListe.getSjablonSamvaersfradragResponse(),
        beregnTotalSaertilskuddGrunnlag));

    return new BeregnSamvaersfradragGrunnlagCore(beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra(),
        beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil(),
        samvaersklassePeriodeCoreListe, sjablonPeriodeCoreListe);
  }

//  private SamvaersklassePeriodeCore mapSamvaersklasse(Grunnlag grunnlag) {
//    String barnPersonId = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "soknadsbarnId").asText();
//    evaluerStringType(barnPersonId, "soknadsbarnId", "Samvaersklasse");
//    String barnFodselsdato = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "soknadsbarnFodselsdato").asText();
//    if(!gyldigDato(barnFodselsdato)) {
//      throw new UgyldigInputException("soknadsbarnFodselsdato i objekt av type " + grunnlag.getType() + " mangler, er null eller har ugyldig verdi");
//    }
//    String samvaersklasse = getNodeIfExists(grunnlag.getInnhold(), grunnlag.getType(), "samvaersklasseId").asText();
//    evaluerStringType(samvaersklasse, "samvaersklasseId", "Samvaersklasse");
//    return new SamvaersklassePeriodeCore(mapPeriode(grunnlag.getInnhold(), grunnlag.getType()), Integer.parseInt(barnPersonId), LocalDate.parse(barnFodselsdato), samvaersklasse);
//  }
}
