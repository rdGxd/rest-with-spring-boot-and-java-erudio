package org.example.restwithspringbootandjavaerudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.BookVO;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "books")
public class BookEmbeddedVO implements Serializable {

    @JsonProperty("bookVOList")
    private List<BookVO> books;
}
