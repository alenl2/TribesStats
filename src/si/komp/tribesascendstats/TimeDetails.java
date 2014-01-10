package si.komp.tribesascendstats;

import java.util.ArrayList;
import java.util.HashMap;

import si.komp.tribesascendstats.adapters.Adapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TimeDetails extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_details);
		Intent intent = getIntent();
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("data");
		
		
		TextView tv1 = (TextView) findViewById(R.id.timeClassNameDetails);
		TextView tv2 = (TextView) findViewById(R.id.timeClasstimeDetails);
		tv1.setText(details.get("name"));
		tv2.setText(details.get("timeForClass") + " mins");
		
		ArrayList<HashMap<String, String>> toPass = new ArrayList<HashMap<String,String>>();
		
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
		
		Adapter adapter = new Adapter(this, toPass);
		ListView lw = ((ListView) findViewById(R.id.detailsTimeClasses));
		lw.setAdapter(adapter);
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
