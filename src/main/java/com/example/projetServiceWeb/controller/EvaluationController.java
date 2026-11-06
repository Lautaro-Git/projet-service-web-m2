package com.example.projetServiceWeb.controller;

import com.example.projetServiceWeb.dto.EvaluationDto;
import com.example.projetServiceWeb.dto.ImagePutDto;
import com.example.projetServiceWeb.dto.evaluations.AddEvaluationDto;
import com.example.projetServiceWeb.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/restaurants/{restaurantId}/evaluations")
    public ResponseEntity<EvaluationDto> addEvaluation(@PathVariable Long restaurantId, @Valid @RequestBody AddEvaluationDto evaluation, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String evaluateur = jwt.getClaimAsString("name");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EvaluationDto.fromEntity(this.evaluationService.addEvaluation(
                        restaurantId,evaluateur,evaluation.getCommentaire(),evaluation.getNote()
                )));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/restaurants/{restaurantId}/evaluations/{evaluationsId}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long restaurantId, @PathVariable Long evaluationsId, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("name");
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        this.evaluationService.deleteEvaluation(restaurantId, evaluationsId, username, isAdmin);
        return ResponseEntity.noContent().build();

    }


    @GetMapping("/evaluations")
    public List<EvaluationDto> rechercheEvaluations(@RequestParam String motsCle) {
        return this.evaluationService.rechercheEvaluations(motsCle).stream()
                .map(EvaluationDto::fromEntity)
                .toList();
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/restaurants/{restaurantId}/evaluations/{evaluationId}/photos/{nomPhoto}")
    public ImagePutDto getPhotoUploadUrl(@PathVariable Long restaurantId, @PathVariable Long evaluationId,
                                         @PathVariable String nomPhoto, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username =jwt.getClaimAsString("name");
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return new ImagePutDto(this.evaluationService.getPhotoUploadUrl(restaurantId, evaluationId, nomPhoto, username, isAdmin));
    }



    @GetMapping("/restaurants/{restaurantId}/evaluations/{evaluationId}/photos/{nomPhoto}")
    public ResponseEntity<Void> getPhoto(@PathVariable Long restaurantId, @PathVariable Long evaluationId, @PathVariable String nomPhoto) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(this.evaluationService.getPhotoUrl(restaurantId, evaluationId, nomPhoto) ))
                .build();


    }

}
