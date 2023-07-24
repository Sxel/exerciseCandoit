package com.exercise.candoit.model;

import jakarta.persistence.*;

@Entity(name = "ciudades")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String province;
    private Double lat;
    private Double lon;

    @Embedded
    private WeatherInfo weather;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }


    // Nested class for weather information
    @Embeddable
    public static class WeatherInfo {
        @Column(name = "humidity")
        private Integer humidity;

        @Column(name = "pressure")
        private Double pressure;

        @Column(name = "visibility")
        private Integer visibility;

        @Column(name = "wind_speed")
        private Integer wind_speed;

        @Column(name = "weather_id")
        private Integer id; // Renamed to "id" to match the JSON field

        @Column(name = "weather_description")
        private String description;

        @Column(name = "temperature")
        private Integer temp; // Renamed to "temp" to match the JSON field

        @Column(name = "wing_deg")
        private String wing_deg;

        @Column(name = "temperature_description")
        private String tempDesc; // Renamed to "tempDesc" to match the JSON field


        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

        public Double getPressure() {
            return pressure;
        }

        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }

        public Integer getVisibility() {
            return visibility;
        }

        public void setVisibility(Integer visibility) {
            this.visibility = visibility;
        }

        public Integer getWind_speed() {
            return wind_speed;
        }

        public void setWind_speed(Integer wind_speed) {
            this.wind_speed = wind_speed;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getTemp() {
            return temp;
        }

        public void setTemp(Integer temp) {
            this.temp = temp;
        }

        public String getWing_deg() {
            return wing_deg;
        }

        public void setWing_deg(String wing_deg) {
            this.wing_deg = wing_deg;
        }

        public String getTempDesc() {
            return tempDesc;
        }

        public void setTempDesc(String tempDesc) {
            this.tempDesc = tempDesc;
        }
    }


    public WeatherInfo getWeather() {
        return weather;
    }

    public void setWeather(WeatherInfo weather) {
        this.weather = weather;
    }
}
