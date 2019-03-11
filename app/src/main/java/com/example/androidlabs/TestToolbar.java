package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    Toolbar toolbar;
    String localmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        localmessage = "this is the intial message.";
        toolbar=(Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.MenuItem1:


                Toast toast1 = Toast.makeText(getApplicationContext(), localmessage, Toast.LENGTH_SHORT);
                toast1.show();
                break;

            case R.id.MenuItem2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New message?");
                builder.setMessage("What is your new message?");

                EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       localmessage = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;

            case R.id.MenuItem3:
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Go back?",Snackbar.LENGTH_LONG);
                snackbar.setAction("Go Back?",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                snackbar.show();
                break;

            case R.id.MenuItem4:
                CharSequence text = "You clicked on the overflow menu";

                Toast toast2 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast2.show();
                break;
        }
        return true;
    }
}

