package com.android.custom.pickview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.custom.pickview.R;
import com.android.custom.pickview.view.CustomPickerView;

import java.util.List;

public class DataAdapter extends BaseAdapter {
    private List<String> mData;
    private Context context;
    private String checkStr = "";

    public DataAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.data_textview, parent, false);
            viewHolder.data_layout = convertView.findViewById(R.id.data_layout);
            viewHolder.textView = convertView.findViewById(R.id.data_text);
            viewHolder.data_img = convertView.findViewById(R.id.data_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setTextSize(CustomPickerView.dataSize);
        viewHolder.textView.setTextColor(CustomPickerView.dataColor);
        viewHolder.textView.setText(mData.get(position));
        if (checkStr.equals(mData.get(position)) && CustomPickerView.discolour) {
            viewHolder.textView.setTextColor(CustomPickerView.discolourColor);
        }
        if (checkStr.equals(mData.get(position)) && CustomPickerView.discolourHook) {
            viewHolder.data_img.setVisibility(View.VISIBLE);
        } else {
            viewHolder.data_img.setVisibility(View.GONE);
        }
        if (CustomPickerView.customHook != null) {
            viewHolder.data_img.setImageDrawable(CustomPickerView.customHook);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, CustomPickerView.dataHeight);
        viewHolder.textView.setLayoutParams(params);
        return convertView;
    }

    public void setList(List<String> data, String toString) {
        if (data != null && data.size() > 0) {
            mData = data;
        }
        checkStr = toString;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView textView;
        LinearLayout data_layout;
        ImageView data_img;
    }
}
