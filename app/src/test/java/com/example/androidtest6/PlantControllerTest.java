package com.example.androidtest6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.example.androidtest6.PlantController.PlantController;
import com.example.androidtest6.influxDB.InfluxDBClient;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlantControllerTest {
    /*
Werte werden in leicht
verständliche Werte
umgerechnet
     */
    @Test
    public void plantValueCalcMoisture() {
        // PlantController pc = new PlantController(new InfluxDBClient(),new MainActivity());
        PlantController pc = new PlantController();

        String rDM1 = "_result,0,2024-07-02T09:05:00.702601838Z,2024-07-02T10:05:00.702601838Z,2024-07-02T10:03:10.669950517Z,579,value,soil_moisture";
        rDM1 = pc.parseMoisture(rDM1);

        //0%
        String rDM2 = "_result,0,2024-07-02T09:05:00.702601838Z,2024-07-02T10:05:00.702601838Z,2024-07-02T10:03:10.669950517Z,685,value,soil_moisture";
        rDM2 = pc.parseMoisture(rDM2);

        //100%
        String rDM3 = "_result,0,2024-07-02T09:05:00.702601838Z,2024-07-02T10:05:00.702601838Z,2024-07-02T10:03:10.669950517Z,499,value,soil_moisture";
        rDM3 = pc.parseMoisture(rDM3);

        assertEquals("57", rDM1);
        assertEquals("0", rDM2);
        assertEquals("100", rDM3);

    }

    @Test
    public void plantValueCalcTemp() {
        PlantController pc = new PlantController();

        String rDT1 = "_result,0,2024-07-02T09:05:00.712725748Z,2024-07-02T10:05:00.712725748Z,2024-07-02T10:00:12.858086328Z,24,value,temperature";
        String rDT2 = "_result,0,2024-07-02T09:05:00.712725748Z,2024-07-02T10:05:00.712725748Z,2024-07-02T10:00:12.858086328Z,35,value,temperature";
        rDT1 = pc.parseTemperature(rDT1);
        rDT2 = pc.parseTemperature(rDT2);


        assertEquals("24", rDT1);
        assertEquals("35", rDT2);
    }

    @Test
    public void plantValueCalcHum() {
        PlantController pc = new PlantController();

        String rDH1 = "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:58.918813299Z,40,value,humidity";
        String rDH2 = "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:58.918813299Z,60,value,humidity";
        rDH1 = pc.parseHumidity(rDH1);
        rDH2 = pc.parseHumidity(rDH2);

        assertEquals("40", rDH1);
        assertEquals("60", rDH2);
    }

    /*
Exception bei falschen Werten
     */
    @Test
    public void wrongPlantValueCalc() {
        // PlantController pc = new PlantController(new InfluxDBClient(),new MainActivity());
        PlantController pc = new PlantController();

        String rDM1 = "_result,0,2024-07-02T09:05:00.702601838Z,2024-07-02T10:05:00.702601838Z,2024-07-02T10:03:10.669950517Z,10,value,soil_moisture";
        rDM1 = pc.parseMoisture(rDM1);

        //0%
        String rDM2 = "_result,0,2024-07-02T09:05:00.702601838Z,2024-07-02T10:05:00.702601838Z,2024-07-02T10:03:10.669950517Z,999,value,soil_moisture";
        rDM2 = pc.parseMoisture(rDM2);

        assertEquals("Fehler", rDM1);
        assertEquals("Fehler", rDM2);


        String rDT1 = "_result,0,2024-07-02T09:05:00.712725748Z,2024-07-02T10:05:00.712725748Z,2024-07-02T10:00:12.858086328Z,-1,value,temperature";
        rDT1 = pc.parseTemperature(rDT1);


        String rDT2 = "_result,0,2024-07-02T09:05:00.712725748Z,2024-07-02T10:05:00.712725748Z,2024-07-02T10:00:12.858086328Z,60,value,temperature";
        rDT2 = pc.parseTemperature(rDT2);

        assertEquals("Fehler", rDT1);
        assertEquals("Fehler", rDT2);

        String rDH1 = "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:58.918813299Z,91,value,humidity";
        rDH1 = pc.parseHumidity(rDH1);


        String rDH2 = "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:58.918813299Z,19,value,humidity";
        rDH2 = pc.parseHumidity(rDH2);

        assertEquals("Fehler", rDH1);
        assertEquals("Fehler", rDH2);
    }

    @Test
    public void emptyDataTest() {

        PlantController pc = new PlantController();

        String rDM1 = "";
        rDM1 = pc.parseMoisture(rDM1);


        assertEquals("Fehler", rDM1);
    }

    @Test
    public void noListValuesTest() {
        // PlantController pc = new PlantController(new InfluxDBClient(),new MainActivity());
        PlantController pc = new PlantController();

        String rDM1 = null;

        Exception exception = assertThrows(Exception.class, () -> pc.parseMoisture(rDM1));
    }
}