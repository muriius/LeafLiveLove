package com.example.androidtest6;

import com.example.androidtest6.influxDB.InfluxDBClient;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import android.util.Log;

public class NetworkProtocolTest {

    @Mock
    Callback mockCallback;

    InfluxDBClient influxDBClient;
    MockWebServer mockWebServer;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String url = mockWebServer.url("/api/v2/query").toString();
        influxDBClient = new InfluxDBClient(url, "test_token", "test_org", "test_bucket");
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
    /*
    Protocol zur richtigen Erstellung einer Connection zur InfluxDB
     */
    @Test
    public void protocolConnectionTest() throws IOException, InterruptedException {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("mock response body");
        mockWebServer.enqueue(mockResponse);


        influxDBClient.fetchData("temperature", mockCallback);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/v2/query/api/v2/query?org=test_org", recordedRequest.getPath());


        assertEquals("Token q3DGgs--tBBWM006JA7I3rha7fCSXKUwtflVmrG-x4TtPD-knBP4EbKOPH9CTT8kGwwsDzXyFjlMfG6VyxhQyw==", recordedRequest.getHeader("Authorization"));
        assertEquals("application/vnd.flux; charset=utf-8", recordedRequest.getHeader("Content-Type"));


        String expectedBody = "from(bucket:\"test_bucket\") |> range(start: -1h) |> filter(fn: (r) => r._measurement == \"temperature\")";
        String actualBody = recordedRequest.getBody().readUtf8();

        assertEquals(expectedBody, actualBody);

        verify(mockCallback, timeout(1000)).onResponse(any(Call.class), any(Response.class));
    }

    private String bodyToString(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }

    /*

     */
    @Test
    public void testFetchDataList() throws IOException, InterruptedException {
        //BSP aus echten records
        String mockData = "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:49:55.938589623Z,59,value,humidity\n" +
                "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:11.701685086Z,59,value,humidity\n" +
                "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:27.431946432Z,59,value,humidity\n" +
                "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:43.172700373Z,59,value,humidity\n" +
                "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:50:58.918813299Z,59,value,humidity\n" +
                "_result,0,2024-07-02T09:05:10.703791834Z,2024-07-02T10:05:10.703791834Z,2024-07-02T09:51:14.794722232Z,59,value,humidity";

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(mockData);
        mockWebServer.enqueue(mockResponse);


        influxDBClient.fetchData("humidity", mockCallback);

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/v2/query/api/v2/query?org=test_org", recordedRequest.getPath());


        assertEquals("Token q3DGgs--tBBWM006JA7I3rha7fCSXKUwtflVmrG-x4TtPD-knBP4EbKOPH9CTT8kGwwsDzXyFjlMfG6VyxhQyw==", recordedRequest.getHeader("Authorization"));
        assertEquals("application/vnd.flux; charset=utf-8", recordedRequest.getHeader("Content-Type"));

        String expectedBody = "from(bucket:\"test_bucket\") |> range(start: -1h) |> filter(fn: (r) => r._measurement == \"humidity\")";
        String actualBody = recordedRequest.getBody().readUtf8();
        assertEquals(expectedBody, actualBody);


        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(mockCallback, timeout(1000)).onResponse(any(Call.class), responseCaptor.capture());

        Response capturedResponse = responseCaptor.getValue();
        assertEquals(200, capturedResponse.code());

        String responseBody = capturedResponse.body().string();
        assertEquals(mockData, responseBody);
    }



    @Test
    public void noListValuesTest() {

        InfluxDBClient dbClient = new InfluxDBClient();


        dbClient.fetchData("temperatureFLASCH", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("InfluxDB Response", responseData);

                    assertEquals("Fehler", responseData);
                } else {
                    Log.e("InfluxDB", "Request failed with code: " + response.code());
                }
            }
        });


    }
    @Test
    public void testFetchDataValue() throws Exception { //DIesmal echte Datenbank, kann ausfallen wenn nicht verbunden.
        MockResponse mockResponse = new MockResponse()
                .setBody("temperature,location=1 value=23 1628000000000\n")
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        influxDBClient.fetchData("temperature", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assertEquals(200, response.code());
                assertEquals("temperature,location=1 value=23 1628000000000\n", response.body().string());
            }
        });
    }
}
