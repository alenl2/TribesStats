package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import si.komp.tribesascendstats.R;

public class Adapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    final private ArrayList<HashMap<String, String>> data;

    public Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
        data = d;
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
            vi = inflater.inflate(R.layout.list_row, null);

        TextView text1 = (TextView) vi.findViewById(R.id.text1); // title
        TextView text2 = (TextView) vi.findViewById(R.id.text2); // artist name

        HashMap<String, String> dat = data.get(position);
        text1.setText(dat.get("name"));
        text2.setText(dat.get("value"));

        return vi;
    }
}
