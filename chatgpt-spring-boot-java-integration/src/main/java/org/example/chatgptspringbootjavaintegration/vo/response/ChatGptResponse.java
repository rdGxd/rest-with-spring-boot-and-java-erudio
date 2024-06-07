package org.example.chatgptspringbootjavaintegration.vo.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class ChatGptResponse implements Serializable {
    private List<Choice> choices;
}
