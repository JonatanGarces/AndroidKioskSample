package com.curzar.androidkiosksample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.curzar.androidkiosksample.database.SettingViewModel;
import com.curzar.androidkiosksample.model.Setting;

import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Map;

public class Activity2Settings extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.curzar.androidkiosksample.wordlistsql.REPLY";
    private EditText mEditSettingView;
    private EditText  txtemail;
    private EditText  txtpassword;
    private Spinner  spincurrency;
    private EditText  txtdevice;
    private EditText  txtcharacteristic;
    private EditText  txtservice;
    private TextView txtviewcurrency;
    private EditText  txttimeperunitofcurrency;
    private Button btnSaveSettings;
    private Button btn_call_second_activity;
    List<Setting> settings1 = null;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private SettingViewModel mSettingViewModel;
    Map<String, String> inputs;

    /*public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK){
            Setting setting = new Setting(data.getStringExtra(Activity2Settings.EXTRA_REPLAY));
            mSettingViewModel.update(setting);
        }else{
            Toast.makeText(
                    getApplicationContext(),
                    "No se guardo",
                    Toast.LENGTH_LONG).show();
        }
    }

     */

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2settings);
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
        //mSettingViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SettingViewModel.class);

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
        // Create an ArrayAdapter using the string array and a default spinner layout

        //DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //devicePolicyManager.clearDeviceOwnerApp(this.getPackageName());

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
    }



}