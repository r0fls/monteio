package com.example.r0fls.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity {
    private EditText user,pass;
    Button b;
    String nam, pw;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get username and password
        user= (EditText) findViewById(R.id.editText);
        pass= (EditText) findViewById(R.id.editText2);
        b=(Button) this.findViewById(R.id.button1);
        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void buttonClick(View view) throws IOException {
        nam = user.getText().toString();
        pw = pass.getText().toString();
        //refactor, move JSON mapping into the newUser() function
        final JSONObject jsonobj;
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("username", nam);
            jsonobj.put("password", pw);

            new Thread() {
                public void run() {
                        getToken(nam, pw);
                        //newUser(jsonobj);
                }
            }.start();
            ProgressBar spinner;
            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.VISIBLE);

        } catch (JSONException ex) {
            ex.printStackTrace();

        }
    }

    protected void getToken(String name, String pass)
    {

        try
        {
            URL url = new URL("http://192.168.0.2/api/token");
            URLConnection urlConnection = url.openConnection();
            String header = "Basic " + new String(android.util.Base64.encode((name+":"+pass).getBytes(), android.util.Base64.NO_WRAP));
            urlConnection.addRequestProperty("Authorization", header);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
            StringBuilder s = new StringBuilder(100);
            while ((inputLine = in.readLine()) != null)
                s.append(inputLine);
            in.close();
            JSONObject job = new JSONObject(s.toString());
            SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("monte_token",job.getString("token"));
            editor.apply();
            SharedPreferences rpreferences=getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
            String nam=rpreferences.getString("monte_token",null);
            Intent intent = new Intent(this, Pricing.class);
            intent.putExtra("com.example.r0fls.myapplication.token", job.getString("token"));
            startActivity(intent);
            Log.d("serial",nam);

        }
        catch(IOException e)
        {
            // Error
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void newUser(JSONObject jobj)
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        try
        {
            URL url = new URL("http://192.168.0.2/api/users");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(jobj.toString());
            request.flush();
            request.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                Log.d("serial",inputLine);
            in.close();
        }
        catch(IOException e)
        {
            // Error
        }
    }
}
