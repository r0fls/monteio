package com.example.r0fls.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Pricing extends ActionBarActivity {
    private EditText ticker, strike, days;
    Button b;
    String t, s, d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        ticker = (EditText) findViewById(R.id.ticker);
        strike = (EditText) findViewById(R.id.strike);
        days = (EditText) findViewById(R.id.days);
        b = (Button) this.findViewById(R.id.button);
        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBarPricing);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pricing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void priceButton(View view) throws IOException, InterruptedException {
        t = ticker.getText().toString();
        s = strike.getText().toString();
        d = days.getText().toString();
        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBarPricing);
        spinner.setVisibility(View.VISIBLE);
        Thread tr =  new Thread(new Runnable() {
            @Override
            public void run() {
                    getPrice(t, d, s);
            }
        });
        tr.start();
        tr.join();
        spinner.setVisibility(View.GONE);
        SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
        String value = preferences.getString("value" + t + d + s, null);
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
        }

    protected void getPrice(String ticker, String days, String strike)
    {

        try
        {
            URL url = new URL("http://192.168.0.2/api/res/"+ticker+"/"+days+"/"+strike);
            URLConnection urlConnection = url.openConnection();
            SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
            String token = preferences.getString("monte_token", null);
            String header = "Basic " + new String(android.util.Base64.encode((token+":x").getBytes(), android.util.Base64.NO_WRAP));
            urlConnection.addRequestProperty("Authorization", header);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
            StringBuilder s = new StringBuilder(100);
            while ((inputLine = in.readLine()) != null)
                s.append(inputLine);
            in.close();
            JSONObject jsonobj = new JSONObject(s.toString());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("value"+ticker+days+strike,jsonobj.getString("value"));
            editor.apply();
            Log.d("serial", jsonobj.getString("value"));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
