package org.example.restwithspringbootandjavaerudio.data.dtos.v2;

import java.io.Serializable;
import java.util.Date;

public record PersonDTOV2(Long id, String firstName, String lastName, String address,
                          String gender, Date birthDate) implements Serializable {

}
