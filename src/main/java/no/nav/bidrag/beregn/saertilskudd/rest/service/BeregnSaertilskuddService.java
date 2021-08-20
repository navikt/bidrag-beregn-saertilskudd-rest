package no.nav.bidrag.beregn.saertilskudd.rest.service;

import static java.util.Collections.emptyList;
import static no.nav.bidrag.beregn.saertilskudd.rest.mapper.CoreMapper.grunnlagTilObjekt;

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
import no.nav.bidrag.beregn.felles.enums.SjablonTallNavn;
import no.nav.bidrag.beregn.saertilskudd.SaertilskuddCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddGrunnlagCore;
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonConsumer;
import no.nav.bidrag.beregn.saertilskudd.rest.consumer.SjablonListe;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPAndelSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPBidragsevneResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnBPSamvaersfradragResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.GrunnlagType;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SoknadsBarnInfo;
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BPAndelSaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.BidragsevneCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SaertilskuddCoreMapper;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.SamvaersfradragCoreMapper;
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

  private final SjablonConsumer sjablonConsumer;
  private final BidragsevneCore bidragsevneCore;
  private final BPsAndelSaertilskuddCore bpAndelSaertilskuddCore;
  private final SamvaersfradragCore samvaersfradragCore;
  private final SaertilskuddCore saertilskuddCore;
  private final BidragsevneCoreMapper bidragsevneCoreMapper;
  private final BPAndelSaertilskuddCoreMapper bpAndelSaertilskuddCoreMapper;
  private final SamvaersfradragCoreMapper samvaersfradragCoreMapper;
  private final SaertilskuddCoreMapper saertilskuddCoreMapper;

  public BeregnSaertilskuddService(SjablonConsumer sjablonConsumer, BidragsevneCore bidragsevneCore, BPsAndelSaertilskuddCore bpAndelSaertilskuddCore,
      SamvaersfradragCore samvaersfradragCore, SaertilskuddCore saertilskuddCore, BidragsevneCoreMapper bidragsevneCoreMapper,
      BPAndelSaertilskuddCoreMapper bpAndelSaertilskuddCoreMapper, SamvaersfradragCoreMapper samvaersfradragCoreMapper,
      SaertilskuddCoreMapper saertilskuddCoreMapper) {
    this.sjablonConsumer = sjablonConsumer;
    this.bidragsevneCore = bidragsevneCore;
    this.bpAndelSaertilskuddCore = bpAndelSaertilskuddCore;
    this.samvaersfradragCore = samvaersfradragCore;
    this.saertilskuddCore = saertilskuddCore;
    this.bidragsevneCoreMapper = bidragsevneCoreMapper;
    this.bpAndelSaertilskuddCoreMapper = bpAndelSaertilskuddCoreMapper;
    this.samvaersfradragCoreMapper = samvaersfradragCoreMapper;
    this.saertilskuddCoreMapper = saertilskuddCoreMapper;
  }

  public HttpResponse<BeregnTotalSaertilskuddResultat> beregn(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {

    // Kontroll av felles inputdata
    beregnTotalSaertilskuddGrunnlag.valider();

    // Validerer og henter ut soknadsbarn
    SoknadsBarnInfo soknadsBarnInfo = validerSoknadsbarnId(beregnTotalSaertilskuddGrunnlag);

    // Lager en map for sjablontall (id og navn)
    var sjablontallMap = new HashMap<String, SjablonTallNavn>();
    for (SjablonTallNavn sjablonTallNavn : SjablonTallNavn.values()) {
      sjablontallMap.put(sjablonTallNavn.getId(), sjablonTallNavn);
    }

    // Henter sjabloner
    var sjablonListe = hentSjabloner();

    // Bygger grunnlag til core og utfører delberegninger
    return utfoerDelberegninger(beregnTotalSaertilskuddGrunnlag, sjablontallMap, sjablonListe, soknadsBarnInfo.getId());
  }

  //  Validerer at det kun er oppgitt ett SoknadsbarnInfo-grunnlag og at mapping til SoknadsBarnInfo objekt ikke feiler
  protected static SoknadsBarnInfo validerSoknadsbarnId(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    List<Grunnlag> soknadsbarnInfoGrunnlag = beregnTotalSaertilskuddGrunnlag.getGrunnlagListe().stream()
        .filter(grunnlag -> grunnlag.getType().equals(GrunnlagType.SOKNADSBARN_INFO)).toList();
    if (soknadsbarnInfoGrunnlag.size() != 1) {
      throw new UgyldigInputException("Det skal kun være ett soknadsbarn i beregningsgrunnlaget");
    }
    return grunnlagTilObjekt(soknadsbarnInfoGrunnlag.get(0), SoknadsBarnInfo.class);
  }

  //==================================================================================================================================================

  // Bygger grunnlag til core og kaller delberegninger
  private HttpResponse<BeregnTotalSaertilskuddResultat> utfoerDelberegninger(BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag,
      Map<String, SjablonTallNavn> sjablontallMap, SjablonListe sjablonListe, Integer soknadsBarnId) {

    // ++ Bidragsevne
    var bidragsevneGrunnlagTilCore = bidragsevneCoreMapper.mapBidragsevneGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablontallMap,
        sjablonListe);
    var bidragsevneResultatFraCore = beregnBidragsevne(bidragsevneGrunnlagTilCore);

    // ++ BPs andel av særtilskudd
    var bpAndelSaertilskuddGrunnlagTilCore = bpAndelSaertilskuddCoreMapper.mapBPsAndelSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag,
        sjablontallMap, sjablonListe);
    var bpAndelSaertilskuddResultatFraCore = beregnBPAndelSaertilskudd(bpAndelSaertilskuddGrunnlagTilCore);

    // ++ Samværsfradrag
    var samvaersfradragGrunnlagTilCore = samvaersfradragCoreMapper.mapSamvaersfradragGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag, sjablonListe);
    var samvaersfradragResultatFraCore = beregnSamvaersfradrag(samvaersfradragGrunnlagTilCore);

    // ++ Særtilskudd (totalberegning)
    var saertilskuddGrunnlagTilCore = saertilskuddCoreMapper.mapSaertilskuddGrunnlagTilCore(beregnTotalSaertilskuddGrunnlag,
        bidragsevneResultatFraCore,
        bpAndelSaertilskuddResultatFraCore, samvaersfradragResultatFraCore, soknadsBarnId);
    var saertilskuddResultatFraCore = beregnSaertilskudd(saertilskuddGrunnlagTilCore);

    // Bygger responsobjekt
    return HttpResponse.from(HttpStatus.OK, new BeregnTotalSaertilskuddResultat(
        new BeregnBPBidragsevneResultat(bidragsevneResultatFraCore),
        new BeregnBPAndelSaertilskuddResultat(bpAndelSaertilskuddResultatFraCore),
        new BeregnBPSamvaersfradragResultat(samvaersfradragResultatFraCore),
        new BeregnSaertilskuddResultat(saertilskuddResultatFraCore)));
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
}
