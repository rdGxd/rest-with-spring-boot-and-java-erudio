package org.example.restwithspringbootandjavaerudio.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Setter
@Getter
@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender", "enabled"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "key", callSuper = false)
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {

    @JsonProperty("id")
    private Long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private boolean enabled;
}
