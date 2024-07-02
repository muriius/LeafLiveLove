package com.example.androidtest6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidtest6.PlantController.PlantController;
import com.example.androidtest6.influxDB.InfluxDBClient;
import com.example.androidtest6.PlantController.PlantController;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView temperatureValue;
    private TextView humidityValue;
    private TextView moistureValue;
    private Button updateButton;

    private InfluxDBClient client;
    private PlantController plantController;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureValue = findViewById(R.id.temperatureValue);
        humidityValue = findViewById(R.id.humidityValue);
        moistureValue = findViewById(R.id.moistureValue);

        updateButton = findViewById(R.id.updateButton);

        client = new InfluxDBClient();
        plantController = new PlantController(client, this);

        // Starten der automatischen Aktualisierung alle 10 Sekunden
        startAutoRefresh(10 * 1000); // 10 Sekunden in Millisekunden

        // Erste Datenabfrage beim Start der Activity
        fetchDataFromInfluxDB();
       // showSnackbar("Mit Datenbank verbunden");


        // OnClickListener für den Update-Button setzen
        updateButton.setOnClickListener(v -> {
            fetchDataFromInfluxDB();
            showSnackbar("Werte aktualisiert");
        });
    }

    private void startAutoRefresh(long intervalMillis) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchDataFromInfluxDB();
            }
        }, 0, intervalMillis);
    }

    private void fetchDataFromInfluxDB() {
        plantController.fetchTemperature();
        plantController.fetchHumidity();
        plantController.fetchMoisture();
    }

    public void updateTemperatureUI(String temperature) {
        if (temperature == "Fehler"){
            runOnUiThread(() -> temperatureValue.setText("Temperatur: " + temperature));
        }
        else {
            runOnUiThread(() -> temperatureValue.setText("Temperatur: " + temperature + "°C"));
        }
    }

    public void updateHumidityUI(String humidity) {
        if (humidity == "Fehler"){
            runOnUiThread(() -> humidityValue.setText("Luftfeuchtigkeit: " + humidity));
        }
        else{
            runOnUiThread(() -> humidityValue.setText("Luftfeuchtigkeit: " + humidity + "%"));
        }
    }

    public void updateMoistureUI(String moisture) {
        if (moisture == "Fehler"){
            runOnUiThread(() -> moistureValue.setText("Wasser: " + moisture ));
        }
        else{
            runOnUiThread(() -> moistureValue.setText("Wasser: " + moisture + "%"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
