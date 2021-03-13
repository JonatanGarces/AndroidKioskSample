package com.curzar.androidkiosksample;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    private TextView mTextMessage,textView16,textView17;
    private Button btn_call_first_activity,kioskmodeButton,zeroButton,connectButton;
    private EditText editTextNumber3,editTextNumber4,editTextNumber5,editTextNumber6;
    private ImageView cross_check;

    private Activity1MainViewModel viewModel;
    //private SettingViewModel mSettingViewModel;
    private int segundos = 0;
    private double dinero = 0;
    private String device_name = "";
    private String device_mac = "";
    private String timeperunitofcurrency = "";
    private Setting setting;
    //private DevicePolicyManager mDevicePolicyManager;
   // private ActivityManager am;
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        Runnable task2 = () -> {
           // Log.d("laputaputa","noconnected");
            if(!viewModel.isConnected && viewModel.viewModelSetup){
                viewModel.connect();
                Log.d("laputaputa","intent");
            }
        };
        PACKAGE_NAME = getApplicationContext().getPackageName();

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(task2, 1, 1, TimeUnit.MINUTES);
        setContentView(R.layout.activity_1main);
        getSupportActionBar().hide();
        //mSettingViewModel       = ViewModelProviders.of(this).get(SettingViewModel.class);
        //am                      = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //mDevicePolicyManager    = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mTextMessage            = (TextView) findViewById(R.id.message);
        kioskmodeButton         = findViewById(R.id.btnkioskmode);
        zeroButton              = findViewById(R.id.communicate_zero);
        connectButton           = findViewById(R.id.button);
        editTextNumber3         = findViewById(R.id.editTextNumber3);
        editTextNumber4         = findViewById(R.id.editTextNumber4);
        editTextNumber5         = findViewById(R.id.editTextNumber5);
        editTextNumber6         = findViewById(R.id.editTextNumber6);
        textView16              = (TextView) findViewById(R.id.textView16);
        textView17              = (TextView) findViewById(R.id.textView17);


        cross_check = (ImageView)findViewById(R.id.imageView2);

        cross_check.setImageResource(R.drawable.cross);


        viewModel               =new  ViewModelProvider(this).get(Activity1MainViewModel.class);
        connectButton.setVisibility(View.INVISIBLE);
        connectButton.setEnabled(true);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 ApiCall();
            }
        });

        /*
       if(am.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE)
        {
            kioskmodeButton.setText(R.string.button_txt_enable_kiosk);
        }
        else
        {
            kioskmodeButton.setText(R.string.button_txt_disable_kiosk);
        }

        kioskmodeButtonkioskmodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(am.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE)
                {
                    askAdminPassword();
                }
                else
                {
                    CheckKioskModeDialog dialog=new CheckKioskModeDialog();
                    dialog.show(getFragmentManager(),"KIOSK_MODE_DIALOG");
                }
            }
        });
*/
        btn_call_first_activity=(Button) findViewById(R.id.btn_call_first_activity);
        btn_call_first_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activity1Main.this, Activity2Settings.class);
                startActivity(intent);
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


    @Override
    protected void onResume() {
       super.onResume();
       if(!viewModel.isConnected && viewModel.viewModelSetup){
           viewModel.connect();
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
                message = "0";
            }else{
                float minutostotales =Float.parseFloat(message)*Float.parseFloat(timeperunitofcurrency) ;
                segundos = (int)minutostotales*60;
                float horas = minutostotales/60;
                float horas_round = roundDown(horas,0);
                float minutos = horas - horas_round ;// you have 0.6789
                float minutos_round = roundDown(minutos*60,0);
                if(horas_round == 0){
                    //textView16.setVisibility(View.INVISIBLE);
                    //editTextNumber5.setVisibility(View.INVISIBLE);
                    editTextNumber5.setText("00");
                }else{
                    textView16.setVisibility(View.VISIBLE);
                    editTextNumber5.setVisibility(View.VISIBLE);

                    editTextNumber5.setText(String.format("%02d", (int)horas_round));
                }
                if (minutos_round == 0) {
                    //textView17.setVisibility(View.INVISIBLE);
                    //editTextNumber6.setVisibility(View.INVISIBLE);
                    editTextNumber6.setText("00");
                }else{
                    //textView17.setVisibility(View.VISIBLE);
                    editTextNumber6.setVisibility(View.VISIBLE);
                    editTextNumber6.setText(Integer.toString((int)minutos_round));
                }
                connectButton.setVisibility(View.VISIBLE);
                connectButton.setEnabled(true);
            }
            editTextNumber4.setText(message);
        });
        viewModel.connect();
    }

    private void onConnectionStatus(Activity1MainViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:

                cross_check.setImageResource(R.drawable.check);

                //connectionText.setText(R.string.status_connected);
                //messageBox.setEnabled(true);
                //sendButton.setEnabled(true);
                //connectButton.setEnabled(true);
                //connectButton.setText(R.string.disconnect);
                //connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:

                cross_check.setImageResource(R.drawable.connecting);

                //connectionText.setText(R.string.status_connecting);
                //messageBox.setEnabled(false);
                //sendButton.setEnabled(false);
                //connectButton.setEnabled(false);
                //connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:

                cross_check.setImageResource(R.drawable.cross);

                //connectionText.setText(R.string.status_disconnected);
                //messageBox.setEnabled(false);
                //sendButton.setEnabled(false);
                //connectButton.setEnabled(true);
                //connectButton.setText(R.string.connect);
                //connectButton.setOnClickListener(v -> viewModel.connect());
                break;
        }
    }

   // @Override
    //public void KioskSetupFinish() {
    //    kioskmodeButton.setText("Disable Kiosk MODE");
    //}
    /*
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
                        disableKioskMode(mDevicePolicyManager,am);
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


    private void disableKioskMode(DevicePolicyManager devicePolicyManager,ActivityManager activityManager)
    {
        if(devicePolicyManager!=null && activityManager!=null){
            ComponentName mAdminComponentName = DeviceAdminReceiver.getComponentName(Activity1Main.this);
            devicePolicyManager.clearPackagePersistentPreferredActivities(mAdminComponentName, getPackageName());
            devicePolicyManager.clearDeviceOwnerApp(getApplication().getPackageName());
            if(activityManager.getLockTaskModeState()!=ActivityManager.LOCK_TASK_MODE_NONE)
            {
                this.stopLockTask();
                kioskmodeButton.setText(R.string.button_txt_enable_kiosk);
            }
        }
    }
*/

    public void ApiCall(){
        ApiUser user = new ApiUser(0,segundos,"MXN","1","","",dinero);
        Call<ApiUser> call1 = apiInterface.createUser(user);
        call1.enqueue(new Callback<ApiUser>() {
            @Override
            public void onResponse(Call<ApiUser> call, Response<ApiUser> response) {
                ApiUser user1 = response.body();
                Intent intent = new Intent(getBaseContext(), Activity21Credentials.class);
                intent.putExtra("username", user1.username);
                intent.putExtra("password", user1.password);
                intent.putExtra("ssid", "Renta_de_Wifi");
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), user1.username + " " + user1.password + " " + user1.id + " " , Toast.LENGTH_SHORT).show();
                //Log.d("laputaputa",user1.username + " " + user1.password + " " + user1.id + " ");
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

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static float roundDown(float d, int decimalPlace)
    {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_FLOOR).floatValue();
    }
}

 /* private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };*/