package org.example.restwithspringbootandjavaerudio.data.vo.v1;

import java.io.Serializable;

public record PersonDTO(Long id, String firstName, String lastName, String address,
                        String gender) implements Serializable {

}
