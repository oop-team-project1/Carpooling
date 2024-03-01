package com.company.carpooling.models.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Point {
    @JsonUnwrapped
    @JsonProperty("coordinates")
    private List<Double> coordinates;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(coordinates.get(0));
        stringBuilder.append(",");
        stringBuilder.append(coordinates.get(1));
        return stringBuilder.toString();

    }
}
