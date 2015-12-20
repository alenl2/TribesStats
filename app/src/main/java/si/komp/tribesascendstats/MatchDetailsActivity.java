package si.komp.tribesascendstats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import si.komp.tribesascendstats.adapters.DetailsAdapter;

public class MatchDetailsActivity extends Activity {
    private String detailsLink;
    @NonNull
    private final Context ctx = this;
    @NonNull
    private String matchId = "";
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_match);

        Intent intent = getIntent();
        detailsLink = intent.getStringExtra("matchDetails");

        if (isNetworkAvailable()) {
            asyncDownloadData();
        } else {
            Toast.makeText(ctx, getResources().getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
            finish();
        }

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTracker != null) {
            mTracker.setScreenName("MatchDetailsActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    private void asyncDownloadData() {
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(PlayerActivity.url + PlayerActivity.user);
    }

    // Will be called by the 'Reset search history' menu entry onClick
    public void resetHistory(MenuItem item) {
        new HistoryManager(this).resetHistory();
    }

    // Will be called by the 'Refresh page' menu entry onClick
    public void refreshPage(MenuItem item) {
        asyncDownloadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setCancelable(false);
            this.dialog.setMessage(getResources().getString(R.string.downloading_data));
            this.dialog.show();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... urls) {
            ArrayList<HashMap<String, String>> ret = new ArrayList<>();
            for (String url : urls) {
                try {
                    Document document = Jsoup.connect(url).data("__VIEWSTATE", PlayerActivity.viewState).data(detailsLink, "Match Details").method(Method.POST).post();

                    try {
                        Element element = document.getElementById("lblError");
                        if (element != null && element.html().contains("No matches")) {
                            return ret;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        matchId = document.title().replace("Tribes Match Details: ", "").trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Element details = document.getElementById("detailsContent");
                    Elements players = details.getElementsByClass("detailsItemTemplateTable");
                    for (Element player : players) {
                        HashMap<String, String> playerInfo = new HashMap<>();

                        playerInfo.put("name", player.getElementsByClass("name").html());
                        Iterator<Element> ite = player.select("span").iterator();
                        while (ite.hasNext()) {
                            String uClass = ite.next().html();
                            playerInfo.put(uClass + "ClassDemage", ite.next().html());
                            playerInfo.put(uClass + "ClassKills", ite.next().html());
                            playerInfo.put(uClass + "ClassTimePlayed", ite.next().html());
                        }
                        ret.add(playerInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ret;
        }

        private final ProgressDialog dialog = new ProgressDialog(ctx);

        @Override
        protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
            if (result.size() == 0) {
                Toast.makeText(ctx, getResources().getString(R.string.no_matches), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                try {
                    String titleFormat = getResources().getString(R.string.match_details_format);
                    setTitle(String.format(titleFormat, matchId));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    DetailsAdapter adapter = new DetailsAdapter((Activity) ctx, result);

                    OnItemClickListener onItemClickListener = new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String userName = result.get(position).get("name");
                            String userText = String.format(getResources().getString(R.string.user_format), userName);

                            Toast.makeText(ctx, userText, Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("showUser", userName);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    };

                    ListView lw = ((ListView) findViewById(R.id.listRecent));
                    lw.setAdapter(adapter);
                    lw.setOnItemClickListener(onItemClickListener);

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
