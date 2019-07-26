package com.gookkis.firebaseuas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        String url = getIntent().getStringExtra("url");

        ZoomageView imageZoom = findViewById(R.id.myZoomageView);
        Glide.with(this).load(url).into(imageZoom);
    }
}
