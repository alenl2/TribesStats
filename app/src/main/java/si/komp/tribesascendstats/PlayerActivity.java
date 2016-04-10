package si.komp.tribesascendstats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import si.komp.tribesascendstats.adapters.StatsAdapter;
import si.komp.tribesascendstats.adapters.TimeAdapter;

public class PlayerActivity extends FragmentActivity {

    public enum PageName{
        Player,
        BaseStats,
        MatchAverages,
        KillStats,
        ClassTimes,
        GameModeStats
    }

    private static final String url = "http://www.wilderzone.org/player/";
    private static String user;
    static boolean flag = true;
    private static Context ctx;
    private static HashMap<PageName, List<StatItem>> userData;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);
        ctx = this;

        Intent intent = getIntent();
        user = intent.getStringExtra("userName");

        if (flag) {
            asyncDownloadData();
        }

        TribesStats application = (TribesStats) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTracker != null) {
            mTracker.setScreenName("PlayerActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        flag = false;
        super.onConfigurationChanged(newConfig);
    }

    private void asyncDownloadData() {
        if (isNetworkAvailable()) {
            DownloadPage task = new DownloadPage();
            task.execute(url + user);
        } else {
            Toast.makeText(ctx, getResources().getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public static class DummySectionFragment extends Fragment {
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Integer num = getArguments().getInt(ARG_SECTION_NUMBER);
            ListAdapter adapter = null;
            switch (num) {
                case 0:
                    adapter = new StatsAdapter((Activity)ctx, userData.get(PageName.BaseStats));
                    break;
                case 1:
                    adapter = new StatsAdapter((Activity)ctx, userData.get(PageName.MatchAverages));
                    break;
                case 2:
                    adapter = new StatsAdapter((Activity)ctx, userData.get(PageName.KillStats));
                    break;
                case 3:
                    adapter = new StatsAdapter((Activity)ctx, userData.get(PageName.GameModeStats));
                    break;
                case 4:
                    adapter = new TimeAdapter((Activity)ctx, userData.get(PageName.ClassTimes));
                    break;
            }
            View rootView = inflater.inflate(R.layout.fragment_main_summary, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.list);
            listView.setAdapter(adapter);

            return rootView;
        }
    }

    private StatItem getStatItem(int resourceName, String value, String extra){
        return new StatItem(getResources().getString(resourceName), value, extra);
    }

    public class DownloadPage extends AsyncTask<String, Void, HashMap<PageName, List<StatItem>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setCancelable(false);
            this.dialog.setMessage(getResources().getString(R.string.downloading_data));
            this.dialog.show();
        }

        @Override
        protected HashMap<PageName, List<StatItem>> doInBackground(String... urls) {
            HashMap<PageName, List<StatItem>> ret = new HashMap<>();
            List<StatItem> player = new ArrayList<>();
            List<StatItem> timeStats = new ArrayList<>();
            List<StatItem> killStats = new ArrayList<>();
            List<StatItem> matchStats = new ArrayList<>();
            List<StatItem> baseStats  = new ArrayList<>();
            List<StatItem> gameModeStats  = new ArrayList<>();

            for (String url : urls) {
                try {
                    Document doc = Jsoup.connect(url).get();
                    player.add(getStatItem(R.string.key_name, doc.getElementsByClass("pagetitle").text(), ""));
                    player.add(getStatItem(R.string.key_playerRank, doc.getElementsByClass("pageTitle").first().nextElementSibling().text(), ""));


                    Elements statLines = doc.getElementsByClass("statstable").get(0).getElementsByAttribute("data-key");
                    for(Element e: statLines){
                        try{
                            String value = e.getElementsByClass("val").text();
                            String name = e.select("td").first().html();
                            killStats.add(getStatItem(TribesUtils.STAT_STRINGS.get(name), value, ""));
                        }catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }

                    statLines = doc.getElementsByClass("statstable").get(1).getElementsByAttribute("data-key");
                    for(Element e: statLines){
                        try{
                            String value = e.getElementsByClass("val").text();
                            String name = e.select("td").first().html();
                            matchStats.add(getStatItem(TribesUtils.STAT_STRINGS.get(name), value, ""));
                        }catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }

                    statLines = doc.getElementsByClass("statstable").get(2).getElementsByAttribute("data-key");
                    for(Element e: statLines){
                        try{
                            String value = e.getElementsByClass("val").text();
                            String name = e.select("td").first().html();
                            baseStats.add(getStatItem(TribesUtils.STAT_STRINGS.get(name), value, ""));
                        }catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }
                    for(Element e: doc.getElementsByClass("classlegendtable").get(0).getElementsByAttribute("data-classname")){
                        try{
                            String name = e.attr("data-classname");
                            String value = e.select("td").get(2).text();
                            timeStats.add(getStatItem(TribesUtils.CLASS_STRINGS.get(name), value, name));
                        }catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }

                    for(Element e: doc.getElementsByClass("classlegendtable").get(1).getElementsByAttribute("data-gametype")){
                        try{
                            String name = e.attr("data-gametype");
                            String value = e.select("td").get(2).text();
                            gameModeStats.add(getStatItem(TribesUtils.GAMEMODE_STRINGS.get(name), value, ""));
                        }catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    return ret;
                }
            }

            ret.put(PageName.Player, player);
            ret.put(PageName.KillStats, killStats);
            ret.put(PageName.MatchAverages, matchStats);
            ret.put(PageName.BaseStats, baseStats);
            ret.put(PageName.ClassTimes, timeStats);
            ret.put(PageName.GameModeStats, gameModeStats);
            return ret;
        }

        private final ProgressDialog dialog = new ProgressDialog(ctx);

        @Override
        protected void onPostExecute(HashMap<PageName, List<StatItem>> result) {
            if (result.containsKey(PageName.Player)) {
                try {
                    String playerTitleFormatter = getString(R.string.player_title_format);
                    String username = result.get(PageName.Player).get(0).getValue();
                    try {
                        new HistoryManager(ctx).addUser(username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setTitle(String.format(playerTitleFormatter, username));
                    userData = result;
                    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception ex) {
                    Toast.makeText(ctx, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(ctx, getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section5).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }
}