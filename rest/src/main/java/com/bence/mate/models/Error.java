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
public class Error implements Serializable {

    @Getter
    @Setter
    private String errorMessage;

    @Getter
    @Setter
    private int errCode;


    @Getter
    @Setter
    private String documentation;
}
