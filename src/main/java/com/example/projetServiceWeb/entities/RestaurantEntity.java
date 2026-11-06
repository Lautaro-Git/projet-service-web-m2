package com.example.projetServiceWeb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="restaurant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {
    @Id
    @GeneratedValue
    @Column(name="id")
    @Getter @Setter
    private Long id;

    @NotBlank
    @Column(name="name", length = 90)
    @Getter @Setter
    private String name;

    @NotBlank
    @Column(name="adress", length = 255)
    @Getter @Setter
    private String adress;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)

    @Getter  @Setter
    @Builder.Default
    private Collection<EvaluationEntity> evaluations = new ArrayList<>();
}
