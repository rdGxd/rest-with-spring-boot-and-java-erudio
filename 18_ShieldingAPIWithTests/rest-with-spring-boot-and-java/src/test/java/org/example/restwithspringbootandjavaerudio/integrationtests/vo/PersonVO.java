package org.example.restwithspringbootandjavaerudio.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class PersonVO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
