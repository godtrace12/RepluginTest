package com.hikvision.daijun.wifitest;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ListView lvWifiList;
    private Button btnWifiConnect;
    private List<ScanResult> mWifiList = null;
    private HashMap<String,ScanResult> mWifiMap = null;
    private WifiListViewAdapter wifiListAdapter = null;
    private WifiManager wifiManager;

    private static final int WIFICIPHER_NOPASS = 0;
    private static final int WIFICIPHER_WEP = 1;
    private static final int WIFICIPHER_WPA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initView();
    }

    private void findViews() {
        lvWifiList = (ListView) findViewById(R.id.lvWifiList);
        btnWifiConnect = (Button) findViewById(R.id.btnWifiConnect);
    }

    private void initView(){
        wifiListAdapter = new WifiListViewAdapter(MainActivity.this);
        lvWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lvWifiList.setAdapter(wifiListAdapter);
        btnWifiConnect.setOnClickListener(this);
        mWifiMap = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean result = wifiManager.setWifiEnabled(true);
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            boolean scanResult = wifiManager.startScan();
            if (scanResult) {
//                mWifiList = wifiManager.getScanResults();
                List<ScanResult> wifiListTmp = wifiManager.getScanResults();    //getScanResults获取到的wifi列表有重复的，需要进行去重
                for (int i=0;i<wifiListTmp.size();i++) {
                    if (!mWifiMap.containsKey(wifiListTmp.get(i).SSID)) {
                        mWifiMap.put(wifiListTmp.get(i).SSID,wifiListTmp.get(i));
                    }
                }
                mWifiList = new ArrayList<>(mWifiMap.values());
                wifiListAdapter.setData(mWifiList);
                wifiListAdapter.notifyDataSetChanged();
            }
        }
    }


    private WifiConfiguration createWifiConfiguration(String ssid,String password,int type){
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        //指定对应的SSID
        config.SSID = "\"" + ssid + "\"";

        //不需要密码的场景
        if(type == WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //以WEP加密的场景
        } else if(type == WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
            //以WPA加密的场景，自己测试时，发现热点以WPA2建立时，同样可以用这种配置连接
        } else if(type == WIFICIPHER_WPA) {
            config.preSharedKey = "\""+password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private void connectSelectedWifi(){
        if (!mWifiMap.isEmpty()) {
            ScanResult selWifi = mWifiMap.get("OPPO dj");
            if (selWifi != null) {
                int netId = wifiManager.addNetwork(createWifiConfiguration(selWifi.SSID,"12345678",WIFICIPHER_WPA));
                boolean enable = wifiManager.enableNetwork(netId,true);
                if (enable) {
                    Log.e(TAG, "connectSelectedWifi: connect wifi success");
                    Toast.makeText(MainActivity.this,"wifi连接成功",Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "connectSelectedWifi: connect wifi fail");
                    Toast.makeText(MainActivity.this,"wifi连接失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnWifiConnect) {
            connectSelectedWifi();
        }
    }
}
