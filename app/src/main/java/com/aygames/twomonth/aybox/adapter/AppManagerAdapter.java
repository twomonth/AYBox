package com.aygames.twomonth.aybox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.domain.AppInfo;

import java.util.List;

/**
 * Created by wf05 on 2017/7/6.
 */

public class AppManagerAdapter extends BaseAdapter {

    private Context context;
    private List<AppInfo> list;

    public AppManagerAdapter(Context context, List<AppInfo> list){
        this.context=context;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_appinfo,null);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.info = (TextView) convertView.findViewById(R.id.tv_message);
//            holder.button = (Button) convertView.findViewById(R.id.bt_start);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setImageDrawable(list.get(position).getApp_icon());
        holder.title.setText(list.get(position).getApp_name());
        holder.info.setText(list.get(position).getPackagename());
        return convertView;
    }

    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
    }
}
