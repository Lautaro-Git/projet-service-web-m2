package com.example.projetServiceWeb.controller;

import com.example.projetServiceWeb.dto.ErreurDto;
import com.example.projetServiceWeb.dto.RestaurantDto;
import com.example.projetServiceWeb.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;

//Pour catch les erreurs et envoyer un code et un message en fonction du probleme (
@ControllerAdvice
public class ExceptionHandlerCustom {

    //erreur 404
    @ExceptionHandler
    public ResponseEntity<ErreurDto> handleException(NotFoundException exception) {
        return ResponseEntity.status(404)
                .body(new ErreurDto(404, exception.getMessage()));

    }

    //erreur 400 : non respect des conditions de taille de string par exemple
    @ExceptionHandler
    public ResponseEntity<ErreurDto> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(400)
                .body(new ErreurDto(400,"Données ivalide"));
    }

    //erreur 403 : pour des erreurs de roles des utilisateurs
    @ExceptionHandler
    public ResponseEntity<ErreurDto> handleException(AccessDeniedException exception) {
        return ResponseEntity.status(403)
                .body(new ErreurDto(403, "Acces non authorisé"));
    }

    //erreur 500 : pour erreurs autres
    @ExceptionHandler
    public ResponseEntity<ErreurDto> handleException(Exception exception) {
        return ResponseEntity.status(500)
                .body(new ErreurDto(500,"Erreur pour le serveur"));
    }
}
