package org.example.restwithspringbootandjavaerudio.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 100) // O nome da coluna vai ser igual no banco e no objeto java
    private String address;

    @Column(nullable = false, length = 6) // O nome da coluna vai ser igual no banco e no objeto java
    private String gender;

    @Column(nullable = false)
    private boolean enabled;

}
