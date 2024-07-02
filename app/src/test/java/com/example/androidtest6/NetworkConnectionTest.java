package com.example.androidtest6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import androidx.annotation.NonNull;

import com.example.androidtest6.influxDB.InfluxDBClient;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NetworkConnectionTest {

/*
Liefert der ConnectionCreater
eine Exception, wenn die
Connection nicht hergestellt
werden konnte.
 */
    @Test
    public void connectionErrorException() {
        InfluxDBClient client = new InfluxDBClient("hallo","ich","teste","hier");
        Exception exception = assertThrows(NullPointerException.class, () -> client.fetchData("test", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("haha");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("hihi");
            }
        }));


    }
/*
Liefert der ConnectionCreator
eine Exception, wenn ihm null
als Parameter weitergegeben
wird
 */
    @Test
    public void connectionNullError()  {
        //InfluxDBClient client = new InfluxDBClient(null,null,null,null);
        Exception exception = assertThrows(NullPointerException.class, () -> new InfluxDBClient(null,null,null,null) );
    }
}