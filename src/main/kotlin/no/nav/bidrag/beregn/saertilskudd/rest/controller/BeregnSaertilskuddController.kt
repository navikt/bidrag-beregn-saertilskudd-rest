package no.nav.bidrag.beregn.saertilskudd.rest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import no.nav.bidrag.beregn.saertilskudd.rest.service.BeregnSaertilskuddService
import no.nav.bidrag.transport.beregning.felles.BeregnGrunnlag
import no.nav.bidrag.transport.beregning.saertilskudd.BeregnetTotalSaertilskuddResultat
import no.nav.security.token.support.core.api.Protected
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/beregn")
@Protected
class BeregnSaertilskuddController(private val beregnSaertilskuddService: BeregnSaertilskuddService) {
    @PostMapping(path = ["/saertilskudd"])
    @Operation(summary = "Beregner saertilskudd")
    @SecurityRequirement(name = "bearer-key")
    fun beregnTotalSaertilskudd(@RequestBody beregnGrunnlag: BeregnGrunnlag): ResponseEntity<BeregnetTotalSaertilskuddResultat> {
        val beregnTotalSaertilskuddResultat = beregnSaertilskuddService.beregn(beregnGrunnlag)
        return ResponseEntity(
            beregnTotalSaertilskuddResultat.responseEntity.body,
            beregnTotalSaertilskuddResultat.responseEntity.statusCode,
        )
    }
}
