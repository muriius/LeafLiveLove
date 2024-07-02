package com.example.androidtest6.influxDB;

import okhttp3.*;
import okhttp3.internal.http2.ConnectionShutdownException;

public class InfluxDBClient {


    private static String influxdb_url = "";
    private static String token = "";
    private static String org = "";
    private static String bucket = "";



    public OkHttpClient client = new OkHttpClient();
    public InfluxDBClient(String url,String token,String org,String bucket){
        if (url == null||token==null||org==null||bucket==null){
            throw new NullPointerException("Null kein zulässiger Wert");
        }

        this.influxdb_url=url;
        this.client=client;
        this.org=org;
        this.bucket=bucket;
    }

    public InfluxDBClient(){
        influxdb_url = InfluxDBValues.getInfluxdbUrl();
        token = InfluxDBValues.getTOKEN();
        org = InfluxDBValues.getORG();
        bucket = InfluxDBValues.getBUCKET();
    }

    public void fetchData(String measurement, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(influxdb_url + "/api/v2/query").newBuilder();
        if (urlBuilder == null){
            throw new IllegalArgumentException("Keine Connectionü");
        }
        urlBuilder.addQueryParameter("org", org);

        String query = "from(bucket:\"" + bucket + "\")" +
                " |> range(start: -1h)" +
                " |> filter(fn: (r) => r._measurement == \"" + measurement + "\")";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/vnd.flux"),
                query
        );

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .post(body)
                .addHeader("Authorization", "Token " + token)
                .addHeader("Content-Type", "application/vnd.flux")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public String getBUCKET() {
        return bucket;
    }
    public static void setInfluxdbUrl(String url){
        influxdb_url=url;
    }
}
