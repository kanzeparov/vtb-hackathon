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
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ListView listview;
    ArrayList<User> list_users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Intent intent = getIntent();
        //* *EDIT* *
        listview = (ListView) findViewById(R.id.listView1);
        listview.setOnItemClickListener(this);

        initFirebase();
        addEventFirebaseListener();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShareActivity.this, ListOfAllActivity.class));
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
                    if (list_users.get(i).owner.equals(""))
                        stringlist[i] = list_users.get(i).name + " " + list_users.get(i).price;
                    else {
                        stringlist[i] = "";
                    }
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
    public void addData(User user,int id) {
        myRef.child("items").child(id+"").setValue(user);
    }
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        User user = list_users.get(position);
        user.owner = "Руслан";
        addData(user,position);
        // Then you start a new Activity via Intent
    }
}
