package com.example.vtb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfAllActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private ListView listview;
    ArrayList<User> list_users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all);
        Intent intent = getIntent();
        //* *EDIT* *
        listview = (ListView) findViewById(R.id.listView1);

        initFirebase();
        addEventFirebaseListener();

        Button button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("items").setValue(null);
                //startActivity(new Intent(ListOfAllActivity.this, DecoderSecond.class));
            }
        });
    }

    private void addEventFirebaseListener() {
        //показываем View загрузки

// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_users.size() > 0) {
                    list_users.clear();
                }
                //проходим по всем записям и помещаем их в list_users в виде класса User
                for (DataSnapshot postSnapshot : dataSnapshot.child("items").getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    list_users.add(user);
                }
                String[] stringlist = new String[list_users.size()];
                for (int i = 0; i < list_users.size(); i++) {
                        stringlist[i] = list_users.get(i).name + " " + list_users.get(i).price + " " + list_users.get(i).owner;
                }
                //публикуем данные в ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, stringlist);
                listview.setAdapter(adapter);
                //убираем View загрузки
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mDatabaseReference.child("check")
//                .addValueEventListener(new ValueEventListener() {
//                    //если данные в БД меняются
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (list_users.size() > 0) {
//                            list_users.clear();
//                        }
//                        //проходим по всем записям и помещаем их в list_users в виде класса User
//                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                            User user = postSnapshot.getValue(User.class);
//                            list_users.add(user);
//                        }
//                        String[] stringlist = new String[list_users.size()];
//                        for(int i = 0; i < list_users.size(); i++) {
//                            stringlist[i] = list_users.get(i).name;
//                        }
//                        //публикуем данные в ListView
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                                android.R.layout.simple_list_item_1, stringlist);
//                        listview.setAdapter(adapter);
//                        //убираем View загрузки
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    private void initFirebase() {
        //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
        // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
        FirebaseApp.initializeApp(this);
        //получаем точку входа для базы данных
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }
}
