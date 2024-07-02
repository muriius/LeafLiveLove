package com.example.androidtest6.influxDB;

//Speichert einfach nur die Daten
//Bald auslagerung für jeden nutzer, das nicht jeder auf das selbe haut
public class InfluxDBValues {
    private static final String INFLUXDB_URL = "https://eu-central-1-1.aws.cloud2.influxdata.com";
    private static final String TOKEN = "q3DGgs--tBBWM006JA7I3rha7fCSXKUwtflVmrG-x4TtPD-knBP4EbKOPH9CTT8kGwwsDzXyFjlMfG6VyxhQyw==";
    private static final String ORG = "edCorp";
    private static final String BUCKET = "AndroidDB";

    //TODO Gerade hard coded zu unserer Datenbank.Mit einem Side Menü den Unterpunkt "Settings", in welchem diese Werte für jeden
    //einzeln eingetragen werden können, sodass jeder seine Datenbank hat.



    public static String getBUCKET() {
        return BUCKET;
    }

    public static String getInfluxdbUrl() {
        return INFLUXDB_URL;
    }

    public static String getORG() {
        return ORG;
    }

    public static String getTOKEN() {
        return TOKEN;
    }
}
