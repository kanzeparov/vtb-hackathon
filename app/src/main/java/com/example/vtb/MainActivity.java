package com.example.vtb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Integer billZhena = 490;
    String payerRus = "7779b1db5db8017c8f4832ad5c8e9df4cad069d6";
    String numberLiza = "";
    String numberZhena = "";
    String payerLiza = "3d6228c52429bbaee13bba8a91929c9f85cfba42";
    String payerZhena = "938666ac50e9c290ccb04b7a42e936e809e2d50a";
    private String sessionIdString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        addEventFirebaseListener();
        final TextView textView = findViewById(R.id.balance);
        TextView dolg = findViewById(R.id.moidolg);

        Button button = findViewById(R.id.button);

        Intent intent = getIntent();
            if(intent.getIntExtra("Zhena",0) == 0 || intent.getIntExtra("Liza",0) == 0) {
                dolg.setText("Женя должен " + (billZhena));
            } else {
            dolg.setText("Женя должен " + (billZhena+intent.getIntExtra("Zhena",0)) +
                    "\n" +
                    "Лиза должна " + (intent.getIntExtra("Liza",0)));
            new Liza().execute();
        }

        Session session = new Session();
        session.setDeviceType(1);
        session.setDeviceId("fewf");
        //startActivity(new Intent(ListOfAllActivity.this, DecoderSecond.class));
        final HashMap<String,String> hashMap2 = new HashMap<>();
        NetworkService.getInstance()
                .getJSONApi().postUser(session)
                .enqueue(new Callback<SessionId>() {
                    @Override
                    public void onResponse(@NonNull Call<SessionId> call, @NonNull Response<SessionId> response) {
                        SessionId post = response.body();
                        sessionIdString = post.getData();
                        hashMap2.put("FPSID", sessionIdString);


                        NetworkService.getInstance()
                                .getJSONApi().getBalance(hashMap2, payerRus)
                                .enqueue(new Callback<Balance>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Balance> call, @NonNull Response<Balance> response) {
                                        Balance status = response.body();
                                        if(textView != null )
                                            textView.setText(textView.getText().toString() + " " + status.getData().getTotal());
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Balance> call, @NonNull Throwable t) {
                                    }
                                });
                    }

                    @Override
                    public void onFailure(@NonNull Call<SessionId> call, @NonNull Throwable t) {
                    }
                });





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



    private void addEventFirebaseListener() {
        //показываем View загрузки

// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberLiza = dataSnapshot.child("status_transaction_liza").getValue(TransStatus.class).number;
                numberZhena = dataSnapshot.child("status_transaction_zhena").getValue(TransStatus.class).number;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initFirebase() {
        //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
        // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
        FirebaseApp.initializeApp(this);
        //получаем точку входа для базы данных
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    class Liza extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberLiza, payerLiza)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberLiza;
                                transStatus.status = true;
                                myRef.child("status_transaction_liza").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberLiza, payerLiza)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberLiza;
                                transStatus.status = true;
                                myRef.child("status_transaction_liza").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberLiza, payerLiza)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberLiza;
                                transStatus.status = true;
                                myRef.child("status_transaction_liza").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class Zhena extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberZhena, payerZhena)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberZhena;
                                transStatus.status = true;
                                myRef.child("status_transaction_zhena").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberZhena, payerZhena)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberZhena;
                                transStatus.status = true;
                                myRef.child("status_transaction_zhena").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            NetworkService.getInstance()
                    .getJSONApi().getDataFromService(numberZhena, payerZhena)
                    .enqueue(new Callback<com.example.vtb.Status>() {
                        @Override
                        public void onResponse(@NonNull Call<com.example.vtb.Status> call, @NonNull Response<com.example.vtb.Status> response) {
                            com.example.vtb.Status status = response.body();
                            if(status.getData().getState() == 5) {
                                TransStatus transStatus = new TransStatus();
                                transStatus.number = numberZhena;
                                transStatus.status = true;
                                myRef.child("status_transaction_zhena").setValue(transStatus);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<com.example.vtb.Status> call, @NonNull Throwable t) {
                        }
                    });
            try{
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
