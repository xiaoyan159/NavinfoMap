package com.navinfo.navimap.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.navinfo.navimap.example.R;
import com.navinfo.navimap.example.entity.StartActivityEntity;

import java.util.ArrayList;
import java.util.List;

public class StartActivityAdapter extends BaseAdapter {
    private Context mContext;
    private List<StartActivityEntity> entityList;
    private LayoutInflater layoutInflater;

    public StartActivityAdapter(Context mContext, List<StartActivityEntity> entityList) {
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
        if (entityList == null) {
            this.entityList = new ArrayList<>();
        } else {
            this.entityList =entityList;
        }
    }

    @Override
    public int getCount() {
        return entityList!=null?entityList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_lv_start, null);
        TextView btn = convertView.findViewById(R.id.btn_item_lv_start);
        btn.setText(entityList.get(position).getName());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, entityList.get(position).getClazz());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
