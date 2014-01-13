package si.komp.tribesascendstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import si.komp.tribesascendstats.adapters.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TimeDetails extends Activity {
	Context ctx;
	
	ArrayList<HashMap<String, String>> toPass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_details);
		Intent intent = getIntent();
		
		ctx = this;
		@SuppressWarnings("unchecked")
		HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("data");
		
		
		TextView tv1 = (TextView) findViewById(R.id.timeClassNameDetails);
		TextView tv2 = (TextView) findViewById(R.id.timeClasstimeDetails);
		setTitle(details.get("name") + " class usage");
		tv1.setText(details.get("name"));
		tv2.setText(details.get("timeForClass") + " mins");
		
		toPass = new ArrayList<HashMap<String,String>>();
		
		for(String key: details.keySet()){
			if(key.equals("name") || key.equals("timeForClass")){
				continue;
			}
			if(key.startsWith("map-")){
				HashMap<String, String> ins = new HashMap<String, String>();
				ins.put("name", key.replace("map-", ""));
				ins.put("value", details.get(key) + " mins");
				toPass.add(ins);
			}
		}

		Collections.sort(toPass, new CustomComparator());//this could be optimised to run when we are creating toPass hashmap	
		
		Adapter adapter = new Adapter(this, toPass);
		ListView lw = ((ListView) findViewById(R.id.detailsTimeClasses));
		lw.setAdapter(adapter);
		
		lw.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(ctx, toPass.get(position).get("name")+" -- "+toPass.get(position).get("value"), Toast.LENGTH_SHORT).show();
			}
		});
		
		
	      ImageView viewb = (ImageView) findViewById(R.id.timeTimeDetailsImage);

	        
			if(details.get("name").contains("Brute")){
				viewb.setImageResource(R.drawable.brute);
			}
			if(details.get("name").contains("Doombringer")){
				viewb.setImageResource(R.drawable.doombringer);
			}
			if(details.get("name").contains("Infiltrator")){
				viewb.setImageResource(R.drawable.infiltrator);
			}
			if(details.get("name").contains("Juggernaught")){
				viewb.setImageResource(R.drawable.juggernaught);
			}
			if(details.get("name").contains("n. pathfinder")){
				viewb.setImageResource(R.drawable.pathfinder);
			}
			if(details.get("name").contains("Pathfinder")){
				viewb.setImageResource(R.drawable.pathfinder);
			}
			if(details.get("name").contains("Raider")){
				viewb.setImageResource(R.drawable.raider);
			}
			if(details.get("name").contains("Sentinel")){
				viewb.setImageResource(R.drawable.sentinel);
			}
			if(details.get("name").contains("Soldier")){
				viewb.setImageResource(R.drawable.soldier);
			}
			if(details.get("name").contains("Technician")){
				viewb.setImageResource(R.drawable.technician);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent resultIntent = new Intent();
		switch (item.getItemId()) {
		case R.id.action_refresh:
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			break;
		case R.id.action_refresh2:
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

}


class CustomComparator implements Comparator<HashMap<String, String>> {
    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
    	try {
            if(Integer.parseInt(o1.get("value").replace(" mins", "")) >= Integer.parseInt(o2.get("value").replace(" mins", ""))){
            	return -1;
            }
            return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

    }
}
