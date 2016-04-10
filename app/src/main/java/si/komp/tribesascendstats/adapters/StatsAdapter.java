package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import si.komp.tribesascendstats.R;
import si.komp.tribesascendstats.StatItem;

public class StatsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private final List<StatItem> data;

    public StatsAdapter(@NonNull Activity a, List<StatItem> d) {
        data = d;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) { return position; }
    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row, null);

        StatItem dat = data.get(position);

        TextView text1 = (TextView) vi.findViewById(R.id.text1);
        text1.setText(dat.getName());

        TextView text2 = (TextView) vi.findViewById(R.id.text2);
        text2.setText(dat.getValue());

        return vi;
    }
}
