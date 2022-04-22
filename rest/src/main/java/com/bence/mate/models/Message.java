package com.bence.mate.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Builder
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class Message extends Page implements Serializable {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Integer year;
}
