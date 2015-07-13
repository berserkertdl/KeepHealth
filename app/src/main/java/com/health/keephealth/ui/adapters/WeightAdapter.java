package com.health.keephealth.ui.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.health.keephealth.R;
import com.health.keephealth.helper.vo.WeightEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/7/13 0013.
 */
public class WeightAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Object> items;
    private LayoutInflater inflater;
    private static final String weeks [] = new String []{"周日","周一","周二","周三","周四","周五","周六"};

    public WeightAdapter(Context mContext, ArrayList<Object> items) {
        this.mContext = mContext;
        this.items = items;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = items.get(position);
        if(item.getClass() == WeightEntity.class){
            convertView = inflater.inflate(R.layout.weight_list_item_layout,null);
            WeightEntity entity = (WeightEntity)item;
            Calendar cd = Calendar.getInstance();
            cd.setTime(entity.getAdd_time());
            ((TextView) convertView.findViewById(R.id.week)).setText(weeks[cd.get(Calendar.DAY_OF_WEEK)]);
            ((TextView) convertView.findViewById(R.id.date)).setText(cd.get(Calendar.DAY_OF_MONTH));
            String hour = cd.get(Calendar.DAY_OF_MONTH)+"".length()==1?"0":"" + cd.get(Calendar.DAY_OF_MONTH);
            String minute = cd.get(Calendar.MINUTE)+"".length()==1?"0":"" + cd.get(Calendar.MINUTE);
            ((TextView) convertView.findViewById(R.id.hour_min)).setText(hour+":"+minute);
            ((TextView) convertView.findViewById(R.id.weight)).setText(entity.getWeight()+" kg");
            convertView.setTag(entity);
        }else{
            ((TextView) convertView.findViewById(R.id.list_header_month_title)).setText("");
            ((TextView) convertView.findViewById(R.id.list_header_year_title)).setText("");
            convertView.setTag(null);
        }
        return convertView;
    }

}
