package org.example.restwithspringbootandjavaerudio.data.vo.v1;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "key", callSuper = false)
@JsonPropertyOrder({"id", "author", "title", "price", "launch_date"})
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

    @JsonProperty("id")
    private Long key;
    private String author;
    private Date launch_date = new Date();
    private Double price;
    private String title;

}

