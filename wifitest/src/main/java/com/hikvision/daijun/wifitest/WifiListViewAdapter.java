package com.hikvision.daijun.wifitest;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/8/29 16:11
 */
public class WifiListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder viewHolder;
    private List<ScanResult> mWifiList = null;

    public WifiListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ScanResult> wifiList){
        this.mWifiList = wifiList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mWifiList != null) {
            count = mWifiList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        ScanResult item = null;
        if (position >= 0 && getCount() > position) {
            item = mWifiList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
       return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_wifi, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ScanResult item = (ScanResult) getItem(position);
        viewHolder.tvWifiName.setText(item.SSID+"  强度-"+item.level);
        return convertView;
    }

    static class ViewHolder{
        public TextView tvWifiName;

        public ViewHolder(View view) {
            tvWifiName = (TextView) view.findViewById(R.id.tvWifiName);
        }

    }


}
