package com.example.vtb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Выберете участников");

// add a list
                String[] animals = {"Лиза - 79775709014", "Жена - 79688957084", "Я"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: Toast.makeText(getApplicationContext(), "Вы выбрали Лизу", Toast.LENGTH_LONG).show(); break;
                            case 1:  Toast.makeText(getApplicationContext(), "Вы выбрали Женю", Toast.LENGTH_LONG).show();break;
                            case 2:  {Toast.makeText(getApplicationContext(), "Вы выбрали Себя", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, ButtonsCamer.class));}
                        }
                    }
                });

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
        }});


    }
}
