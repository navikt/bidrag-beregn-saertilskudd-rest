package no.nav.bidrag.beregn.saertilskudd.rest.mapper;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.validator.GenericValidator.isDate;

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
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;


public abstract class CoreMapper {

  protected static final String BP_ANDEL_SAERTILSKUDD = "BPsAndelSaertilskudd";
  protected static final String BIDRAGSEVNE = "Bidragsevne";

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

  protected <T> T grunnlagTilObjekt(Grunnlag grunnlag, Class<T> contentClass) {
    try {
      jacksonObjectMapper().registerModule(new JavaTimeModule());
      T objekt = jacksonObjectMapper().readValue(grunnlag.getInnhold().toString(), contentClass);
      return objekt;
    } catch (JsonProcessingException e) {
      throw new UgyldigInputException("Kunne ikke deserialisere " + contentClass.getName());
    }
  }
}
