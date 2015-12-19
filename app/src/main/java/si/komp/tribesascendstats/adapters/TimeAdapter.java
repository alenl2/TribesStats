package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import si.komp.tribesascendstats.R;
import si.komp.tribesascendstats.TribesUtils;

public class TimeAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private final ArrayList<HashMap<String, String>> data;
    private final TribesUtils utils;

    public TimeAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        data = d;
        utils = new TribesUtils(a);
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_time, null);

        HashMap<String, String> dat = data.get(position);
        String className = dat.get("name"), timeForClass = dat.get("timeForClass");

        Integer classDrawableId = utils.getClassDrawableId(className);
        if (classDrawableId != null)
            ((ImageView) vi.findViewById(R.id.classImage)).setImageResource(classDrawableId);

        TextView text1 = (TextView) vi.findViewById(R.id.textTimeClass);
        text1.setText(utils.translateClassName(dat.get("name")));

        TextView text2 = (TextView) vi.findViewById(R.id.textTimeTime);
        String minutesFormatter = utils.getString(R.string.minutes_short_format);
        text2.setText(String.format(minutesFormatter, timeForClass));

        return vi;
    }
}