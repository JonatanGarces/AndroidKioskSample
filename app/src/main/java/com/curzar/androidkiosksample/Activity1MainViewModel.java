package com.curzar.androidkiosksample;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Activity1MainViewModel  extends AndroidViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BluetoothManager bluetoothManager;
    @Nullable
    private SimpleBluetoothDeviceInterface deviceInterface;
    private MutableLiveData<String> messagesData = new MutableLiveData<>();
    private MutableLiveData<CommunicateViewModel.ConnectionStatus> connectionStatusData = new MutableLiveData<>();
    private MutableLiveData<String> deviceNameData = new MutableLiveData<>();
    private MutableLiveData<String> messageData = new MutableLiveData<>();
    private StringBuilder messages = new StringBuilder();
    private int pulses=0;
    private float money=0;
    private String deviceName;
    private String mac;
    private boolean connectionAttemptedOrMade = false;
    private boolean viewModelSetup = false;

    public Activity1MainViewModel(@NotNull Application application) {
        super(application);
    }

    public boolean setupViewModel(String deviceName, String mac) {
        if (!viewModelSetup) {
            viewModelSetup = true;
            bluetoothManager = BluetoothManager.getInstance();
            if (bluetoothManager == null) {
                toast(R.string.bluetooth_unavailable);
                return false;
            }
            this.deviceName = deviceName;
            this.mac = mac;
            deviceNameData.postValue(deviceName);
            connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.DISCONNECTED);
        }
        return true;
    }

    public void connect() {
        if (!connectionAttemptedOrMade) {
            compositeDisposable.add(bluetoothManager.openSerialDevice(mac)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(device -> onConnected(device.toSimpleDeviceInterface()), t -> {
                        toast(R.string.connection_failed);
                        connectionAttemptedOrMade = false;
                        connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.DISCONNECTED);
                    }));
            connectionAttemptedOrMade = true;
            connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.CONNECTING);
        }
    }

    public void disconnect() {
        if (connectionAttemptedOrMade && deviceInterface != null) {
            connectionAttemptedOrMade = false;
            bluetoothManager.closeDevice(deviceInterface);
            deviceInterface = null;
            connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.DISCONNECTED);
        }
    }

    private void onConnected(SimpleBluetoothDeviceInterface deviceInterface) {
        this.deviceInterface = deviceInterface;
        if (this.deviceInterface != null) {
            connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.CONNECTED);
            this.deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, t -> toast(R.string.message_send_error));
            toast(R.string.connected);
            messages = new StringBuilder();
            messagesData.postValue(messages.toString());
        } else {
            toast(R.string.connection_failed);
            connectionStatusData.postValue(CommunicateViewModel.ConnectionStatus.DISCONNECTED);
        }
    }

    private void onMessageReceived(String message) {
        if(isInt(message.trim())){
            pulses = pulses + Integer.parseInt(message.trim());
        }
        money =(float) pulses *(float)  0.5;
        messagesData.postValue(Float.toString(round(money,2)));
    }

    private void onMessageSent(String message) {
        messages.append(getApplication().getString(R.string.you_sent)).append(": ").append(message).append('\n');
        messagesData.postValue(messages.toString());
        messageData.postValue("");
    }
    public void sendMessage(String message) {
        if (deviceInterface != null && !TextUtils.isEmpty(message)) {
            deviceInterface.sendMessage(message);
        }
    }

    public void Zero() {
        pulses = 0 ;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        bluetoothManager.close();
    }

    private void toast(@StringRes int messageResource) { Toast.makeText(getApplication(), messageResource, Toast.LENGTH_LONG).show(); }
    public LiveData<String> getMessages() { return messagesData; }
    public LiveData<CommunicateViewModel.ConnectionStatus> getConnectionStatus() { return connectionStatusData; }
    public LiveData<String> getDeviceName() { return deviceNameData; }
    public LiveData<String> getMessage() { return messageData; }
    enum ConnectionStatus {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
    static boolean isInt(String s)  // assuming integer is in decimal number system
    {
        for(int a=0;a<s.length();a++)
        {
            if(a==0 && s.charAt(a) == '-') continue;
            if( !Character.isDigit(s.charAt(a)) ) return false;
        }
        return true;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
