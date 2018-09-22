package com.hikvision.daijun.replugintest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivityHost";
    private Button btnPlugin1;
    private Button btnPluginOutMode;
    private GetPluginInfoTask getPluginInfoTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initClickListener();
        initData();
    }

    private void findViews() {
        btnPlugin1 = (Button) findViewById(R.id.btnPlugin1);
        btnPluginOutMode = (Button) findViewById(R.id.btnPluginOutMode);
    }

    private void initClickListener(){
        btnPlugin1.setOnClickListener(this);
        btnPluginOutMode.setOnClickListener(this);
    }

    private void initData() {
        if (getPluginInfoTask != null && !getPluginInfoTask.isCancelled()) {
            getPluginInfoTask.cancel(true);
            getPluginInfoTask = null;
        }
        getPluginInfoTask = new GetPluginInfoTask();
        if (getPluginInfoTask.getStatus() == AsyncTask.Status.PENDING) {
            getPluginInfoTask.execute();
        }

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btnPlugin1:
                if (RePlugin.isPluginInstalled("plugin1")) {
                    RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("plugin1", "com.hikvision.daijun.plugin1.MainActivity"));
                } else {
                    Toast.makeText(MainActivity.this,"plugi no found,you must install plugin1 first!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnPluginOutMode:
                if (RePlugin.isPluginInstalled("pluginoutmode")) {
                    Toast.makeText(MainActivity.this, "外置plugin已安装，准备直接打开", Toast.LENGTH_SHORT).show();
                    RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("pluginoutmode", "com.hikvision.daijun.pluginoutmode.MainActivity"));
                } else {
                    Toast.makeText(MainActivity.this, "外置plugin未安装，请先进行安装", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private class GetPluginInfoTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            simulateInstallExternalPlugin();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    /**
     * 模拟安装或升级（覆盖安装）外置插件
     * 注意：为方便演示，外置插件临时放置到Host的assets/external目录下，具体说明见README</p>
     */
    private void simulateInstallExternalPlugin() {
        String demo3Apk= "pluginoutmode.apk";
        String demo3apkPath = "external" + File.separator + demo3Apk;

        // 文件是否已经存在？直接删除重来
        String pluginFilePath = getFilesDir().getAbsolutePath() + File.separator + demo3Apk;
        Log.e(TAG, "simulateInstallExternalPlugin: pluginFilePath="+pluginFilePath);
        File pluginFile = new File(pluginFilePath);
        Log.e(TAG, "simulateInstallExternalPlugin: apkPath="+demo3apkPath);
        if (pluginFile.exists()) {
            FileUtils.deleteQuietly(pluginFile);
        }

        // 开始复制
        copyAssetsFileToAppFiles(demo3apkPath, demo3Apk);
        PluginInfo info = null;
        if (pluginFile.exists()) {
            info = RePlugin.install(pluginFilePath);
        }

        if (info != null) {
//            RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(info.getName(), "com.qihoo360.replugin.sample.demo3.MainActivity"));
            Log.e(TAG, "simulateInstallExternalPlugin: install plugin success");
        } else {
            Log.e(TAG, "simulateInstallExternalPlugin: install plugin failed");
            Toast.makeText(MainActivity.this, "install external plugin failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从assets目录中复制某文件内容
     *  @param  assetFileName assets目录下的Apk源文件路径
     *  @param  newFileName 复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        int buffsize = 1024;

        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[buffsize];
            while((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
