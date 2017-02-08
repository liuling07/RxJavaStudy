package com.rxjavastudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basis(View view) {
        Intent intent = new Intent(this, RxJavaBasisActivity.class);
        startActivity(intent);
    }

    public void createData(View view) {
        Intent intent = new Intent(this, CreateDataActivity.class);
        startActivity(intent);
    }

}
