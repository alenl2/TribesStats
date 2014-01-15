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
import android.widget.TextView;

 

public class DetailsAdapter extends BaseAdapter {
    private final ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null; 
 
    public DetailsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        data=d;
        inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.list_row_match, null);
 
        TextView text1 = (TextView)vi.findViewById(R.id.playerName); 


        HashMap<String, String> dat = data.get(position);
        text1.setText(dat.get("name")); 
        for(String key: dat.keySet()){
        	if(key.equals("name")){
        		continue;
        	}
        	if(key.startsWith("Pathfinder")){
        		vi.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass1)).setText("Pathfinder");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage1)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills1)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime1)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Soldier")){
        		vi.findViewById(R.id.layout2).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass2)).setText("Soldier");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage2)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills2)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime2)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Brute")){
        		vi.findViewById(R.id.layout3).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass3)).setText("Brute");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage3)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills3)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime3)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Sentinel")){
        		vi.findViewById(R.id.layout4).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass4)).setText("Sentinel");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage4)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills4)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime4)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Juggernaught")){
        		vi.findViewById(R.id.layout5).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass5)).setText("Juggernaught");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage5)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills5)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime5)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Technician")){
        		vi.findViewById(R.id.layout6).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass6)).setText("Technician");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage6)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills6)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime6)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Raider")){
        		vi.findViewById(R.id.layout7).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass7)).setText("Raider");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage7)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills7)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime7)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Infiltrator")){
        		vi.findViewById(R.id.layout8).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass8)).setText("Infiltrator");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage8)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills8)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime8)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        	if(key.startsWith("Doombringer")){
        		vi.findViewById(R.id.layout9).setVisibility(View.VISIBLE);
        		((TextView)vi.findViewById(R.id.playerClass9)).setText("Doombringer");
        		if(key.contains("ClassDemage")){
        			((TextView)vi.findViewById(R.id.playerDemage9)).setText(dat.get(key));
        		}
        		if(key.contains("ClassKills")){
        			((TextView)vi.findViewById(R.id.playerKills9)).setText(dat.get(key));
        		}
        		if(key.contains("ClassTimePlayed")){
        			((TextView)vi.findViewById(R.id.playerTime9)).setText(dat.get(key).replace("minutes", "min"));
        		}
        	}
        }
    	
        
        
        
        return vi;
    }
}
