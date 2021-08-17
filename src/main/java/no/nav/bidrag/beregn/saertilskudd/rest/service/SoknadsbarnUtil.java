package no.nav.bidrag.beregn.saertilskudd.rest.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;

public class SoknadsbarnUtil {

  protected static final String SOKNADSBARN_INFO_TYPE = "SoknadsbarnInfo";

  public static String hentFodselsdatoForId(Integer soknadsbarnId, Map<Integer, String> soknadsbarnMap) {
    if (soknadsbarnMap.containsKey(soknadsbarnId)) {
      return soknadsbarnMap.get(soknadsbarnId);
    }
    return null;
  }

  // Lager en map for søknadsbarn (id og fødselsdato)
  protected static Map<Integer, String> mapSoknadsbarn(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    var soknadsbarnMap = new HashMap<Integer, String>();
    beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlag.getType().equals(SOKNADSBARN_INFO_TYPE))
        .forEach(grunnlag -> soknadsbarnMap.putAll(mapSoknadsbarnInfo(grunnlag)));
    return soknadsbarnMap;
  }

  private static Map<Integer, String> mapSoknadsbarnInfo(Grunnlag grunnlag) {
    var soknadsbarnMap = new HashMap<Integer, String>();
    var id = Optional.of(grunnlag.getInnhold().get("id"))
        .orElseThrow(() -> new UgyldigInputException("id mangler i objekt av type SoknadsbarnInfo")).asInt();
    var fodselsdato = Optional.of(grunnlag.getInnhold().get("fodselsdato"))
        .orElseThrow(() -> new UgyldigInputException("fodselsdato mangler i objekt av type SoknadsbarnInfo")).asText();
    soknadsbarnMap.put(id, fodselsdato);
    return soknadsbarnMap;
  }

  // Validerer at alle forekomster av soknadsbarnId er numeriske og har tilknyttet fødselsdato
  protected static void validerSoknadsbarnId(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    var soknadsbarnMap = mapSoknadsbarn(beregnTotalSaertilskuddGrunnlag);
    beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().forEach(grunnlag -> {
      if (grunnlag.getInnhold().has("soknadsbarnId")) {
        var soknadsbarnId = 0;
        if (grunnlag.getInnhold().get("soknadsbarnId").isNumber()) {
          soknadsbarnId = grunnlag.getInnhold().get("soknadsbarnId").asInt();
        } else {
          throw new UgyldigInputException("soknadsbarnId i objekt av type " + grunnlag.getType() + " er null eller har ugyldig verdi");
        }
        if (null == hentFodselsdatoForId(soknadsbarnId, soknadsbarnMap)) {
          throw new UgyldigInputException("fodselsdato for soknadsbarnId " + soknadsbarnId + " i objekt av type " + grunnlag.getType() + " mangler");
        }
      }
    });
  }
}
