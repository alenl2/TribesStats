package si.komp.tribesascendstats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import si.komp.tribesascendstats.adapters.Adapter;

public class TimeDetails extends Activity {
    @NonNull
    private final Context ctx = this;

    @NonNull
    private final TribesUtils utils = new TribesUtils(this);

    private ArrayList<HashMap<String, String>> toPass;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_details);
        Intent intent = getIntent();

        @SuppressWarnings("unchecked")
        HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("data");

        String className = details.get("name"), timeForClass = details.get("timeForClass");

        String title = String.format(getResources().getString(R.string.class_usage_format), utils.translateClassName(className));
        setTitle(title);

        TextView tv1 = (TextView) findViewById(R.id.timeClassNameDetails);
        tv1.setText(className);

        TextView tv2 = (TextView) findViewById(R.id.timeClasstimeDetails);
        String tv2Text = String.format(getResources().getString(R.string.minutes_short_format), timeForClass);
        tv2.setText(tv2Text);

        toPass = new ArrayList<>();

        for (String key : details.keySet()) {
            if (key.startsWith("map-")) {
                HashMap<String, String> ins = new HashMap<>();
                ins.put("name", key.replace("map-", ""));
                ins.put("value", details.get(key));
                toPass.add(ins);
            }
        }

        Collections.sort(toPass, new CustomMapComparator("value"));//this could be optimised to run when we are creating toPass hashmap

        Adapter adapter = new Adapter(this, toPass);

        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String toastText = toPass.get(position).get("name") + " -- " + toPass.get(position).get("value");
                Toast.makeText(ctx, toastText, Toast.LENGTH_SHORT).show();
            }
        };

        ListView lw = ((ListView) findViewById(R.id.detailsTimeClasses));
        lw.setAdapter(adapter);
        lw.setOnItemClickListener(onItemClickListener);

        Integer imageId = utils.getClassDrawableId(className);
        if (imageId != null)
            ((ImageView) findViewById(R.id.timeTimeDetailsImage)).setImageResource(imageId);

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mTracker != null) {
            mTracker.setScreenName("PlayerActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }
}