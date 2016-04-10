package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import si.komp.tribesascendstats.R;
import si.komp.tribesascendstats.StatItem;
import si.komp.tribesascendstats.TribesUtils;

public class TimeAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private final List<StatItem> data;

    public TimeAdapter(Activity a, List<StatItem> d) {
        data = d;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_time, null);

        StatItem dat = data.get(position);
        String className = dat.getExtra().toLowerCase(), timeForClass = dat.getValue();

        try {
            ((ImageView) vi.findViewById(R.id.classImage)).setImageResource(TribesUtils.CLASS_DRAWABLES.get(className));
        } catch (Exception e) {
            new IllegalArgumentException(className, e).printStackTrace();
        }
        ((TextView) vi.findViewById(R.id.textTimeClass)).setText(TribesUtils.CLASS_STRINGS.get(className));
        ((TextView) vi.findViewById(R.id.textTimeTime)).setText(timeForClass);

        return vi;
    }
}