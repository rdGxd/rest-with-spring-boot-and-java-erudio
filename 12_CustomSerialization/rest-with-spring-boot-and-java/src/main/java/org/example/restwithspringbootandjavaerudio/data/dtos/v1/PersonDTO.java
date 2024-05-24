package org.example.restwithspringbootandjavaerudio.data.dtos.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder({"id", "address", "first_name", "last_name", "gender"})
public record PersonDTO(Long id, @JsonProperty("first_name") String firstName,
                        @JsonProperty("last_name") String lastName, String address,
                        @JsonIgnore String gender) implements Serializable {
}
