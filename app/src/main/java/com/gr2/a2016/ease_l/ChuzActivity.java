package com.gr2.a2016.ease_l;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.volley.RequestQueue;

public class ChuzActivity extends Activity {
    RequestQueue requestQueue;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuz);
        context = this;
    }
}
