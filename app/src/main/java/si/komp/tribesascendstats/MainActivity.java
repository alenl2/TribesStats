package si.komp.tribesascendstats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Set;

public class MainActivity extends Activity {
    private Tracker mTracker;
    private HistoryManager historyManager;
    private AutoCompleteTextView inputText;

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
            if (v.getId() == R.id.button1)
                goToPlayer();
        }
    };

    private final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus && view.getId() == R.id.inputText) {
                //Wait until the text field is initialized and then show the dropdown menu
                inputText.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                inputText.showDropDown();
                            }
                        }
                );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyManager = new HistoryManager(this);

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(onClickListener);

        inputText = (AutoCompleteTextView) findViewById(R.id.inputText);
        inputText.setOnEditorActionListener(onEditorActionListener);
        inputText.setOnFocusChangeListener(onFocusChangeListener);
        inputText.setSelectAllOnFocus(true);

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTracker != null) {
            mTracker.setScreenName("MainActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

        loadHistory();
        inputText.requestFocus();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void goToPlayer() {
        if (isNetworkAvailable()) {
            String username = inputText.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(this, "Invalid player name", Toast.LENGTH_SHORT).show();
            } else {
                historyManager.addUser(username);

                PlayerActivity.flag = true;
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("userName", username);

                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "No internet access", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads the search history into inputText
     */
    private void loadHistory() {
        Set<String> historySet = historyManager.getHistory();
        if (!historySet.isEmpty()) {
            // Set<String> -> String[] -> ArrayAdapter<String> <- setAdapter
            String[] historyArray = historySet.toArray(new String[1]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, historyArray);
            inputText.setAdapter(adapter);
        }
    }
}
