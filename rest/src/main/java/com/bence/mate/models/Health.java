package com.bence.mate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Health implements Serializable {

    @JsonIgnore
    public static final String DOWN = "DOWN";

    @JsonIgnore
    public static final String UP = "UP";

    @Getter
    @Setter
    private String status;
}
