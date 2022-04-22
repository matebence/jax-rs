package com.bence.mate.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@NoArgsConstructor
@AllArgsConstructor
public class MessageFilterParam {

    @Getter
    @Setter
    @QueryParam("year")
    @DefaultValue("2021")
    private int year;

    @Override
    public String toString() {
        return String.format("%d", year);
    }
}
