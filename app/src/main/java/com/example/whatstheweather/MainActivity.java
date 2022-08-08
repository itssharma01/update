package com.example.whatstheweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
public class MainActivity extends AppCompatActivity {

    String cityName;
    TextView weatherInfo, temperatureInfo;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            try {
                URL url = new URL(urls[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "ENTER A VALID CITY !";
            }

        }

        @Override
        protected void onPostExecute(String s) {      //here we will get the jSON array.
            super.onPostExecute(s);

            Log.i("JSON",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo = jsonObject.getString("weather");
                String tempInfo = jsonObject.getString("main");
                Log.i("weather",weatherinfo);
                Log.i("tempInfo",tempInfo);
                JSONArray arr = new JSONArray(weatherinfo);

                for(int i=0;i<arr.length();i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                    Log.i("temp", (jsonObject.getJSONObject("temp")).toString());
                    weatherInfo.setText(jsonPart.getString("main")+"  :  "+jsonPart.getString("description"));
                }


            } catch (Exception e) {
                e.printStackTrace();
                weatherInfo.setText(s);
            }
        }

    }

    public void getInfo(View view) {
        TextView enteredCityName = findViewById(R.id.enteredCityName);
        DownloadTask task = new DownloadTask();
        Log.i("City",enteredCityName.getText().toString());
        cityName = enteredCityName.getText().toString();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+ cityName +"&appid=190156e8369eedd8438d4922f47cd374");

        //This two lines are the keyboard to close immediately you click the button.
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(weatherInfo.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherInfo = findViewById(R.id.weatherInfo);
        temperatureInfo = findViewById(R.id.temperatureInfo);

    }
}