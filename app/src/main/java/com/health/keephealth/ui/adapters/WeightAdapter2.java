package com.health.keephealth.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.health.keephealth.R;
import com.health.keephealth.helper.vo.WeightEntity;
import com.health.swipelistview.SwipeListView;

import java.util.Calendar;
import java.util.List;

//import com.health.swipelistview.SwipeListView;

/**
 * Created by Administrator on 2015/7/13 0013.
 */
public class WeightAdapter2 extends BaseAdapter{

    private Context context;
    private List items;
    private LayoutInflater inflater;
    private static final String weeks[] = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private OnClickListener mClickListener;

    public interface OnClickListener {
        void onEditClick(View v, final Object item);
        void onDeleteClick(View v, final Object item);

    }

    public void setClickListener(OnClickListener onClickListener){
        this.mClickListener = onClickListener;
    }

    public WeightAdapter2(Context mContext, List items) {
        this.context = mContext;
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
        if (item.getClass() == WeightEntity.class) {
            convertView = inflater.inflate(R.layout.weight_list_item, parent,false);
            final WeightEntity entity = (WeightEntity) item;
            Calendar cd = Calendar.getInstance();
            cd.setTime(entity.getAdd_time());
            ((TextView) convertView.findViewById(R.id.week)).setText(weeks[cd.get(Calendar.DAY_OF_WEEK)]);
            ((TextView) convertView.findViewById(R.id.date)).setText(cd.get(Calendar.DAY_OF_MONTH)+"");
            String hour = cd.get(Calendar.HOUR_OF_DAY) + "".length() == 1 ? "0" : "" + cd.get(Calendar.HOUR_OF_DAY);
            String minute = cd.get(Calendar.MINUTE) + "".length() == 1 ? "0" : "" + cd.get(Calendar.MINUTE);
            ((TextView) convertView.findViewById(R.id.hour_min)).setText(hour + ":" + minute);
            ((TextView) convertView.findViewById(R.id.weight)).setText(String.format("%g kg", entity.getWeight()));
            convertView.setTag(entity);
        } else {
            convertView = inflater.inflate(R.layout.weight_list_item_title_layout, null);
            String strs[] = (String[]) item;
            ((TextView) convertView.findViewById(R.id.list_header_month_title)).setText(String.format("%s 月",strs[1]));
            ((TextView) convertView.findViewById(R.id.list_header_year_title)).setText(strs[0]);
            convertView.setTag(null);
        }
        return convertView;
    }

}
