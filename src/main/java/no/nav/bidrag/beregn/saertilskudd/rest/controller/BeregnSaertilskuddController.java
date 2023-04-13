package no.nav.bidrag.beregn.saertilskudd.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnTotalSaertilskuddGrunnlag;
import no.nav.bidrag.beregn.saertilskudd.rest.dto.http.BeregnetTotalSaertilskuddResultat;
import no.nav.bidrag.beregn.saertilskudd.rest.service.BeregnSaertilskuddService;
import no.nav.security.token.support.core.api.Protected;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beregn")
@Protected
public class BeregnSaertilskuddController {

  private final BeregnSaertilskuddService beregnSaertilskuddService;

  public BeregnSaertilskuddController(BeregnSaertilskuddService beregnSaertilskuddService) {
    this.beregnSaertilskuddService = beregnSaertilskuddService;
  }

  @PostMapping(path = "/saertilskudd")
  @Operation(summary = "Beregner saertilskudd")
  @SecurityRequirement(name = "bearer-key")
  public ResponseEntity<BeregnetTotalSaertilskuddResultat> beregnTotalSaertilskudd(
      @RequestBody BeregnTotalSaertilskuddGrunnlag beregnTotalSaertilskuddGrunnlag) {
    var beregnTotalSaertilskuddResultat = beregnSaertilskuddService.beregn(beregnTotalSaertilskuddGrunnlag);
    return new ResponseEntity<>(beregnTotalSaertilskuddResultat.getResponseEntity().getBody(),
        beregnTotalSaertilskuddResultat.getResponseEntity().getStatusCode());
  }
}
