package com.example.projetServiceWeb.entities;

import com.example.projetServiceWeb.dto.RestaurantDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "evaluation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationEntity {
    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;

    @NotBlank
    @Column(name = "evaluateur", length = 50)
    @Getter @Setter
    private String evaluateur;

    @NotBlank
    @Column(name = "commentaire", length = 255)
    @Getter @Setter
    private String commentaire;

    //@NotBlank
    @Column(name = "note")
    @Min(value = 0)
    @Max(value = 3)
    @Getter @Setter
    private int note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    @Getter @Setter
    private RestaurantEntity restaurant;

}
