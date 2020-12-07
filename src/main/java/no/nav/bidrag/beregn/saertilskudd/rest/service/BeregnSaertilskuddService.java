package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import no.nav.bidrag.beregn.bidragsevne.BidragsevneCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneGrunnlagCore;
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.BPsAndelSaertilskuddCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore;
import no.nav.bidrag.beregn.felles.dto.AvvikCore;
import no.nav.bidrag.beregn.felles.dto.PeriodeCore;
import no.nav.bidrag.beregn.felles.dto.SjablonInnholdCore;
import no.nav.bidrag.beregn.felles.dto.SjablonNokkelCore;
import no.nav.bidrag.beregn.felles.dto.SjablonPeriodeCore;
import no.nav.bidrag.beregn.felles.enums.SjablonInnholdNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonNokkelNavn;
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevnePeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragPeriodeCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.Sjablontall;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.TrinnvisSkattesats;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPAndelSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPBidragsevneResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPSamvaersfradragResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.samvaersfradrag.SamvaersfradragCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragGrunnlagCore;
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore;
import no.nav.bidrag.commons.web.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BeregnSaertilskuddService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeregnSaertilskuddService.class);

  private static final String BIDRAGSEVNE = "Bidragsevne";
  private static final String BP_ANDEL_SAERTILSKUDD = "BPsAndelSaertilskudd";
  private static final String SAERTILSKUDD = "Særtilskudd";

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;
  private final BPsAndelSaertilskuddCore bpAndelSaertilskuddCore;
  private final SamvaersfradragCore samvaersfradragCore;
  private final SaertilskuddCore saertilskuddCore;

  public BeregnSaertilskuddService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore, BPsAndelSaertilskuddCore bpAndelSaertilskuddCore,
      SamvaersfradragCore samvaersfradragCore, SaertilskuddCore saertilskuddCore) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
    this.bpAndelSaertilskuddCore = bpAndelSaertilskuddCore;
    this.samvaersfradragCore = samvaersfradragCore;
    this.saertilskuddCore = saertilskuddCore;
  }

  public HttpResponse<BeregnTotalSaertilskuddResultat> beregn(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    // Kontroll av felles inputdata
    beregnTotalSaertilskuddGrunnlag.validerTotalSaertilskuddGrunnlag();
    beregnTotalSaertilskuddGrunnlag.getSoknadsbarnGrunnlag().validerSoknadsbarn();
    beregnTotalSaertilskuddGrunnlag.validerInntekt();

    // Lager en map for sjablontall (id og navn)
    var sjablontallMap = new HashMap<String, SjablonTallNavn>();
    for (SjablonTallNavn sjablonTallNavn : SjablonTallNavn.values()) {
      sjablontallMap.put(sjablonTallNavn.getId(), sjablonTallNavn);
    }

    // Henter sjabloner
    var sjablonListe = hentSjabloner();

    // Bygger grunnlag til core og utfører delberegninger
    return utfoerDelberegninger(beregnTotalSaertilskuddGrunnlag, sjablontallMap, sjablonListe);
  }

  //==================================================================================================================================================

  // Bygger grunnlag til core og kaller delberegninger
  private HttpResponse<BeregnTotalSaertilskuddResultat> utfoerDelberegninger(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {

    // ++ Bidragsevne
    var bidragsevneGrunnlagTilCore = byggBidragsevneGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablontallMap, sjablonListe);
    var bidragsevneResultatFraCore = beregnBidragsevne(bidragsevneGrunnlagTilCore);

    // ++ BPs andel av særtilskudd
    var bpAndelSaertilskuddGrunnlagTilCore = byggBpAndelSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablontallMap, sjablonListe);
    var bpAndelSaertilskuddResultatFraCore = beregnBPAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCore);

    // ++ Samværsfradrag
    var samvaersfradragGrunnlagTilCore = byggSamvaersfradragGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablonListe);
    var samvaersfradragResultatFraCore = beregnSamvaersfradrag(samvaersfradragGrunnlagTilCore);

    // ++ Særtilskudd (totalberegning)
    var saertilskuddGrunnlagTilCore = byggSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablontallMap, bidragsevneResultatFraCore,
        bpAndelSaertilskuddResultatFraCore, samvaersfradragResultatFraCore, sjablonListe);
    var saertilskuddResultatFraCore = beregnSaertilskudd(saertilskuddGrunnlagTilCore);

    // Bygger responsobjekt
    return HttpResponse.from(HttpStatus.OK, new BeregnTotalSaertilskuddResultat(
        new BeregnBPBidragsevneResultat(bidragsevneResultatFraCore),
        new BeregnBPAndelSaertilskuddResultat(bpAndelSaertilskuddResultatFraCore),
        new BeregnBPSamvaersfradragResultat(samvaersfradragResultatFraCore),
        new BeregnSaertilskuddResultat(saertilskuddResultatFraCore)));
  }

  //==================================================================================================================================================

  // Bygger grunnlag til core for beregning av bidragsevne
  private BeregnBidragsevneGrunnlagCore byggBidragsevneGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = new ArrayList<SjablonPeriodeCore>();
    sjablonPeriodeCoreListe.addAll(
        mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), BIDRAGSEVNE, beregnTotalSaertilskuddGrunnlag, sjablontallMap));
    sjablonPeriodeCoreListe.addAll(mapSjablonBidragsevne(sjablonListe.getSjablonBidragsevneResponse(), beregnTotalSaertilskuddGrunnlag));
    sjablonPeriodeCoreListe
        .addAll(mapSjablonTrinnvisSkattesats(sjablonListe.getSjablonTrinnvisSkattesatsResponse(), beregnTotalSaertilskuddGrunnlag));

    // Bygg grunnlag for beregning av bidragsevne. Her gjøres også kontroll av inputdata
    return beregnTotalSaertilskuddGrunnlag.bidragsevneTilCore(sjablonPeriodeCoreListe);
  }

  // Bygger grunnlag til core for beregning av BPs andel av særtilskudd
  private BeregnBPsAndelSaertilskuddGrunnlagCore byggBpAndelSaertilskuddGrunnlagTilCore(
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag, Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe) {

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), BP_ANDEL_SAERTILSKUDD,
        beregnTotalSaertilskuddGrunnlag, sjablontallMap);

    // Bygg grunnlag for beregning av BPs andel av særtilskudd. Her gjøres også kontroll av inputdata
    return beregnTotalSaertilskuddGrunnlag.bpAndelSaertilskuddTilCore(sjablonPeriodeCoreListe);
  }

  // Bygger grunnlag til core for beregning av samværsfradrag
  private BeregnSamvaersfradragGrunnlagCore byggSamvaersfradragGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      SjablonListe sjablonListe) {

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = mapSjablonSamvaersfradrag(sjablonListe.getSjablonSamvaersfradragResponse(), beregnTotalSaertilskuddGrunnlag);

    // Bygg grunnlag for beregning av samværsfradrag. Her gjøres også kontroll av inputdata
    return beregnTotalSaertilskuddGrunnlag.samvaersfradragTilCore(sjablonPeriodeCoreListe);
  }

  // Bygger grunnlag til core for beregning av særtilskudd
  private BeregnSaertilskuddGrunnlagCore byggSaertilskuddGrunnlagTilCore(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, BeregnBidragsevneResultatCore bidragsevneResultatFraCore,
      BeregnBPsAndelSaertilskuddResultatCore bpAndelSaertilskuddResultatFraCore, BeregnSamvaersfradragResultatCore samvaersfradragResultatFraCore,
      SjablonListe sjablonListe) {

    // Løp gjennom output fra beregning av bidragsevne og bygg opp ny input-liste til core
    var bidragsevnePeriodeCoreListe =
        bidragsevneResultatFraCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BidragsevnePeriodeCore(
                new PeriodeCore(resultat.getResultatDatoFraTil().getPeriodeDatoFra(),
                    resultat.getResultatDatoFraTil().getPeriodeDatoTil()),
                resultat.getResultatBeregning().getResultatEvneBelop(), resultat.getResultatBeregning().getResultat25ProsentInntekt()))
            .collect(toList());

    // Løp gjennom output fra beregning av BPs andel særtilskudd og bygg opp ny input-liste til core
    var bPAndelSaertilskuddPeriodeCoreListe =
        bpAndelSaertilskuddResultatFraCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new BPsAndelSaertilskuddPeriodeCore(
                new PeriodeCore(resultat.getResultatDatoFraTil().getPeriodeDatoFra(), resultat.getResultatDatoFraTil().getPeriodeDatoTil()),
                resultat.getResultatBeregning().getResultatAndelProsent(), resultat.getResultatBeregning().getResultatAndelBelop(),
                resultat.getResultatBeregning().getBarnetErSelvforsorget()))
            .collect(toList());

    // Løp gjennom output fra beregning av samværsfradrag og bygg opp ny input-liste til core
    var samvaersfradragPeriodeCoreListe =
        samvaersfradragResultatFraCore.getResultatPeriodeListe()
            .stream()
            .map(resultat -> new SamvaersfradragPeriodeCore(
                new PeriodeCore(resultat.getResultatDatoFraTil().getPeriodeDatoFra(),
                    resultat.getResultatDatoFraTil().getPeriodeDatoTil()),
                resultat.getResultatBeregning().getResultatSamvaersfradragBelop()))
            .collect(toList());

    // Hent aktuelle sjabloner
    var sjablonPeriodeCoreListe = mapSjablonSjablontall(sjablonListe.getSjablonSjablontallResponse(), SAERTILSKUDD,
        beregnTotalSaertilskuddGrunnlag, sjablontallMap);

    // Bygg grunnlag for beregning av barnebidrag. Her gjøres også kontroll av inputdata
    return beregnTotalSaertilskuddGrunnlag.saertilskuddTilCore(bidragsevnePeriodeCoreListe, bPAndelSaertilskuddPeriodeCoreListe,
        samvaersfradragPeriodeCoreListe, sjablonPeriodeCoreListe);
  }

  //==================================================================================================================================================

  // Kaller core for beregning av bidragsevne
  private BeregnBidragsevneResultatCore beregnBidragsevne(BeregnBidragsevneGrunnlagCore bidragsevneGrunnlagTilCore) {

    // Kaller core-modulen for beregning av bidragsevne
    LOGGER.debug("Bidragsevne - grunnlag for beregning: {}", bidragsevneGrunnlagTilCore);
    var bidragsevneResultatFraCore = bidragsevneCore.beregnBidragsevne(bidragsevneGrunnlagTilCore);

    if (!bidragsevneResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: " + System.lineSeparator()
          + bidragsevneResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("Bidragsevne - grunnlag for beregning:" + System.lineSeparator()
          + "beregnDatoFra= " + bidragsevneGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + bidragsevneGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "antallBarnIEgetHusholdPeriodeListe= " + bidragsevneGrunnlagTilCore.getAntallBarnIEgetHusholdPeriodeListe() + System.lineSeparator()
          + "bostatusPeriodeListe= " + bidragsevneGrunnlagTilCore.getBostatusPeriodeListe() + System.lineSeparator()
          + "inntektPeriodeListe= " + bidragsevneGrunnlagTilCore.getInntektPeriodeListe() + System.lineSeparator()
          + "særfradragPeriodeListe= " + bidragsevneGrunnlagTilCore.getSaerfradragPeriodeListe() + System.lineSeparator()
          + "skatteklassePeriodeListe= " + bidragsevneGrunnlagTilCore.getSkatteklassePeriodeListe());
      throw new UgyldigInputException("Ugyldig input ved beregning av bidragsevne. Følgende avvik ble funnet: "
          + bidragsevneResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Bidragsevne - resultat av beregning: {}", bidragsevneResultatFraCore.getResultatPeriodeListe());
    return bidragsevneResultatFraCore;
  }

  // Kaller core for beregning av BPs andel av særtilskudd
  private BeregnBPsAndelSaertilskuddResultatCore beregnBPAndelSaertilskudd(
      BeregnBPsAndelSaertilskuddGrunnlagCore bpAndelSaertilskuddGrunnlagTilCore) {

    // Kaller core-modulen for beregning av BPs andel av særtilskudd
    LOGGER.debug("BPs andel av særtilskudd - grunnlag for beregning: {}", bpAndelSaertilskuddGrunnlagTilCore);
    var bpAndelSaertilskuddResultatFraCore = bpAndelSaertilskuddCore.beregnBPsAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCore);

    if (!bpAndelSaertilskuddResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + bpAndelSaertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("BPs andel av særtilskudd - grunnlag for beregning:" + System.lineSeparator()
          + "beregnDatoFra= " + bpAndelSaertilskuddGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + bpAndelSaertilskuddGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "nettoSaertilskuddPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getNettoSaertilskuddPeriodeListe() + System.lineSeparator()
          + "inntektBPPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBPPeriodeListe() + System.lineSeparator()
          + "inntektBMPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBMPeriodeListe() + System.lineSeparator()
          + "inntektBBPeriodeListe= " + bpAndelSaertilskuddGrunnlagTilCore.getInntektBBPeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av BPs andel av særtilskudd. Følgende avvik ble funnet: "
          + bpAndelSaertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("BPs andel av særtilskudd - resultat av beregning: {}", bpAndelSaertilskuddResultatFraCore.getResultatPeriodeListe());
    return bpAndelSaertilskuddResultatFraCore;
  }

  // Kaller core for beregning av samværsfradrag
  private BeregnSamvaersfradragResultatCore beregnSamvaersfradrag(BeregnSamvaersfradragGrunnlagCore samvaersfradragGrunnlagTilCore) {

    // Kaller core-modulen for beregning av samværsfradrag
    LOGGER.debug("Samværsfradrag - grunnlag for beregning: {}", samvaersfradragGrunnlagTilCore);
    var samvaersfradragResultatFraCore = samvaersfradragCore.beregnSamvaersfradrag(samvaersfradragGrunnlagTilCore);

    if (!samvaersfradragResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet: " + System.lineSeparator()
          + samvaersfradragResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("Samværsfradrag - grunnlag for beregning: " + System.lineSeparator()
          + "beregnDatoFra= " + samvaersfradragGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + samvaersfradragGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "soknadsbarnFodselsdato= " + samvaersfradragGrunnlagTilCore.getSoknadsbarnFodselsdato() + System.lineSeparator()
          + "samvaersklassePeriodeListe= " + samvaersfradragGrunnlagTilCore.getSamvaersklassePeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av samværsfradrag. Følgende avvik ble funnet: "
          + samvaersfradragResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Samværsfradrag - resultat av beregning: {}", samvaersfradragResultatFraCore.getResultatPeriodeListe());
    return samvaersfradragResultatFraCore;
  }

  // Kaller core for beregning av særtilskudd
  private BeregnSaertilskuddResultatCore beregnSaertilskudd(BeregnSaertilskuddGrunnlagCore saertilskuddGrunnlagTilCore) {

    // Kaller core-modulen for beregning av særtilskudd
    LOGGER.debug("Særtilskudd - grunnlag for beregning: {}", saertilskuddGrunnlagTilCore);
    var saertilskuddResultatFraCore = saertilskuddCore.beregnSaertilskudd(saertilskuddGrunnlagTilCore);

    if (!saertilskuddResultatFraCore.getAvvikListe().isEmpty()) {
      LOGGER.error("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet: " + System.lineSeparator()
          + saertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst)
          .collect(Collectors.joining(System.lineSeparator())));
      LOGGER.info("Særtilskudd - grunnlag for beregning: " + System.lineSeparator()
          + "beregnDatoFra= " + saertilskuddGrunnlagTilCore.getBeregnDatoFra() + System.lineSeparator()
          + "beregnDatoTil= " + saertilskuddGrunnlagTilCore.getBeregnDatoTil() + System.lineSeparator()
          + "soknadsbarnPersonId= " + saertilskuddGrunnlagTilCore.getSoknadsbarnPersonId() + System.lineSeparator()
          + "bidragsevnePeriodeListe= " + saertilskuddGrunnlagTilCore.getBidragsevnePeriodeListe() + System.lineSeparator()
          + "bPsAndelSaertilskuddPeriodeListe= " + saertilskuddGrunnlagTilCore.getBPsAndelSaertilskuddPeriodeListe() + System.lineSeparator()
          + "samvaersfradragPeriodeListe= " + saertilskuddGrunnlagTilCore.getSamvaersfradragPeriodeListe() + System.lineSeparator()
          + "lopendeBidragPeriodeListe= " + saertilskuddGrunnlagTilCore.getLopendeBidragPeriodeListe() + System.lineSeparator());
      throw new UgyldigInputException("Ugyldig input ved beregning av særtilskudd. Følgende avvik ble funnet: "
          + saertilskuddResultatFraCore.getAvvikListe().stream().map(AvvikCore::getAvvikTekst).collect(Collectors.joining("; ")));
    }

    LOGGER.debug("Særtilskudd - resultat av beregning: {}", saertilskuddResultatFraCore.getResultatPeriodeListe());
    return saertilskuddResultatFraCore;
  }

  //==================================================================================================================================================

  // Henter sjabloner
  private SjablonListe hentSjabloner() {

    // Henter sjabloner for sjablontall
    var sjablonSjablontallListe = Optional.ofNullable(sjablonConsumer.hentSjablonSjablontall().getResponseEntity().getBody()).orElse(emptyList());
    LOGGER.debug("Antall sjabloner hentet av type Sjablontall: {}", sjablonSjablontallListe.size());

    // Henter sjabloner for samværsfradrag
    var sjablonSamvaersfradragListe = Optional.ofNullable(sjablonConsumer.hentSjablonSamvaersfradrag().getResponseEntity().getBody())
        .orElse(emptyList());
    LOGGER.debug("Antall sjabloner hentet av type Samværsfradrag: {}", sjablonSamvaersfradragListe.size());

    // Henter sjabloner for bidragsevne
    var sjablonBidragsevneListe = Optional.ofNullable(sjablonConsumer.hentSjablonBidragsevne().getResponseEntity().getBody()).orElse(emptyList());
    LOGGER.debug("Antall sjabloner hentet av type Bidragsevne: {}", sjablonBidragsevneListe.size());

    // Henter sjabloner for trinnvis skattesats
    var sjablonTrinnvisSkattesatsListe = Optional.ofNullable(sjablonConsumer.hentSjablonTrinnvisSkattesats().getResponseEntity().getBody())
        .orElse(emptyList());
    LOGGER.debug("Antall sjabloner hentet av type Trinnvis skattesats: {}", sjablonTrinnvisSkattesatsListe.size());

    return new SjablonListe(sjablonSjablontallListe, sjablonSamvaersfradragListe, sjablonBidragsevneListe, sjablonTrinnvisSkattesatsListe);
  }

  // Mapper sjabloner av typen sjablontall
  // Filtrerer bort de sjablonene som ikke brukes i den aktuelle delberegningen og de som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  private List<SjablonPeriodeCore> mapSjablonSjablontall(List<Sjablontall> sjablonSjablontallListe, String delberegning,
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

  // Mapper sjabloner av typen samværsfradrag
  // Filtrerer bort de sjablonene som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  private List<SjablonPeriodeCore> mapSjablonSamvaersfradrag(List<Samvaersfradrag> sjablonSamvaersfradragListe,
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

  // Mapper sjabloner av typen bidragsevne
  // Filtrerer bort de sjablonene som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  private List<SjablonPeriodeCore> mapSjablonBidragsevne(List<Bidragsevne> sjablonBidragsevneListe,
      BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    var beregnDatoFra = beregnTotalSaertilskuddGrunnlag.getBeregnDatoFra();
    var beregnDatoTil = beregnTotalSaertilskuddGrunnlag.getBeregnDatoTil();

    return sjablonBidragsevneListe
        .stream()
        .filter(sjablon -> (sjablon.getDatoFom().isBefore(beregnDatoTil) && (!(sjablon.getDatoTom().isBefore(beregnDatoFra)))))
        .map(sjablon -> new SjablonPeriodeCore(
            new PeriodeCore(sjablon.getDatoFom(), sjablon.getDatoTom()),
            SjablonNavn.BIDRAGSEVNE.getNavn(),
            singletonList(new SjablonNokkelCore(SjablonNokkelNavn.BOSTATUS.getNavn(), sjablon.getBostatus())),
            asList(new SjablonInnholdCore(SjablonInnholdNavn.BOUTGIFT_BELOP.getNavn(), sjablon.getBelopBoutgift()),
                new SjablonInnholdCore(SjablonInnholdNavn.UNDERHOLD_BELOP.getNavn(), sjablon.getBelopUnderhold()))))
        .collect(toList());
  }

  // Mapper sjabloner av typen trinnvis skattesats
  // Filtrerer bort de sjablonene som ikke er innenfor intervallet beregnDatoFra-beregnDatoTil
  private List<SjablonPeriodeCore> mapSjablonTrinnvisSkattesats(List<TrinnvisSkattesats> sjablonTrinnvisSkattesatsListe,
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
      case SAERTILSKUDD -> sjablonTallNavn.getSaertilskudd();
      default -> false;
    };
  }
}
