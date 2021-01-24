package com.curzar.androidkiosksample;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.curzar.androidkiosksample.Activity3BleScanDevices;
import com.curzar.androidkiosksample.R;

import java.util.ArrayList;

public class Activity3BleScanDevicesAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;
    public Activity3BleScanDevicesAdapter(Activity3BleScanDevices Activity3BleScanDevices) {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
        mInflator = Activity3BleScanDevices.getLayoutInflater();
    }
    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }
    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Activity3BleScanDevices.ViewHolder viewHolder;
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new Activity3BleScanDevices.ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (Activity3BleScanDevices.ViewHolder) view.getTag();
        }
        BluetoothDevice device = mLeDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getAddress());
        return view;
    }
}
