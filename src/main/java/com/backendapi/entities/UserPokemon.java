package com.backendapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_pokemon")
public class UserPokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pokemon")
    private long idPokemon;
    private String name;
    private String nature;
    private String status;
    @Column(name = "max_life")
    private long maxLife;
    @Column(name = "current_life")
    private long currentLife;
    @Column(name = "original_trainer_id")
    private long originalTrainer;
    @Column(name = "current_experience")
    private long currentExperience;
    @Column(name = "max_experience")
    private long maxExperience;
    private String item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Users user;
}
