package com.company.carpooling.helpers;

import com.company.carpooling.models.dtos.PointDto;
import com.company.carpooling.models.json.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class PointMapper {

    public Point fromDto(PointDto dto) {
        String trimmed = dto.getCoordinates().replaceAll("[ Map()Loction\\[\\]]", "");
        Point point = new Point();
        List<Double> coordinates = new ArrayList<>();
        String[] coords = trimmed.split(",");
        coordinates.add(Double.parseDouble(coords[0]));
        coordinates.add(Double.parseDouble(coords[1]));
        point.setCoordinates(coordinates);
        return point;

    }

}
