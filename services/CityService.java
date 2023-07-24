package com.exercise.candoit.services;

import com.exercise.candoit.repositories.CityRepository;
import com.exercise.candoit.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class CityService {

    private final WebClient webClient;
    private final CityRepository cityRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);


    @Autowired
    public CityService(WebClient.Builder webClientBuilder, CityRepository cityRepository) {
        this.webClient = webClientBuilder.baseUrl("https://ws.smn.gob.ar").build();
        this.cityRepository = cityRepository;
    }


    public void updateCityWeatherData() {
        try {
            // Hacer la solicitud HTTP a la API externa para obtener los datos climáticos
            City[] cities = webClient.get()
                    .uri("/map_items/weather")
                    .retrieve()
                    .bodyToMono(City[].class)
                    .block();

            if (cities != null) {
                for (City city : cities) {
                    // Verificar si la ciudad ya existe en la base de datos
                    City existingCity = cityRepository.findByName(city.getName());
                    if (existingCity != null) {
                        // Actualizar los datos climáticos de la ciudad existente
                        City.WeatherInfo weatherInfo = existingCity.getWeather();
                        if (weatherInfo == null) {
                            weatherInfo = new City.WeatherInfo();
                            existingCity.setWeather(weatherInfo);
                        }
                        weatherInfo.setHumidity(city.getWeather().getHumidity());
                        weatherInfo.setPressure(city.getWeather().getPressure());
                        weatherInfo.setVisibility(city.getWeather().getVisibility());
                        weatherInfo.setWind_speed(city.getWeather().getWind_speed());
                        weatherInfo.setId(city.getWeather().getId());
                        weatherInfo.setDescription(city.getWeather().getDescription());
                        weatherInfo.setTemp(city.getWeather().getTemp());
                        weatherInfo.setWing_deg(city.getWeather().getWing_deg()); // This line should work now
                        weatherInfo.setTempDesc(city.getWeather().getTempDesc());

                        cityRepository.save(existingCity);
                    } else {
                        // Guardar una nueva ciudad en la base de datos
                        cityRepository.save(city);
                    }
                }
                System.out.println("Datos climáticos de las ciudades actualizados con éxito.");
            }
        } catch (Exception e) {
            // Capturar cualquier excepción que pueda ocurrir durante el proceso y registrar los detalles.
            System.err.println("Error al actualizar los datos climáticos de las ciudades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startUpdatingWeatherData() {
        LOGGER.info("Iniciando la tarea de actualización de datos climáticos.");

        // Crear un ScheduledExecutorService con un único hilo
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Programar la tarea de actualización para que se ejecute cada 5 minutos
        scheduler.scheduleAtFixedRate(() -> {
            LOGGER.info("Ejecutando la tarea de actualización de datos climáticos.");
            updateCityWeatherData();
        }, 0, 5, TimeUnit.MINUTES);
    }

    public List<Map<String, Object>> getAllCitiesWithLatestTemperature() {
        List<City> allCities = cityRepository.findAll();
        List<Map<String, Object>> citiesWithLatestTemp = new ArrayList<>();

        // Ordenar la lista de ciudades en memoria en función de la última temperatura registrada
        allCities.sort(Comparator.comparing(city -> city.getWeather().getTemp(), Comparator.nullsLast(Comparator.reverseOrder())));

        for (City city : allCities) {
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("name", city.getName());
            cityData.put("province", city.getProvince());
            cityData.put("weather", city.getWeather().getDescription());
            cityData.put("humidity", city.getWeather().getHumidity());
            cityData.put("pressure", city.getWeather().getPressure());
            cityData.put("visibility", city.getWeather().getVisibility());
            cityData.put("wind_speed", city.getWeather().getWind_speed());
            cityData.put("tempDesc", city.getWeather().getTempDesc());
            cityData.put("wing_deg", city.getWeather().getWing_deg());

            citiesWithLatestTemp.add(cityData);
        }

        return citiesWithLatestTemp;
    }

    public List<Map<String, Object>> getAllCitiesNameAndProvince() {
        List<City> allCities = cityRepository.findAll();
        List<Map<String, Object>> citiesNameAndProvince = new ArrayList<>();

        for (City city : allCities) {
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("name", city.getName());
            cityData.put("province", city.getProvince());

            citiesNameAndProvince.add(cityData);
        }

        return citiesNameAndProvince;
    }


    private String encodeCityName(String cityName) throws UnsupportedEncodingException {
        return URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
    }
}
