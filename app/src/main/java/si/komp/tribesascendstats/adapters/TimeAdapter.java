package si.komp.tribesascendstats.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import si.komp.tribesascendstats.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeAdapter extends BaseAdapter {
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null; 
 
    public TimeAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row_time, null);
 
        TextView text1 = (TextView)vi.findViewById(R.id.textTimeClass); // title
        TextView text2 = (TextView)vi.findViewById(R.id.textTimeTime); // artist name
        ImageView viewb = (ImageView) vi.findViewById(R.id.classImage);

        HashMap<String, String> dat = data.get(position);
        

		if(dat.get("name").contains("Brute")){
			viewb.setImageResource(R.drawable.brute);
		}
		if(dat.get("name").contains("Doombringer")){
			viewb.setImageResource(R.drawable.doombringer);
		}
		if(dat.get("name").contains("Infiltrator")){
			viewb.setImageResource(R.drawable.infiltrator);
		}
		if(dat.get("name").contains("Juggernaught")){
			viewb.setImageResource(R.drawable.juggernaught);
		}
		if(dat.get("name").contains("n. pathfinder")){
			viewb.setImageResource(R.drawable.pathfinder);
		}
		if(dat.get("name").contains("Pathfinder")){
			viewb.setImageResource(R.drawable.pathfinder);
		}
		if(dat.get("name").contains("Raider")){
			viewb.setImageResource(R.drawable.raider);
		}
		if(dat.get("name").contains("Sentinel")){
			viewb.setImageResource(R.drawable.sentinel);
		}
		if(dat.get("name").contains("Soldier")){
			viewb.setImageResource(R.drawable.soldier);
		}
		if(dat.get("name").contains("Technician")){
			viewb.setImageResource(R.drawable.technician);
		}
		
        text1.setText(dat.get("name")); 
        text2.setText(dat.get("timeForClass") + " mins"); 

        return vi;
    }
}