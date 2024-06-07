package org.example.chatgptspringbootjavaintegration.vo.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Message implements Serializable {

    private String role;
    private String content; // prompt


}
