package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.EditText;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.util.Log;


public class ProfileActivity extends AppCompatActivity {
    EditText editText3;
    EditText editText4;
    ImageButton imageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageButton = findViewById(R.id.imageButton);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);

        Intent fromPrevious = getIntent();
        String previousTyped = fromPrevious.getStringExtra("typed");
        editText4.setText(previousTyped);

        imageButton.setOnClickListener( b -> {
            dispatchTakePictureIntent();
        });
        Log.e(ACTIVITY_NAME, "In function:"/*onCreate*/);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function:"/*onActivityResult*/);
    }

    protected void onStart(){
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:"/*onStart*/);
    }

    protected void onResume(){
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:"/*onResume*/);
    }

    protected void onPause(){
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:"/*onPause*/);
    }

    protected void onStop(){
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:"/*onStop*/);
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:"/*onDestroy*/);
    }
}
