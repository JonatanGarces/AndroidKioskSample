package com.curzar.androidkiosksample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Activity21Credentials extends AppCompatActivity {
    String TAG = "GenerateQRCode";
    TextView txtusername,txtpassword,txtssid;
    ImageView qrImage;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
//WIFI:T:WPA2-EAP;S:ssid;E:[EAP method];PH2:[Phase 2 method];A:[anonymous identity];I:[username];P:[password];;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_21credentials);
        getSupportActionBar().hide();

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        String ssid     = getIntent().getStringExtra("ssid");
        String eap = "PEAP";
        String ph = "MSCHAPV2";//MSCHAPV2

        txtusername = (TextView) findViewById(R.id.txtusername);
        txtpassword = (TextView) findViewById(R.id.txtpassword);
        txtssid = (TextView) findViewById(R.id.txtssid);

        txtusername.setText(username);
        txtpassword.setText(password);
        txtssid.setText(ssid);

        //qrImage = (ImageView) findViewById(R.id.QR_Image);

        //inputValue ="WIFI:T:WPA2-EAP;S:"+ssid+";E:"+eap+";PH2:"+ph+";A:"+username+";I:"+username+";P:"+password+";;";
        inputValue ="WIFI:T:WPA2-EAP;S:"+ssid+";E:"+eap+";PH2:"+ph+";I:"+username+";P:"+password+";;";

        //inputValue ="WIFI:T:WPA2-EAP;S:"+username+";E:"+eap+";PH2:"+ph+";I:"+username+";P:"+password+";;";
      /*
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            bitmap = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(bitmap);
        } else {

        }
*/



        /*
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;
                try {

                    save = QRGSaver.save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        */


    }
}