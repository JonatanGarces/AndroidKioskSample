package com.curzar.androidkiosksample;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.curzar.androidkiosksample.database.SettingViewModel;
import com.curzar.androidkiosksample.model.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity1Main extends AppCompatActivity implements KioskInterface {

    private TextView mTextMessage;
    private Button btn_call_first_activity,kioskmodeButton,zeroButton,connectButton;
    private EditText editTextNumber4;
    private Activity1MainViewModel viewModel;
    private SettingViewModel mSettingViewModel;
    private String device_name = "";
    private String device_mac = "";
    private Setting setting;

    private DevicePolicyManager mDevicePolicyManager;
    private ActivityManager am;

    @Override
    protected void onResume() {
        super.onResume();


        mSettingViewModel.getAllSettings().observe(this,settings ->{
            for (Setting setting : settings){
                switch (setting.getName()){
                    case   "devicename":
                        device_name =setting.getValue();
                        break;
                    case   "devicemac":
                        device_mac =setting.getValue();
                        break;
                    default:
                        break;
                }
            }
        });


       // Log.d("device_bl",device_name);
       // Log.d("device_bl",device_mac);


        if (!viewModel.setupViewModel(device_name,device_mac)) {
            finish();
            return;
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1main);
        getSupportActionBar().hide();

        viewModel = ViewModelProviders.of(this).get(Activity1MainViewModel.class);
        mSettingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        am                      = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mDevicePolicyManager    = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mTextMessage            = (TextView) findViewById(R.id.message);
        kioskmodeButton         = findViewById(R.id.btnkioskmode);
        zeroButton              = findViewById(R.id.communicate_zero);
        connectButton              = findViewById(R.id.button);

        if(am.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE)
        {
            kioskmodeButton.setText(R.string.button_txt_enable_kiosk);
        }
        else
        {
            kioskmodeButton.setText(R.string.button_txt_disable_kiosk);
        }

        kioskmodeButton.setOnClickListener(new View.OnClickListener() {
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

        btn_call_first_activity=(Button) findViewById(R.id.btn_call_first_activity);
        btn_call_first_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activity1Main.this, Activity2Settings.class);
                startActivity(intent);
            }
        });


        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.no_messages);
            }
            // messagesView.setText(message);
        });
        viewModel.getMessage().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                editTextNumber4.setText(message);
            }
        });

    }


    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                //connectionText.setText(R.string.status_connected);
                //messageBox.setEnabled(true);
                //sendButton.setEnabled(true);
                //connectButton.setEnabled(true);
                //connectButton.setText(R.string.disconnect);
                //connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
                //connectionText.setText(R.string.status_connecting);
                //messageBox.setEnabled(false);
                //sendButton.setEnabled(false);
                //connectButton.setEnabled(false);
                //connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:
                //connectionText.setText(R.string.status_disconnected);
                //messageBox.setEnabled(false);
                //sendButton.setEnabled(false);
                //connectButton.setEnabled(true);
                //connectButton.setText(R.string.connect);
                connectButton.setOnClickListener(v -> viewModel.connect());
                break;
        }
    }

    @Override
    public void KioskSetupFinish() {
        kioskmodeButton.setText("Disable Kiosk MODE");
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