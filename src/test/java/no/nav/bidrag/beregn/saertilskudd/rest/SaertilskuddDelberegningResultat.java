package no.nav.bidrag.beregn.saertilskudd.rest;

import java.util.ArrayList;
import java.util.List;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPsAndelSaertilskuddResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BidragsevneResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SamvaersfradragResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SjablonResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.CoreMapper;

public class SaertilskuddDelberegningResultat {

  public List<BidragsevneResultatPeriode> bidragsevneListe = new ArrayList<>();
  public List<BPsAndelSaertilskuddResultatPeriode> bpsAndelSaertilskuddListe = new ArrayList<>();
  public List<SamvaersfradragResultatPeriode> samvaersfradragListe = new ArrayList<>();
  public List<SjablonResultatPeriode> sjablonPeriodeListe = new ArrayList<>();

  public SaertilskuddDelberegningResultat(BeregnetTotalSaertilskuddResultat beregnetTotalSaertilskuddResultat) {
    for (ResultatPeriode resultatPeriode : beregnetTotalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe()) {
      for (String referanse : resultatPeriode.getGrunnlagReferanseListe()) {
        Grunnlag resultatGrunnlag = beregnetTotalSaertilskuddResultat.getGrunnlagListe().stream()
            .filter(grunnlag -> grunnlag.getReferanse().equals(referanse)).findFirst().orElse(null);
        if (resultatGrunnlag != null) {
          switch (resultatGrunnlag.getType()) {
            case BIDRAGSEVNE:
              bidragsevneListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, BidragsevneResultatPeriode.class));
              break;
            case BPSANDELSAERTILSKUDD:
              bpsAndelSaertilskuddListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, BPsAndelSaertilskuddResultatPeriode.class));
              break;
            case SAMVAERSFRADRAG:
              samvaersfradragListe.add(
                  CoreMapper.grunnlagTilObjekt(resultatGrunnlag, SamvaersfradragResultatPeriode.class));
              break;
            case SJABLON:
              sjablonPeriodeListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, SjablonResultatPeriode.class));
            default:
              break;
          }
        }
      }
    }
  }
}
