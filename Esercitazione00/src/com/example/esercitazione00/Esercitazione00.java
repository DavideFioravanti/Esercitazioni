package com.example.esercitazione00;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Esercitazione00 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esercitazione00);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_esercitazione00, menu);
        return true;
    }
}
