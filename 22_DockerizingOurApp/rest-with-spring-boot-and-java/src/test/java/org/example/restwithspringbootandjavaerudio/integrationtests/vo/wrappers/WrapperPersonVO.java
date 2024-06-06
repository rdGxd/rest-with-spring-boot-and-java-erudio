package org.example.restwithspringbootandjavaerudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "embedded")
public class WrapperPersonVO implements Serializable {

    @JsonProperty("_embedded")
    private PersonEmbeddedVO embedded;
}
