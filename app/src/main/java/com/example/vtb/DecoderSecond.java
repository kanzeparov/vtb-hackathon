package com.example.vtb;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class DecoderSecond extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

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


        }

// create and show the alert dialog




        // Called when a QR is decoded
        // "text" : the text encoded in QR
        // "points" : points where QR control points are placed in View
        @Override
        public void onQRCodeRead(String text, PointF[] points) {




            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

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
