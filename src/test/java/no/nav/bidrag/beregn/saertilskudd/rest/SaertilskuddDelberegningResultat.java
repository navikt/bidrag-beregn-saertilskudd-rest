package no.nav.bidrag.beregn.saertilskudd.rest;

import java.util.ArrayList;
import java.util.List;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BPsAndelSaertilskudd;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Bidragsevne;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Grunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.ResultatPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.Samvaersfradrag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.SjablonPeriode;
import no.nav.bidrag.beregn.saertilskudd.rest.mapper.CoreMapper;

public class SaertilskuddDelberegningResultat {

  public List<Bidragsevne> bidragsevneListe = new ArrayList<>();
  public List<BPsAndelSaertilskudd> bpsAndelSaertilskuddListe = new ArrayList<>();
  public List<Samvaersfradrag> samvaersfradragListe = new ArrayList<>();
  public List<SjablonPeriode> sjablonPeriodeListe = new ArrayList<>();

  public SaertilskuddDelberegningResultat(BeregnetTotalSaertilskuddResultat beregnetTotalSaertilskuddResultat) {
    for (ResultatPeriode resultatPeriode : beregnetTotalSaertilskuddResultat.getBeregnetSaertilskuddPeriodeListe()) {
      for (String referanse : resultatPeriode.getGrunnlagReferanseListe()) {
        Grunnlag resultatGrunnlag = beregnetTotalSaertilskuddResultat.getGrunnlagListe().stream()
            .filter(grunnlag -> grunnlag.getReferanse().equals(referanse)).findFirst().orElse(null);
        if (resultatGrunnlag != null) {
          switch (resultatGrunnlag.getType()) {
            case BIDRAGSEVNE:
              bidragsevneListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, Bidragsevne.class));
              break;
            case BPSANDELSAERTILSKUDD:
              bpsAndelSaertilskuddListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, BPsAndelSaertilskudd.class));
              break;
            case SAMVAERSFRADRAG:
              samvaersfradragListe.add(
                  CoreMapper.grunnlagTilObjekt(resultatGrunnlag, Samvaersfradrag.class));
              break;
            case SJABLON:
              sjablonPeriodeListe.add(CoreMapper.grunnlagTilObjekt(resultatGrunnlag, SjablonPeriode.class));
            default:
              break;
          }
        }
      }
    }
  }
}
