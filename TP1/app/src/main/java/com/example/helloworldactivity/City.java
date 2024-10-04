package com.example.helloworldactivity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.Math;
import java.util.*;

public class City implements Serializable {
    private String name;
    private float latitude;
    private float longitude;
    private int population;
    private float elevation;
    private TimeZone timeZone;

    public City(String name, float latitude, float longitude, int population, float elevation, TimeZone timeZone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
        this.elevation = elevation;
        this.timeZone = timeZone;

    }

    public static City loadFromLine(String line) {
        String[] c = line.split("\t");
        return new City(c[0] + " " + c[3], Float.parseFloat(c[1]), Float.parseFloat(c[2]), Integer.parseInt(c[4]), Float.parseFloat(c[5]), TimeZone.getTimeZone(c[6]));
    }

    public static List<City> loadFromAsset(Context context, int id) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(id)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() > 0) {
                    cities.add(loadFromLine(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static final double R = 6372.8; // in kilometers
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double λ1 = Math.toRadians(lat1);
        double λ2 = Math.toRadians(lat2);
        double Δλ = Math.toRadians(lat2 - lat1);
        double Δφ = Math.toRadians(lon2 - lon1);
        return 2 * R * Math.asin(Math.sqrt(Math.pow(Math.sin(Δλ / 2), 2.0) + Math.pow(Math.sin(Δφ / 2), 2.0) * Math.cos(λ1) * Math.cos(λ2)));
    }

    public static City findNearest(List<City> cities, float latitude, float longitude) {
        return Collections.min(cities, (c1, c2) -> Double.compare(haversine(latitude, longitude, c1.latitude, c1.longitude), haversine(latitude, longitude, c2.latitude, c2.longitude)));
    }

    @Override
    public String toString() {
        return name;
    }
}