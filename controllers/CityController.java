package com.exercise.candoit.controllers;

import com.exercise.candoit.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update")
    public ResponseEntity<String> updateCityWeatherData() {
        cityService.updateCityWeatherData();
        return new ResponseEntity<>("City weather data updated successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/update5")
    public ResponseEntity<String> startUpdatingWeatherData() {
        cityService.startUpdatingWeatherData();
        return new ResponseEntity<>("City weather data update scheduled.", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/temp")
    public ResponseEntity<List<Map<String, Object>>> getCityWithLatestTemperature() {
        List<Map<String, Object>> citiesWithLatestTemp = cityService.getAllCitiesWithLatestTemperature();
        return new ResponseEntity<>(citiesWithLatestTemp, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ', 'ADMIN')")
    @GetMapping("/province")
    public ResponseEntity<List<Map<String, Object>>> getAllCitiesNameAndProvince() {
        List<Map<String, Object>> citiesNameAndProvince = cityService.getAllCitiesNameAndProvince();
        return new ResponseEntity<>(citiesNameAndProvince, HttpStatus.OK);
    }


}
