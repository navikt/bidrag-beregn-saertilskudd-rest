package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.validator.GenericValidator.isDate;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;


public abstract class CoreMapper {

  protected static final String BP_ANDEL_SAERTILSKUDD = "BPsAndelSaertilskudd";
  protected static final String BIDRAGSEVNE = "Bidragsevne";

  protected static final String SAERFRADRAG_TYPE = "Saerfradrag";
  protected static final String SKATTEKLASSE_TYPE = "Skatteklasse";
  protected static final String BARN_I_HUSSTAND_TYPE = "BarnIHusstand";
  protected static final String BOSTATUS_TYPE = "Bostatus";
  protected static final String INNTEKT_TYPE = "Inntekt";
  protected static final String NETTO_SAERTILSKUDD_TYPE= "NettoSaertilskudd";
  protected static final String SAMVAERSKLASSE_TYPE = "Samvaersklasse";
  protected static final String LOPENDE_BIDRAG_TYPE = "LopendeBidrag";

  // Mapper sjabloner av typen sjablontall
  // Filtrerer bort de sjablonene som ikke brukes i den aktuelle delberegningen og de som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  public List<SjablonPeriodeCore> mapSjablonSjablontall(List<Sjablontall> sjablonSjablontallListe, String delberegning,
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag, Map<String, SjablonTallNavn> sjablontallMap) {

    var beregnDatoFra = beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra();
    var beregnDatoTil = beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil();

    return sjablonSjablontallListe
        .stream()
        .filter(sjablon -> (sjablon.getDatoFom().isBefore(beregnDatoTil) && (!(sjablon.getDatoTom().isBefore(beregnDatoFra)))))
        .filter(sjablon -> filtrerSjablonTall(sjablontallMap.getOrDefault(sjablon.getTypeSjablon(), SjablonTallNavn.DUMMY), delberegning))
        .map(sjablon -> new SjablonPeriodeCore(
            new PeriodeCore(sjablon.getDatoFom(), sjablon.getDatoTom()),
            sjablontallMap.getOrDefault(sjablon.getTypeSjablon(), SjablonTallNavn.DUMMY).getNavn(),
            emptyList(),
            singletonList(new SjablonInnholdCore(SjablonInnholdNavn.SJABLON_VERDI.getNavn(), sjablon.getVerdi()))))
        .collect(toList());
  }
  // Mapper sjabloner av typen trinnvis skattesats
  // Filtrerer bort de sjablonene som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  public List<SjablonPeriodeCore> mapSjablonTrinnvisSkattesats(List<TrinnvisSkattesats> sjablonTrinnvisSkattesatsListe,
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    var beregnDatoFra = beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra();
    var beregnDatoTil = beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil();

    return sjablonTrinnvisSkattesatsListe
        .stream()
        .filter(sjablon -> (sjablon.getDatoFom().isBefore(beregnDatoTil) && (!(sjablon.getDatoTom().isBefore(beregnDatoFra)))))
        .map(sjablon -> new SjablonPeriodeCore(
            new PeriodeCore(sjablon.getDatoFom(), sjablon.getDatoTom()),
            SjablonNavn.TRINNVIS_SKATTESATS.getNavn(),
            emptyList(),
            asList(new SjablonInnholdCore(SjablonInnholdNavn.INNTEKTSGRENSE_BELOP.getNavn(), sjablon.getInntektgrense()),
                new SjablonInnholdCore(SjablonInnholdNavn.SKATTESATS_PROSENT.getNavn(), sjablon.getSats()))))
        .collect(toList());
  }
  // Sjekker om en type SjablonTall er i bruk for en delberegning
  private boolean filtrerSjablonTall(SjablonTallNavn sjablonTallNavn, String delberegning) {

    return switch (delberegning) {
      case BIDRAGSEVNE -> sjablonTallNavn.getBidragsevne();
      case BP_ANDEL_SAERTILSKUDD -> sjablonTallNavn.getBpAndelSaertilskudd();
      default -> false;
    };
  }

  // Sjekker om dataelement av typen String er null
//  protected void evaluerStringType(String verdi, String dataElement, String grunnlagType) {
//    if (null == verdi || ("null".equals(verdi))) {
//      throw new UgyldigInputException(dataElement + " i objekt av type " + grunnlagType + " er null");
//    }
//  }
//
//  protected void evauluerBooleanType(JsonNode booleanNode, String fieldName) {
//    if(!booleanNode.isBoolean()) {
//      throw new UgyldigInputException(fieldName + "er ikke av type boolean");
//    }
//  }
//
//  protected void evaluerNumberType(JsonNode numberNode, String fieldName) {
//    if(!numberNode.isNumber()) {
//      throw new UgyldigInputException(fieldName + "er ikke av type number");
//    }
//  }


//  protected PeriodeCore mapPeriode(JsonNode grunnlagInnhold, String grunnlagType) {
//    String datoFom = getNodeIfExists(grunnlagInnhold, grunnlagType, "datoFom").asText();
//    if (!gyldigDato(datoFom)) {
//      throw new UgyldigInputException("datoFom i objekt av type " + grunnlagType + " mangler, er null eller har ugyldig verdi");
//    }
//    String datoTil = getNodeIfExists(grunnlagInnhold, grunnlagType, "datoTil").asText();
//    if (!gyldigDato(datoTil)) {
//      throw new UgyldigInputException("datoTil i objekt av type " + grunnlagType + " mangler, er null eller har ugyldig verdi");
//    }
//    return new PeriodeCore(LocalDate.parse(datoFom), LocalDate.parse(datoTil));
//  }

  protected JsonNode getNodeIfExists(JsonNode grunnlagInnhold, String grunnlagType, String fieldName) {
    if (grunnlagInnhold.has(fieldName)) {
      return grunnlagInnhold.get(fieldName);
    }
    throw new UgyldigInputException(fieldName + " i objekt av type " + grunnlagType + " mangler");
  }

  // Sjekker om dato har gyldig format
  protected boolean gyldigDato(String dato) {
    if (null == dato) {
      return false;
    }
    return isDate(dato, "yyyy-MM-dd", true);
  }

  // Mapper sjabloner av typen samværsfradrag
  // Filtrerer bort de sjablonene som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  protected List<SjablonPeriodeCore> mapSjablonSamvaersfradrag(List<Samvaersfradrag> sjablonSamvaersfradragListe,
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    var beregnDatoFra = beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra();
    var beregnDatoTil = beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil();

    return sjablonSamvaersfradragListe
        .stream()
        .filter(sjablon -> (sjablon.getDatoFom().isBefore(beregnDatoTil) && (!(sjablon.getDatoTom().isBefore(beregnDatoFra)))))
        .map(sjablon -> new SjablonPeriodeCore(
            new PeriodeCore(sjablon.getDatoFom(), sjablon.getDatoTom()),
            SjablonNavn.SAMVAERSFRADRAG.getNavn(),
            asList(new SjablonNokkelCore(SjablonNokkelNavn.SAMVAERSKLASSE.getNavn(), sjablon.getSamvaersklasse()),
                new SjablonNokkelCore(SjablonNokkelNavn.ALDER_TOM.getNavn(), String.valueOf(sjablon.getAlderTom()))),
            asList(new SjablonInnholdCore(SjablonInnholdNavn.ANTALL_DAGER_TOM.getNavn(), BigDecimal.valueOf(sjablon.getAntDagerTom())),
                new SjablonInnholdCore(SjablonInnholdNavn.ANTALL_NETTER_TOM.getNavn(), BigDecimal.valueOf(sjablon.getAntNetterTom())),
                new SjablonInnholdCore(SjablonInnholdNavn.FRADRAG_BELOP.getNavn(), sjablon.getBelopFradrag()))))
        .collect(toList());
  }

//  protected String hentSoknadsbarnId(JsonNode grunnlagInnhold, String grunnlagType) {
//    String soknadsbarnId = getNodeIfExists(grunnlagInnhold, grunnlagType, "soknadsbarnId").asText();
//    evaluerStringType(soknadsbarnId, "soknadsbarnId", grunnlagType);
//    return soknadsbarnId;
//  }

  protected <T> T jsonNodeTilObjekt(JsonNode jsonNode, Class<T> contentClass) {
    try {
      jacksonObjectMapper().registerModule(new JavaTimeModule());
      T objekt = jacksonObjectMapper().readValue(jsonNode.toString(), contentClass);
      return objekt;
    } catch (JsonProcessingException e) {
      throw new UgyldigInputException("Kunne ikke deserialisere " + contentClass.getName());
    }
  }
}
