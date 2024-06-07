package org.example.chatgptspringbootjavaintegration.vo.response;

import lombok.*;
import org.example.chatgptspringbootjavaintegration.vo.request.Message;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Choice implements Serializable {
    private int index;
    private Message message;
}
