package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button button1;
    EditText editText1;
    EditText editText2;
    SharedPreferences preferences;
    Button loginButton;
    Button toolbarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        preferences = getSharedPreferences("File", Context.MODE_PRIVATE);
        String fromStore = preferences.getString("Email", "Default value");
        editText1.setText(fromStore);

        loginButton = findViewById(R.id.button1);
        loginButton.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);
            nextPage.putExtra("typed", editText1.getText().toString());
            startActivity(nextPage);
        });

        toolbarButton = findViewById(R.id.toolbartestbutton);
        toolbarButton.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, TestToolbar.class);
            startActivity(nextPage);
        });

        toolbarButton = findViewById(R.id.buttonWeather);
        toolbarButton.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, WeatherForecast.class);
            startActivity(nextPage);
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        String toStore = editText1.getText().toString();
        editor.putString("Email", toStore);
        editor.commit();
    }
}
