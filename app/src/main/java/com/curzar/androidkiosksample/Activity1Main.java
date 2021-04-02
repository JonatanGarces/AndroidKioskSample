package com.curzar.androidkiosksample;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.curzar.androidkiosksample.database.SettingViewModel;
import com.curzar.androidkiosksample.model.Setting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class Activity1Main extends AppCompatActivity implements KioskInterface {
public class Activity1Main extends AppCompatActivity {
    public static String PACKAGE_NAME;
    private Button btn_call_first_activity,connectButton;
    private EditText editTextNumber3,editTextNumber4,editTextNumber5,editTextNumber6;
    private ImageView cross_check;
    private Activity1MainViewModel viewModel;
    private int segundos = 0;
    private double dinero = 0;
    private String device_name = "";
    private String device_mac = "";
    private String timeperunitofcurrency = "";
    private String txtMoneda = "";
    private String txtHora = "";
    private String txtMinuto = "";
    boolean buttonVisible = false;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        Runnable task2 = () -> {
            if(!viewModel.isConnected && viewModel.viewModelSetup){
                viewModel.connect();
            }
        };
        PACKAGE_NAME = getApplicationContext().getPackageName();
        scheduleTaskExecutor.scheduleAtFixedRate(task2, 20, 20, TimeUnit.SECONDS);
        setContentView(R.layout.activity_1main);
        getSupportActionBar().hide();
        connectButton           = findViewById(R.id.button);
        editTextNumber3         = findViewById(R.id.editTextNumber3);
        editTextNumber4         = findViewById(R.id.editTextNumber4);
        editTextNumber5         = findViewById(R.id.editTextNumber5);
        editTextNumber6         = findViewById(R.id.editTextNumber6);
        cross_check = (ImageView)findViewById(R.id.imageView2);
        cross_check.setImageResource(R.drawable.cross);
        viewModel               =new  ViewModelProvider(this).get(Activity1MainViewModel.class);
        connectButton.setVisibility(View.INVISIBLE);
        connectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 3000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                ApiCall();

            }
        });

        btn_call_first_activity=(Button) findViewById(R.id.btn_call_first_activity);
        btn_call_first_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askAdminPassword();
            }
        });
        viewModel.getAllSettings().observe(this,settings -> {
                    for (Setting setting : settings) {
                        switch (setting.getName()) {
                            case "devicename":
                                device_name = setting.getValue();
                                break;
                            case "devicemac":
                                device_mac = setting.getValue();
                                break;
                            case "timeperunitofcurrency":
                                timeperunitofcurrency =setting.getValue();
                                editTextNumber3.setText(timeperunitofcurrency);
                                break;
                            default:
                                break;
                        }
                    }
                    callViewModelBl();
                });
    }


    public void askAdminPassword(){
        final Dialog dialog = new Dialog(Activity1Main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_ask_admin_password);
        final EditText et_admin_password=(EditText) dialog.findViewById(R.id.et_admin_password);
        Button btn_proceed=(Button) dialog.findViewById(R.id.btn_proceed);
        Button btn_exit=(Button) dialog.findViewById(R.id.btn_exit);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!TextUtils.isEmpty(et_admin_password.getText()))
                {
                    if(getResources().getString(R.string.device_admin_password).equalsIgnoreCase(et_admin_password.getText().toString()))
                    {
                        Intent intent=new Intent(Activity1Main.this, Activity2Settings.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(Activity1Main.this, "Please enter valid password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onResume() {
       super.onResume();
       if(!viewModel.isConnected && viewModel.viewModelSetup){
           viewModel.connect();
       }

        variablesView("0","0","0",false);

    }

    public void variablesView(String moneda, String horas, String minutos,Boolean visible){
        editTextNumber4.setText(moneda);
        editTextNumber5.setText(horas);
        editTextNumber6.setText(minutos);
        if(visible) {
            connectButton.setEnabled(true);
            connectButton.setVisibility(View.VISIBLE);
        }else{
            connectButton.setEnabled(false);
            connectButton.setVisibility(View.INVISIBLE);
        }
    }

    private void callViewModelBl(){
        if (!viewModel.setupViewModel(device_name, device_mac)) {
            finish();
            return;
        }
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                txtMoneda = "0";
                buttonVisible = false;
                txtHora = "00";
                txtMinuto = "00";
            }else{
                txtMoneda = message;
                float minutostotales =Float.parseFloat(message)*Float.parseFloat(timeperunitofcurrency) ;
                segundos = (int)minutostotales*60;
                float horas = minutostotales/60;
                float horas_round = roundDown(horas,0);
                float minutos = horas - horas_round ;// you have 0.6789
                float minutos_round = roundDown(minutos*60,0);
                buttonVisible= true;
                if(horas_round == 0){
                    txtHora = "00";
                }else{
                    txtHora = String.format("%02d", (int)horas_round);
                }
                if (minutos_round == 0) {
                    txtMinuto = "00";
                }else{
                    txtMinuto =String.format("%02d", (int)minutos_round);
                }
            }
            variablesView(txtMoneda,txtHora,txtMinuto,buttonVisible);
        });
        viewModel.connect();
    }

    private void onConnectionStatus(Activity1MainViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                cross_check.setImageResource(R.drawable.check);
                break;
            case CONNECTING:
                cross_check.setImageResource(R.drawable.connecting);
                break;
            case DISCONNECTED:
                cross_check.setImageResource(R.drawable.cross);
                break;
        }
    }
    public void ApiCall(){
        ApiUser user = new ApiUser(0,segundos,"MXN","1","","",dinero);
        Call<ApiUser> call1 = apiInterface.createUser(user);
        call1.enqueue(new Callback<ApiUser>() {
            @Override
            public void onResponse(Call<ApiUser> call, Response<ApiUser> response) {
                viewModel.Zero();
                ApiUser user1 = response.body();
                Intent intent = new Intent(getBaseContext(), Activity21Credentials.class);
                intent.putExtra("username", user1.username);
                intent.putExtra("password", user1.password);
                intent.putExtra("ssid", "Renta_de_Wifi");
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ApiUser> call, Throwable t) {
                call.cancel();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public static float roundDown(float d, int decimalPlace)
    {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_FLOOR).floatValue();
    }
}
