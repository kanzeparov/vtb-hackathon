package com.example.vtb;

import androidx.annotation.NonNull;
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
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfAllActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Integer billLiza = 0;
    Integer billZhena = 0;
    String numberLiza = "";
    String numberZhena = "";
    private ListView listview;
    ArrayList<User> list_users = new ArrayList<>();
    private String sessionIdString = "";
    String payerLiza = "3d6228c52429bbaee13bba8a91929c9f85cfba42";
    String payerRus = "7779b1db5db8017c8f4832ad5c8e9df4cad069d6";
    String payerZhena = "938666ac50e9c290ccb04b7a42e936e809e2d50a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all);
        Intent intent = getIntent();
        //* *EDIT* *
        listview = (ListView) findViewById(R.id.listView1);

        initFirebase();
        addEventFirebaseListener();
        final HashMap<String,String> hashMap1 = new HashMap<>();
        Button button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("items").setValue(null);
                Session session = new Session();
                session.setDeviceType(1);
                session.setDeviceId("fewf");
                //startActivity(new Intent(ListOfAllActivity.this, DecoderSecond.class));
                NetworkService.getInstance()
                        .getJSONApi().postUser(session)
                        .enqueue(new Callback<SessionId>() {
                            @Override
                            public void onResponse(@NonNull Call<SessionId> call, @NonNull Response<SessionId> response) {
                                SessionId post = response.body();
                                sessionIdString = post.getData();

                                hashMap1.put("FPSID", sessionIdString);

                                InvoiceReq invoiceReq = new InvoiceReq();
                                invoiceReq.setAmount(billLiza);
                                invoiceReq.setCurrencyCode(810);
                                invoiceReq.setDescription("Лиза верни долг");
                                invoiceReq.setNumber(numberLiza);
                                invoiceReq.setPayer(payerLiza);
                                invoiceReq.setRecipient(payerRus);



                                InvoiceReq invoiceReq1 = new InvoiceReq();
                                invoiceReq1.setAmount(billZhena+490);
                                invoiceReq1.setCurrencyCode(810);
                                invoiceReq1.setDescription("Женя верни долг");
                                invoiceReq1.setNumber(numberZhena);
                                invoiceReq1.setPayer(payerZhena);
                                invoiceReq1.setRecipient(payerRus);

                                NetworkService.getInstance()
                                        .getJSONApi().invoice(hashMap1,invoiceReq)
                                        .enqueue(new Callback<Invoice>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Invoice> call, @NonNull Response<Invoice> response) {
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Invoice> call, @NonNull Throwable t) {
                                            }
                                        });

                                NetworkService.getInstance()
                                        .getJSONApi().invoice(hashMap1,invoiceReq1)
                                        .enqueue(new Callback<Invoice>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Invoice> call, @NonNull Response<Invoice> response) {
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Invoice> call, @NonNull Throwable t) {
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(@NonNull Call<SessionId> call, @NonNull Throwable t) {
                            }
                        });



                Intent intent1 = new Intent(ListOfAllActivity.this, MainActivity.class);
                intent1.putExtra("Zhena", billZhena);
                intent1.putExtra("Liza", billLiza);

                startActivity(intent1);


//
//                HashMap<String,String> hashMap2 = new HashMap<>();
//                hashMap1.put("FPSID", sessionIdString);
//
//                NetworkService.getInstance()
//                        .getJSONApi().getBalance(hashMap2, payerRus)
//                        .enqueue(new Callback<Status>() {
//                            @Override
//                            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
//                                Status status = response.body();
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<Status> call, @NonNull Throwable t) {
//                            }
//                        });
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

                numberLiza = dataSnapshot.child("status_transaction_liza").getValue(TransStatus.class).number;
                numberZhena = dataSnapshot.child("status_transaction_zhena").getValue(TransStatus.class).number;
                String[] stringlist = new String[list_users.size()];
                for (int i = 0; i < list_users.size(); i++) {
                        stringlist[i] = list_users.get(i).name + " " + list_users.get(i).price + " " + list_users.get(i).owner;
                    if(list_users.get(i).owner.equals("Лиза")) {
                        billLiza += list_users.get(i).price;
                    }
                    if(list_users.get(i).owner.equals("Женя")) {
                        billZhena += list_users.get(i).price;
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
        UserStatus userStatus = new UserStatus();
        userStatus.status = false;
        myRef.child("/push_bill_liza").setValue(userStatus);
        myRef.child("/push_bill_zhena").setValue(userStatus);

    }
}
