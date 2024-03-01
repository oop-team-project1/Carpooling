package com.company.carpooling.services;

import com.company.carpooling.models.AddressDto;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.TripDto;
import com.company.carpooling.models.json.Point;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



@Service
@PropertySource("classpath:application.properties")
public class BingMapsService {
    private final WebClient webClient;
    private final String key;

    @Autowired
    public BingMapsService(WebClient.Builder builder, Environment environment) {
        webClient = builder.build();
        key = environment.getProperty("bingmapkey");
    }

    public Point getCoordinates(AddressDto addressDto) {
        String uri = String.format("Locations?countryRegion=%s&locality=%s&addressLine=%s&key=%s&maxResults=1", addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), key);
        String json = webClient.get()
                .uri(uri).
                retrieve().
                bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode locatedNode = rootNode.path("resourceSets").get(0).path("resources").get(0).path("point");

            return mapper.convertValue(locatedNode, Point.class);
        } catch (JsonProcessingException e) {
            //TODO figure out what to throw instead
        }
        // TODO think of Null pointer exceptions down the line
        return null;
    }

    public void setDistanceAndDuration(TripDto tripDto, Trip trip) {
        Point coordinateStart = getCoordinates(tripDto.getStartPoint());
        Point coordinateEnd = getCoordinates(tripDto.getEndPoint());
        String uri = String.format("Routes/DistanceMatrix?origins=%s&destinations=%s&travelMode=driving&key=%s", coordinateStart, coordinateEnd.toString(), key);
        System.out.println(uri);
        String json = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode locatedDistance = rootNode.path("resourceSets").get(0).path("resources").get(0).path("results").get(0).path("travelDistance");
            trip.setDistance(mapper.convertValue(locatedDistance, Float.class));
            //TODO make trip have duration
            //JsonNode locatedDuration = rootNode.path("resourceSets").get(0).path("resources").get(0).path("results").get(0).path("travelDuration");

        } catch (JsonProcessingException e) {
            //TODO figure out what to throw instead
        }
        // TODO think of Null pointer exceptions down the line


    }
}
