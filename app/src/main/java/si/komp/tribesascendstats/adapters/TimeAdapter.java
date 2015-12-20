package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
    @NonNull
    private final ArrayList<HashMap<String, String>> data;
    @NonNull
    private final Context context;

    public TimeAdapter(@NonNull Activity a, @NonNull ArrayList<HashMap<String, String>> d) {
        data = d;
        context = a;
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
        String className = dat.get("name").toLowerCase(), timeForClass = dat.get("timeForClass");

        try {
            ((ImageView) vi.findViewById(R.id.classImage)).setImageResource(TribesUtils.CLASS_DRAWABLES.get(className));
        } catch (Exception e) {
            new IllegalArgumentException(className, e).printStackTrace();
        }

        ((TextView) vi.findViewById(R.id.textTimeClass)).setText(TribesUtils.CLASS_STRINGS.get(className));

        ((TextView) vi.findViewById(R.id.textTimeTime)).setText(String.format(context.getString(R.string.minutes_short_format), timeForClass));

        return vi;
    }
}