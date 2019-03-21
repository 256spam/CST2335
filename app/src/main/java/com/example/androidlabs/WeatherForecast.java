package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    private ImageView currentWeather;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView uvView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        currentWeather = findViewById(R.id.weatherImg);
        currentTemp = findViewById(R.id.currentTempView);
        minTemp = findViewById(R.id.minTempView);
        maxTemp = findViewById(R.id.maxTempView);
        uvView = findViewById(R.id.uvView);
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery query = new ForecastQuery();
        query.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    class ForecastQuery extends AsyncTask<String, Integer, String[]> {
        private WeakReference<Context> contextRef;
        String current = "0";
        String min = "0";
        String max = "0";
        String weatherIcon = "0";
        Bitmap image;

        @Override
        protected String[] doInBackground(String... strings) {
            String results[] = new String[5];
            try {
                //get weather info
                String urlString = strings[0];

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                Log.e("WeatherAsyncTask", "connection created");

                //start checking xml
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();
                Log.e("WeatherAsyncTask", "pullparser created");

                //get info from XML
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();
                        if(tagName.equals("temperature")) {
                            current = parser.getAttributeValue(null, "value");
                            Log.i("WeatherAsyncTask", "Found parameter message: "+ currentTemp);
                            publishProgress(25);
                            results[0] = current;
                            min = parser.getAttributeValue(null, "min");
                            Log.i("WeatherAsyncTask", "Found parameter message: "+ minTemp);
                            publishProgress(50);
                            results[1] = min;
                            max = parser.getAttributeValue(null, "max");
                            Log.i("WeatherAsyncTask", "Found parameter message: "+ maxTemp);
                            publishProgress(75);
                            results[2] = max;
                        } else if (tagName.equals("weather")) {
                            weatherIcon = parser.getAttributeValue(null, "icon");
                            Log.i("WeatherAsyncTask", "Found parameter message: "+ weatherIcon);
                        }
                    }
                    parser.next();
                }

                //obtain bitmap image
                if (fileExistance(weatherIcon + ".png") == false) {
                    Log.i("WeatherAsyncTask", "Downloading image: " + weatherIcon + ".png");
                    String urlString2 = ("http://openweathermap.org/img/w/" + weatherIcon + ".png");
                    image = null;
                    URL url2 = new URL(urlString2);
                    conn = (HttpURLConnection) url2.openConnection();
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(conn.getInputStream());
                    }
                    FileOutputStream outputStream = openFileOutput( weatherIcon + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }else{
                    Log.i("WeatherAsyncTask", "Accessing Local image: " + weatherIcon + ".png");
                    FileInputStream fis = null;
                    try {    fis = openFileInput(weatherIcon + ".png");   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                   image = BitmapFactory.decodeStream(fis);
                }
                publishProgress(100);

                //Start of JSON reading of UV factor:

                //create the network connection:
                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                double aDouble = jObject.getDouble("value");
                Log.i("UV is:", ""+ aDouble);
                results[3] = Double.toString(aDouble);

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            return results;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String[] results){

            currentWeather.setImageBitmap(image);
            currentTemp.setText(results[0]);
            minTemp.setText(results[1]);
            maxTemp.setText(results[2]);
            uvView.setText(results[3]);
            super.onPostExecute(results);
        }
    }
}

