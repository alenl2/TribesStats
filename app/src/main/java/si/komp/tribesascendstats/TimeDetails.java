package si.komp.tribesascendstats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

    private ArrayList<HashMap<String, String>> toPass;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_details);
        Intent intent = getIntent();

        @SuppressWarnings("unchecked")
        HashMap<String, String> details = (HashMap<String, String>) intent.getSerializableExtra("data");

        if (details == null) {
            Log.e("TimeDetails.onCreate", "Details is NULL, aborting initialization");
        } else {
            String className = details.get("name"), translatedClassName, timeForClass = details.get("timeForClass");

            try {
                translatedClassName = getString(TribesUtils.CLASS_STRINGS.get(className.toLowerCase()));
            } catch (Exception e) {
                translatedClassName = className;
            }

            setTitle(String.format(getString(R.string.class_usage_format), translatedClassName));

            ((TextView) findViewById(R.id.timeClassNameDetails)).setText(translatedClassName);

            ((TextView) findViewById(R.id.timeClasstimeDetails)).setText(String.format(getString(R.string.minutes_short_format), timeForClass));

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

            ListView lw = ((ListView) findViewById(R.id.detailsTimeClasses));
            lw.setAdapter(adapter);
            lw.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        String toastText = String.format(getString(R.string.time_toast_format), toPass.get(position).get("name"), toPass.get(position).get("value"));
                        Toast.makeText(ctx, toastText, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            ((ImageView) findViewById(R.id.timeTimeDetailsImage)).setImageResource(TribesUtils.CLASS_DRAWABLES.get(className.toLowerCase()));
        }

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Will be called by the 'Reset search history' menu entry onClick
    public void resetHistory(MenuItem item) {
        new HistoryManager(this).resetHistory();
    }

    // Will be called by the 'Refresh page' menu entry onClick
    public void refreshPage(MenuItem item) {
        setResult(Activity.RESULT_OK, new Intent());
        finish();
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