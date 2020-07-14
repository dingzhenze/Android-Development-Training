package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.todolist.R;

public class NewActivity extends AppCompatActivity {

    EditText editText = findViewById(R.id.edit);
    Button button = findViewById(R.id.button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(NewActivity.this, TodoListActivity.class);
                intent2.putExtra("data", editText.getText().toString().trim());
                startActivity(intent2);
            }
        });
    }
}
