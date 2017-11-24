package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Set;

/*
 * 蓝牙开发
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "lvkaixuan";
    private static final int REQUEST_ENABLE = 1;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getBlueToothDetail();
    }

    //获取蓝牙详情
    private void getBlueToothDetail() {
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.d(TAG, "蓝牙名称: " + name + "蓝牙地址: " + address);
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "已配对蓝牙数: " + devices.size());
        for (BluetoothDevice bonddevice : devices) {
            Log.d(TAG, "已配对蓝牙 name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
        }
    }

    private void init() {
        //获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG, "蓝牙是否打开: " + mBluetoothAdapter.isEnabled());
        //判断当前蓝牙是否打开
        if (!mBluetoothAdapter.isEnabled()) {
            //如果蓝牙不可用,弹出提示询问用户是否打开蓝牙
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, REQUEST_ENABLE);
        }
    }

    //开始搜索蓝牙
    public void start(View view) {
        Log.d(TAG, "start: 开始搜索...");
        mBluetoothAdapter.startDiscovery();
        registerReceiver();
    }

    //注册广播搜索蓝牙
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        //发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备连接状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    //停止搜索
    public void end(View view) {
        mBluetoothAdapter.cancelDiscovery();
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(scanDevice == null || scanDevice.getName() == null) return;
                Log.d(TAG, "搜索到了: name="+scanDevice.getName()+"  address="+scanDevice.getAddress());

            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            }
        }

    };
}
