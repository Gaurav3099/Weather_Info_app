package com.gaurav.weatherinfo;

import android.os.AsyncTask;
import android.renderscript.Double3;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText etPlaceName;
    Button btnSearch;
    TextView tvSearchresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPlaceName = (EditText)findViewById(R.id.etPlaceName);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvSearchresult = (TextView)findViewById(R.id.tvSearchResult);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pn = etPlaceName.getText().toString();
                if (pn.length() == 0)
                {
                    etPlaceName.setError("place is empty");
                    etPlaceName.requestFocus();
                    return;
                }

                Task1 t = new Task1();
                t.execute("http://api.openweathermap.org/data/2.5/weather?units=metric" + "&q="+pn+ "&appid=c6e315d09197cec231495138183954bd");
            }
        });
    }
    class Task1 extends AsyncTask<String, Void, Double>{
        double temperature=0.0;
        @Override
        protected Double doInBackground(String... strings) {
            String json="", line="";

            try{
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.connect();
                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null){
                    json = json + line + "\n";
                }
                if (json != null){
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject info = jsonObject.getJSONObject("main");
                    temperature = info.getDouble("temp");
                }

            }
            catch (Exception e){

            }
            return temperature;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvSearchresult.setText("Temperatue "+aDouble);
        }
    }
}
