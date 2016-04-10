package si.komp.tribesascendstats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Set;

public class MainActivity extends Activity {
    private Tracker mTracker;
    private AutoCompleteTextView inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tw = (TextView)findViewById(R.id.textView);
        tw.setClickable(true);
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wilderzone.org/"));
                startActivity(browserIntent);
            }
        });
        
        inputText = (AutoCompleteTextView) findViewById(R.id.inputText);
        inputText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    goToPlayer(null);
                    return true;
                }
                return false;
            }
        });
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // When th view will be focused, the listener will show the dropdown menu with the suggestions
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && view.getId() == R.id.inputText) {
                    // Wait until the text field is initialized and then show the dropdown menu
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
        });

        mTracker = ((TribesStats) getApplication()).getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputText.setAdapter(null);

        if (mTracker != null) {
            mTracker.setScreenName("MainActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

        try {
            loadHistory();
            inputText.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Will be called by homepage 'Submit' button onClick
    public void goToPlayer(View view) {
        if (isNetworkAvailable()) {
            String username = inputText.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.invalid_player_name), Toast.LENGTH_SHORT).show();
            } else {
                PlayerActivity.flag = true;
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("userName", username);

                startActivity(intent);
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads the search history into inputText
     */
    private void loadHistory() {
        Set<String> historySet = new HistoryManager(this).getHistory();
        if (!historySet.isEmpty()) {
            // Set<String> -> String[] -> ArrayAdapter<String> <- setAdapter
            String[] historyArray = historySet.toArray(new String[1]);
            inputText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, historyArray));
        }
    }
}
