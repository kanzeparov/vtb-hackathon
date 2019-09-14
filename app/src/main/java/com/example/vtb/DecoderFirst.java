package com.example.vtb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DecoderFirst extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder_first);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DecoderFirst.this);
        builder.setTitle("Выберете участников");

// add a list
        String[] animals = {"Отправить пуши"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Пуши отправлены", Toast.LENGTH_LONG).show();
                        UserStatus userStatus = new UserStatus();
                        userStatus.status = true;
                        myRef.child("/push_bill_liza").setValue(userStatus);
                        myRef.child("/push_bill_zhena").setValue(userStatus);
                    }
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
        initFirebase();
    }

// create and show the alert dialog


    private void initFirebase() {
        //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
        // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
        FirebaseApp.initializeApp(this);
        //получаем точку входа для базы данных
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {




            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        myRef.child("items").setValue(null);
        User user = new User();
        user.name = "Хлеб";
        user.owner = "";
        user.price = 50;
        addData(user,0);

        User user1 = new User();
        user1.name = "Сок";
        user1.owner = "";
        user1.price = 50;
        addData(user1,1);

        Intent intent = new Intent(DecoderFirst.this, ShareActivity.class);
        intent.putExtra("text",text);
        startActivity(intent);
        }

        public void addData(User user,int id) {
            myRef.child("items").child(id+"").setValue(user);
        }




    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}