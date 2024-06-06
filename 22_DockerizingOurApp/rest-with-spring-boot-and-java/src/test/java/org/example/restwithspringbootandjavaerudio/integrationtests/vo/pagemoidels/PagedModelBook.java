package org.example.restwithspringbootandjavaerudio.integrationtests.vo.pagemoidels;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.BookVO;

import java.util.List;

@XmlRootElement
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "content")
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookVO> content;
}
