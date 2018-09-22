package com.hikvision.daijun.messengerclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivityClient";
    private Button btn1;
    private Button btn2;
    //发送
    private Messenger mesgerSend;
    //接收
    private Messenger mesgerAccept;
    private ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initSendMessenger();
        initServiceConnection();
    }

    private void initSendMessenger() {
        mesgerAccept = new Messenger(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2001:
                        Log.e(TAG, "handleMessage: 收到服务端的回复");
                        Log.e(TAG, "handleMessage: service_reply="+msg.getData().getString("service_reply"));
                        break;
                }
                super.handleMessage(msg);
            }
        });
    }

    private void initServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "onServiceConnected: ");
                mesgerSend = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected: ");
            }
        };
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.hikvision.daijun.messengerservice","com.hikvision.daijun.messengerservice.PluginComService"));
//        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void connectToService(){
        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.hikvision.daijun.messengerservice","com.hikvision.daijun.messengerservice.PluginComService"));
        intent.setAction("com.hikvision.daijun.messengerclient");
        intent.setPackage("com.hikvision.daijun.messengerservice");
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void findViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn1) {
            // TODO: 2018/9/6 发送消息
            sendMessageToPlugin();
        } else if (viewId == R.id.btn2) {
            connectToService();
        }

    }

    public void sendMessageToPlugin(){
        Log.e(TAG, "sendMessageToPlugin: ");
        Message msg = Message.obtain();
        msg.what = 1001;
        msg.arg1 = 1001;
        msg.replyTo = mesgerAccept;
        Bundle bundle = new Bundle();
        bundle.putString("msg","我要与宿主通信");
        bundle.putString("msgJson",getJsonString());
        msg.setData(bundle);
        try {
            mesgerSend.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "sendMessageToPlugin: 消息发送异常"+e.getMessage());
        }

    }

    private String getJsonString(){
        MsgContent msgContent = new MsgContent();
        msgContent.alarmId =13;
        msgContent.alarmTime = "2018-09-07 11:12";
        msgContent.alarmTypeCode = 3;
        msgContent.alarmTypeName = "人脸侦测报警";
        msgContent.channelNo = 3;
        PushMsgContent pushMsgContent = new PushMsgContent();
        pushMsgContent.msgContent = msgContent;
        pushMsgContent.msgType = 1;
        pushMsgContent.sendTime = "2018-09-07 11:14";
        pushMsgContent.title = "报警";
        Gson gs = new Gson();
        String strMsg = gs.toJson(pushMsgContent);
        return strMsg;
    }

    @Override
    protected void onDestroy() {
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }
}
