package com.example.projetServiceWeb.controller;

import com.example.projetServiceWeb.dto.EvaluationDto;
import com.example.projetServiceWeb.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final EvaluationService evaluationService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/evaluations")
    public List<EvaluationDto> getEvaluationsUser(Authentication authentication)
    {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String evaluateur = jwt.getClaimAsString("name");
        return this.evaluationService.getAllEvaluationsByEvaluateur(evaluateur).stream()
                .map(EvaluationDto::fromEntity)
                .toList();

    }
}
