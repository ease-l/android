package com.gr2.a2016.ease_l;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gr2.a2016.ease_l.classes.CommentRequests;

public class CommentPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        ((EditText)findViewById(R.id.editText3)).setText(getIntent().getExtras().getString("Name"));
        ((EditText)findViewById(R.id.editText4)).setText(getIntent().getExtras().getString("Text"));
        ((Button)findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentRequests commentRequests = new CommentRequests();
                commentRequests.updateComment(((EditText) findViewById(R.id.editText3)).getText().toString(), ((EditText) findViewById(R.id.editText4)).getText().toString(), CommentPost.this,getIntent().getStringExtra("Id"));
                CommentPost.this.setResult(RESULT_OK);
                finish();
            }
        });

    }
}
