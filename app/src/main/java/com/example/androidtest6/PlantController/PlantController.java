package com.example.androidtest6.PlantController;

import android.util.Log;

import com.example.androidtest6.MainActivity;
import com.example.androidtest6.influxDB.InfluxDBClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlantController {

    private InfluxDBClient client;
    public MainActivity activity;

    public PlantController(InfluxDBClient client, MainActivity activity) {
        this.client = client;
        this.activity = activity;
    }
    public PlantController(){

    }

    public void fetchTemperature() {
        client.fetchData("temperature", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("InfluxDB Response", responseData);
                    String value = parseTemperature(responseData);
                    activity.updateTemperatureUI(value);
                } else {
                    Log.e("InfluxDB", "Request failed with code: " + response.code());
                }
            }
        });
    }

    public void fetchHumidity() {
        client.fetchData("humidity", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("InfluxDB Response", responseData);
                    String value = parseHumidity(responseData);
                    activity.updateHumidityUI(value);
                } else {
                    Log.e("InfluxDB", "Request failed with code: " + response.code());
                }
            }
        });
    }

    public void fetchMoisture() {
        client.fetchData("soil_moisture", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("InfluxDB Response", responseData);
                    String value = parseMoisture(responseData);
                    activity.updateMoistureUI(value);
                } else {
                    Log.e("InfluxDB", "Request failed with code: " + response.code());
                }
            }
        });
    }

    public String parseTemperature(String responseData) {
        try {
            JsonObject jsonObject = parseLineProtocolToJson(responseData);
            JsonArray results = jsonObject.getAsJsonArray("results");
            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                if (firstResult.has("series")) {
                    JsonArray seriesArray = firstResult.getAsJsonArray("series");
                    if (seriesArray.size() > 0) {
                        JsonObject seriesObject = seriesArray.get(0).getAsJsonObject();
                        if (seriesObject.has("values")) {
                            JsonArray valuesArray = seriesObject.getAsJsonArray("values");
                            if (valuesArray.size() > 0) {
                                JsonArray latestValue = valuesArray.get(valuesArray.size() - 1).getAsJsonArray();
                                String value = latestValue.get(latestValue.size() - 3).getAsString(); // Vorletzter Wert in latestValue
                                int check;
                                try {
                                    check = Integer.parseInt(value);
                                }catch(Exception e)
                                { return "Fehler";
                                }

                                //Werte des Angegebenen Sensors
                                if (check < 50 && check > 0){
                                    return value;
                                }
                                else {
                                    return "Fehler";
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("InfluxDB", "Error parsing JSON response", e);
        }
        return "Fehler";
    }

    public String parseHumidity(String responseData) {
        try {
            JsonObject jsonObject = parseLineProtocolToJson(responseData);
            JsonArray results = jsonObject.getAsJsonArray("results");
            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                if (firstResult.has("series")) {
                    JsonArray seriesArray = firstResult.getAsJsonArray("series");
                    if (seriesArray.size() > 0) {
                        JsonObject seriesObject = seriesArray.get(0).getAsJsonObject();
                        if (seriesObject.has("values")) {
                            JsonArray valuesArray = seriesObject.getAsJsonArray("values");
                            if (valuesArray.size() > 0) {
                                JsonArray latestValue = valuesArray.get(valuesArray.size() - 1).getAsJsonArray();
                                String value = latestValue.get(latestValue.size() - 3).getAsString(); // Vorletzter Wert in latestValue

                                int check;
                                try {
                                    check = Integer.parseInt(value);
                                }catch(Exception e)
                                { return "Fehler";
                                }
                                if (check < 90 && check > 20){
                                    return value;
                                }
                                else {
                                    return "Fehler";
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("InfluxDB", "Error parsing JSON response", e);
        }
        return "Fehler";
    }

    public String parseMoisture(String responseData) {
        try {
            JsonObject jsonObject = parseLineProtocolToJson(responseData);
            JsonArray results = jsonObject.getAsJsonArray("results");
            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                if (firstResult.has("series")) {
                    JsonArray seriesArray = firstResult.getAsJsonArray("series");
                    if (seriesArray.size() > 0) {
                        JsonObject seriesObject = seriesArray.get(0).getAsJsonObject();
                        if (seriesObject.has("values")) {
                            JsonArray valuesArray = seriesObject.getAsJsonArray("values");
                            if (valuesArray.size() > 0) {
                                JsonArray latestValue = valuesArray.get(valuesArray.size() - 1).getAsJsonArray();
                                String value = latestValue.get(latestValue.size() - 3).getAsString(); // Vorletzter Wert in latestValue

                                int check;
                                try {
                                    check = Integer.parseInt(value);
                                }catch(Exception e)
                                { return "Fehler";
                                }

                                if (check > 300 && check < 800){
                                    return moistureIntoPercent(value);
                                }
                                else {
                                    return "Fehler";
                                }
                            }


                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("InfluxDB", "Error parsing JSON response", e);
        }
        return "Fehler";
    }

    public String moistureIntoPercent(String moisture) {
        int value = Integer.parseInt(moisture);

        int minWert = 499;  // Höchste Feuchtigkeit //TODO CHECK SINN
        int maxWert = 685;  // Niedrigste Feuchtigkeit

        if (value == 0) {
            return "Sensor nicht verfügbar";
        }

        if (value > maxWert) {
            value = maxWert;
        } else if (value < minWert) {
            value = minWert;
        }
        Integer erg = 100 - ((value - minWert) * 100 / (maxWert - minWert));
        return erg.toString();
    }

    public JsonObject parseLineProtocolToJson(String lineProtocol) {
        JsonObject jsonObject = new JsonObject();
        JsonArray resultsArray = new JsonArray();
        JsonObject resultObject = new JsonObject();
        JsonArray seriesArray = new JsonArray();
        JsonObject seriesObject = new JsonObject();
        JsonArray valuesArray = new JsonArray();

        String[] lines = lineProtocol.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 1) {
                JsonArray valueArray = new JsonArray();
                for (int i = 1; i < parts.length; i++) {
                    valueArray.add(parts[i]);
                }
                valuesArray.add(valueArray);
            }
        }

        seriesObject.add("values", valuesArray);
        seriesArray.add(seriesObject);
        resultObject.add("series", seriesArray);
        resultsArray.add(resultObject);
        jsonObject.add("results", resultsArray);

        return jsonObject;
    }
}
