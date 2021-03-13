package com.curzar.androidkiosksample;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.curzar.androidkiosksample.database.SettingViewModel;
import com.curzar.androidkiosksample.model.Setting;

import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Map;

public class Activity2Settings extends AppCompatActivity implements KioskInterface{
    public static final String EXTRA_REPLY = "com.curzar.androidkiosksample.wordlistsql.REPLY";
    private EditText  txtemail;
    private EditText  txtpassword;
    private Spinner  spincurrency;
    private EditText  txtdevice;
    private EditText  txtcharacteristic;
    private EditText  txtservice;
    private TextView txtviewcurrency;
    private EditText  txttimeperunitofcurrency;
    private Button btnSaveSettings;
    private Button btn_call_second_activity,button3;

    private ToggleButton toggleButton;
    private DevicePolicyManager mDevicePolicyManager;
    private ActivityManager am;

    List<Setting> settings1 = null;
    private SettingViewModel mSettingViewModel;


    @Override
    public void KioskSetupFinish() {
        button3.setText("Disable Kiosk MODE");
    }
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2settings);

        toggleButton = (ToggleButton)findViewById(R.id.toggle_device_admin_1);
        button3 = (Button) findViewById(R.id.button3);

        btn_call_second_activity = (Button) findViewById(R.id.btn_call_second_activity);
        btnSaveSettings = (Button) findViewById(R.id.btnSaveSettings);
        txtemail=(EditText ) findViewById(R.id.txtemail);
        txtpassword=(EditText ) findViewById(R.id.txtPassword);
        txttimeperunitofcurrency=(EditText) findViewById(R.id.txttimeperunitofcurrency);
        spincurrency=(Spinner) findViewById(R.id.spincurrency);
        txtcharacteristic=(EditText) findViewById(R.id.txt_characteristic);
        txtdevice=(EditText) findViewById(R.id.txt_device);
        txtservice=(EditText) findViewById(R.id.txt_service);
        txtviewcurrency= (TextView) findViewById(R.id.txtviewcurrency);
        mSettingViewModel= new ViewModelProvider(this).get(SettingViewModel.class);
        Spinner spinner = (Spinner) findViewById(R.id.spincurrency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mSettingViewModel.getAllSettings().observe(this,settings ->{
            settings1 = settings;

                for (Setting setting : settings){
                    switch (setting.getName()){
                        case   "email":
                            txtemail.setText(setting.getValue());
                            break;
                        case   "password":
                            txtpassword.setText(setting.getValue());
                            break;
                        case   "timeperunitofcurrency":
                            txttimeperunitofcurrency.setText(setting.getValue());
                            break;
                        case   "currency":
                            spincurrency.setSelection(adapter.getPosition(setting.getValue()));
                            txtviewcurrency.setText(setting.getValue());
                            break;
                        case   "device":
                            txtdevice.setText(setting.getValue());
                            break;
                        case   "service":
                            txtservice.setText(setting.getValue());
                            break;
                        case   "characteristic":
                            txtcharacteristic.setText(setting.getValue());
                            break;
                        default:
                            break;
                    }

                }

        });

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingViewModel.updateByName("email",txtemail.getText().toString());
                mSettingViewModel.updateByName("password",txtpassword.getText().toString());
                mSettingViewModel.updateByName("currency",spinner.getSelectedItem().toString());
                mSettingViewModel.updateByName("device",txtdevice.getText().toString());
                mSettingViewModel.updateByName("characteristic",txtcharacteristic.getText().toString());
                mSettingViewModel.updateByName("service",txtservice.getText().toString());
                mSettingViewModel.updateByName("timeperunitofcurrency",txttimeperunitofcurrency.getText().toString());

                // Intent replyIntent = new Intent();
               // if (TextUtils.isEmpty(txtminpertypeofchange.getText())) {
                 //   setResult(RESULT_CANCELED, replyIntent);
                //} else {
                //    String word = txtminpertypeofchange.getText().toString();
                //    replyIntent.putExtra(EXTRA_REPLY, word);
               //     setResult(RESULT_OK, replyIntent);
               // }
                //finish();
            }
        });

        btn_call_second_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activity2Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });


        toggleButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {


            }
        });




        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);


        if(am.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE)
        {
            button3.setText(R.string.button_txt_enable_kiosk);
        }
        else
        {
            button3.setText(R.string.button_txt_disable_kiosk);
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    }



    public void askAdminPassword(){
        final Dialog dialog = new Dialog(Activity2Settings.this);
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
                        Toast.makeText(Activity2Settings.this, "Please enter valid password.", Toast.LENGTH_SHORT).show();
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
            ComponentName mAdminComponentName = DeviceAdminReceiver.getComponentName(Activity2Settings.this);
            devicePolicyManager.clearPackagePersistentPreferredActivities(mAdminComponentName, getPackageName());
            devicePolicyManager.clearDeviceOwnerApp(getApplication().getPackageName());
            if(activityManager.getLockTaskModeState()!=ActivityManager.LOCK_TASK_MODE_NONE)
            {
                this.stopLockTask();
                button3.setText(R.string.button_txt_enable_kiosk);
            }
        }
    }








}