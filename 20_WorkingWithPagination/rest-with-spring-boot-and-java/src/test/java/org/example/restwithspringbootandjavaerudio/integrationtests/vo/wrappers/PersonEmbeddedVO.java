package org.example.restwithspringbootandjavaerudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.PersonVO;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "persons")
public class PersonEmbeddedVO implements Serializable {

    @JsonProperty("personVOList")
    private List<PersonVO> persons;
}
