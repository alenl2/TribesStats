package si.komp.tribesascendstats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends Activity {

    private static Context ctx;
    private Tracker mTracker;

    private final OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                goToPlayer();
                return true;
            }
            return false;
        }
    };
    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.button1:
                    goToPlayer();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(onClickListener);

        EditText edittext = (EditText) findViewById(R.id.editText1);
        edittext.setOnEditorActionListener(onEditorActionListener);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String name = sharedPref.getString(getString(R.string.name), "");
        edittext.setText(name);
        edittext.setSelectAllOnFocus(true);

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTracker != null ){
            mTracker.setScreenName("MainActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void goToPlayer() {
        if (isNetworkAvailable()) {
            EditText txt = (EditText) findViewById(R.id.editText1);
            if (!txt.getText().toString().equals("")) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.name), txt.getText().toString());
                editor.commit();

                PlayerActivity.flag = true;
                Intent intent = new Intent(ctx, PlayerActivity.class);
                intent.putExtra("userName", txt.getText().toString());


                startActivity(intent);
            } else {
                Toast.makeText(ctx, "Invalid player name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ctx, "No internet access", Toast.LENGTH_SHORT).show();
        }

    }


}
