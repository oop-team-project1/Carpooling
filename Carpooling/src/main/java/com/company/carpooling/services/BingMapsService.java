package com.company.carpooling.services;

import com.company.carpooling.models.dtos.AddressDto;
import com.company.carpooling.models.Trip;
import com.company.carpooling.models.dtos.TripDto;
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
    public static final String URI_GET_LOCATIONS = "Locations?countryRegion=%s&locality=%s&addressLine=%s&key=%s&maxResults=1";
    public static final String URI_DISTANCE_DURATION = "Routes/DistanceMatrix?origins=%s&destinations=%s&travelMode=driving&key=%s";
    public static final String REVERSE_GEOCODE= "http://dev.virtualearth.net/REST/v1/Locations/%s,%s?key=%s";
    private final WebClient webClient;
    private final String key;

    @Autowired
    public BingMapsService(WebClient.Builder builder, Environment environment) {
        webClient = builder.build();
        key = environment.getProperty("bingmapkey");
    }
    public AddressDto getAddress(Point point){
        String uri = String.format(REVERSE_GEOCODE,point.getCoordinates().get(0),point.getCoordinates().get(1),key);
        String json = webClient.get()
                .uri(uri).
                retrieve().
                bodyToMono(String.class)
                .block();
        AddressDto verifiedAddress = new AddressDto();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode street = rootNode.path("resourceSets").get(0).path("resources").get(0).path("address").path("addressLine");
            JsonNode city = rootNode.path("resourceSets").get(0).path("resources").get(0).path("address").path("adminDistrict2");
            JsonNode country = rootNode.path("resourceSets").get(0).path("resources").get(0).path("address").path("countryRegion");

            verifiedAddress.setStreet(mapper.convertValue(street,String.class));
            verifiedAddress.setCity(mapper.convertValue(city,String.class));
            verifiedAddress.setCountry(mapper.convertValue(country,String.class));
            return verifiedAddress;
        } catch (JsonProcessingException e) {
            //TODO figure out what to throw instead
        }
        // TODO think of Null pointer exceptions down the line
        return null;
    }

    public Point getCoordinates(AddressDto addressDto) {
        String uri = String.format(URI_GET_LOCATIONS, addressDto.getCountry(), addressDto.getCity(), addressDto.getStreet(), key);
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
        String uri = String.format(URI_DISTANCE_DURATION, coordinateStart, coordinateEnd.toString(), key);
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

            JsonNode locatedDuration = rootNode.path("resourceSets").get(0).path("resources").get(0).path("results").get(0).path("travelDuration");
            trip.setDuration(mapper.convertValue(locatedDuration, Float.class));
        } catch (JsonProcessingException e) {
            //TODO figure out what to throw instead
        }
        // TODO think of Null pointer exceptions down the line


    }
}

