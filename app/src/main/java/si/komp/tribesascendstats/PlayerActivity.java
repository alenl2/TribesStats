package si.komp.tribesascendstats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import si.komp.tribesascendstats.adapters.Adapter;
import si.komp.tribesascendstats.adapters.RecentAdapter;
import si.komp.tribesascendstats.adapters.TimeAdapter;

public class PlayerActivity extends FragmentActivity {

    public static final String url = "https://account.hirezstudios.com/tribesascend/stats.aspx?player=";
    public static String user;
    static String viewState = "";
    static boolean flag = true;
    private static Context ctx;
    private static HashMap<String, ArrayList<HashMap<String, String>>> userData;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                user = data.getStringExtra("showUser");
                asyncDownloadData();
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                asyncDownloadData();
            }
        }
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

    private HashMap<String, String> setVal(String name, String value) {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("name", name);
        ret.put("value", value);
        return ret;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                asyncDownloadData();
                break;
            case R.id.action_refresh2:
                asyncDownloadData();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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

            int rootId, lwId = 0;
            ListAdapter adapter = null;
            OnItemClickListener onItemClickListener = null;

            switch (num) {
                case 1:
                    rootId = R.layout.fragment_main_summary;

                    if (userData != null) {
                        lwId = R.id.list;

                        adapter = new Adapter((Activity) ctx, userData.get("playerSum"));

                        onItemClickListener = new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String name = userData.get("playerSum").get(position).get("name");
                                String value = userData.get("playerSum").get(position).get("value");
                                Toast.makeText(ctx, name + ": " + value, Toast.LENGTH_SHORT).show();
                            }
                        };
                    }
                    break;

                case 2:
                    rootId = R.layout.fragment_main_recent;

                    if (userData != null) {
                        lwId = R.id.listPlayer;

                        adapter = new RecentAdapter((Activity) ctx, userData.get("recent"));

                        onItemClickListener = new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String details = userData.get("recent").get(position).get("matchDetails");
                                if (details.equals("NoDetails!!!")) {
                                    Toast.makeText(ctx, getResources().getString(R.string.match_removed), Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(ctx, MatchDetailsActivity.class);
                                    intent.putExtra("matchDetails", details);
                                    getActivity().startActivityForResult(intent, 1);
                                }
                            }
                        };
                    }
                    break;

                default:
                    rootId = R.layout.fragment_main_time;

                    if (userData != null) {
                        lwId = R.id.listViewTime;

                        ArrayList<HashMap<String, String>> timesSet = userData.get("times");
                        Collections.sort(timesSet, new CustomMapComparator("timeForClass"));
                        adapter = new TimeAdapter((Activity) ctx, timesSet);

                        onItemClickListener = new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent it = new Intent(ctx, TimeDetails.class);
                                it.putExtra("data", userData.get("times").get(position));
                                getActivity().startActivityForResult(it, 2);
                            }
                        };
                    }
            }

            View rootView = inflater.inflate(rootId, container, false);
            if (userData != null) {
                ListView listView = (ListView) rootView.findViewById(lwId);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(onItemClickListener);
                listView.setScrollingCacheEnabled(false);
            }
            return rootView;
        }
    }

    public class DownloadPage extends AsyncTask<String, Void, HashMap<String, ArrayList<HashMap<String, String>>>> {
        private Document doc;
        @NonNull
        private final HashMap<String, ArrayList<HashMap<String, String>>> ret = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setCancelable(false);
            this.dialog.setMessage(getResources().getString(R.string.downloading_data));
            this.dialog.show();
        }

        @Override
        protected HashMap<String, ArrayList<HashMap<String, String>>> doInBackground(String... urls) {
            for (String url : urls) {
                try {
                    doc = Jsoup.connect(url).get();

                    try {
                        Element lblErrorElement = doc.getElementById("lblError");
                        if (lblErrorElement != null && lblErrorElement.html().contains("No player"))
                            return ret;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Element viewStateElement = doc.getElementById("__VIEWSTATE");
                        viewState = viewStateElement.attr("value");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loadPlayerSummary();

                    loadRecentMatches();

                    loadClassTimes();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ret;
        }

        private void addSummaryValue(@NonNull ArrayList<HashMap<String, String>> destination, int valNameId, @NonNull String valKey) {
            try {
                Element element = doc.getElementById(valKey);
                if (element != null)
                    destination.add(setVal(getResources().getString(valNameId), element.html()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadPlayerSummary() {
            try {
                ArrayList<HashMap<String, String>> playerSummary = new ArrayList<>();

                addSummaryValue(playerSummary, R.string.key_name, "psName");
                addSummaryValue(playerSummary, R.string.key_level, "psLevel");
                addSummaryValue(playerSummary, R.string.key_last_online, "psLogin");
                addSummaryValue(playerSummary, R.string.key_user_created, "psCreated");
                addSummaryValue(playerSummary, R.string.key_matches_completed, "lblMatchesCompleted");
                addSummaryValue(playerSummary, R.string.key_kills, "lblKills");
                addSummaryValue(playerSummary, R.string.key_deaths, "lblDeaths");
                addSummaryValue(playerSummary, R.string.key_assists, "lblAssists");
                addSummaryValue(playerSummary, R.string.key_kill_death_ratio, "lblKDR");
                addSummaryValue(playerSummary, R.string.key_ski_distance, "lblSkiDistance");
                addSummaryValue(playerSummary, R.string.key_top_speed, "lblTopSpeed");
                addSummaryValue(playerSummary, R.string.key_belt_kills, "lblBeltKills");
                addSummaryValue(playerSummary, R.string.key_sprees, "lblSprees");
                addSummaryValue(playerSummary, R.string.key_multi_kills, "lblMultiKill");
                addSummaryValue(playerSummary, R.string.key_melee_kills, "lblMeleeKills");
                addSummaryValue(playerSummary, R.string.key_midairs, "lblMidairs");
                addSummaryValue(playerSummary, R.string.key_call_ins, "lblCallInsMade");
                addSummaryValue(playerSummary, R.string.key_call_in_kills, "lblCallInKills");
                addSummaryValue(playerSummary, R.string.key_full_regenerations, "lblFullRegeneration");
                addSummaryValue(playerSummary, R.string.key_headshots, "lblHeadshots");
                addSummaryValue(playerSummary, R.string.key_flag_caps, "lblFlagCaps");
                addSummaryValue(playerSummary, R.string.key_flag_returns, "lblFlagReturns");
                addSummaryValue(playerSummary, R.string.key_high_speed_grabs, "lblHighspeedGrabs");
                addSummaryValue(playerSummary, R.string.key_generators_destroyed, "lblGensDestroyed");
                addSummaryValue(playerSummary, R.string.key_base_assets_destroyed, "lblBaseAssetsDestroyed");
                addSummaryValue(playerSummary, R.string.key_base_repairs, "lblBaseRepairs");
                addSummaryValue(playerSummary, R.string.key_roadkills, "lblRoadkills");
                addSummaryValue(playerSummary, R.string.key_vehicles_destroyed, "lblVehiclesDestroyed");
                addSummaryValue(playerSummary, R.string.key_vehicle_kills, "lblVehicleKills");
                addSummaryValue(playerSummary, R.string.key_base_upgrades, "lblBaseUpgrades");

                ret.put("playerSum", playerSummary);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Adds the next match detail from {@param origin} to {@param destination}, using {@param name} as key
         *
         * @param origin      Where the detail is read
         * @param destination Where the detail is written
         * @param name        Key of the detail
         */
        private void addGameDetail(@NonNull Iterator<Element> origin, @NonNull HashMap<String, String> destination, @NonNull String name) {
            try {
                String value = origin.next().html();

                if (name.equals("gameTimeInMatch")) {
                    // I18n of the time value spent in game
                    try {
                        String minutes = value.replace(" mins", "");
                        value = String.format(getResources().getString(R.string.minutes_short_format), minutes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                destination.put(name, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadGameDetails(@NonNull Element origin, @NonNull HashMap<String, String> destination) {
            Element gameTable = origin.getElementById("historyTable");
            Iterator<Element> iterator = gameTable.select("span").iterator();
            addGameDetail(iterator, destination, "gameKills");
            addGameDetail(iterator, destination, "gameDeaths");
            addGameDetail(iterator, destination, "gameAssists");
            addGameDetail(iterator, destination, "gameKdRatio");
            addGameDetail(iterator, destination, "gameScore");
            addGameDetail(iterator, destination, "gameTimeInMatch");

        }

        private void loadPlayedGame(@NonNull Element origin, @NonNull ArrayList<HashMap<String, String>> destination) {
            HashMap<String, String> playedGame = new HashMap<>();

            try {
                playedGame.put("imageUrl", origin.getElementsByClass("mapIcon").attr("src"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Elements classesPlayed = origin.getElementById("iconContainer").getElementsByClass("classIcon");
                StringBuilder classes = new StringBuilder("");

                for (Element classPlayed : classesPlayed) {
                    try {
                        classes.append(classPlayed.attr("src"));
                        classes.append(',');
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                playedGame.put("classesPlayed", classes.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                playedGame.put("mapPlayed", origin.getElementById("lblMapName").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                playedGame.put("timePlayed", origin.getElementById("lblMapEntryDatetime").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (origin.getElementById("btnGoToMap") != null) { //lots of null pointers here because hi-rez keeps only recent match history and when they dellete it the link is gone
                    playedGame.put("matchDetails", origin.getElementById("btnGoToMap").attr("name"));
                } else {
                    playedGame.put("matchDetails", "NoDetails!!!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            loadGameDetails(origin, playedGame);

            try {
                destination.add(playedGame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadRecentMatches() {
            try {
                ArrayList<HashMap<String, String>> recent = new ArrayList<>();
                Elements games = doc.getElementById("historyTab").getElementsByClass("containerPh");
                for (Element game : games)
                    loadPlayedGame(game, recent);

                ret.put("recent", recent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadClassTimes() {
            try {
                Elements statsTab = doc.getElementById("tabs").getElementById("statsTab").getAllElements();

                ArrayList<HashMap<String, String>> times = new ArrayList<>();

                ArrayList<String> test = new ArrayList<>(); //used to check if that element was allredy in there because tribes ppl used id tag on one page 10 times >*

                for (Element tab : statsTab) {
                    if (tab.hasAttr("id") && tab.attr("id").equals("panelMap")) {
                        HashMap<String, String> toAdd = new HashMap<>();

                        try {
                            String currentClass = tab.getElementById("lblTimePlayedClass").html();

                            if (test.contains(currentClass)) {
                                continue;
                            }

                            test.add(currentClass);

                            toAdd.put("name", currentClass);

                            Integer classPlayTime = 0;

                            for (Element mapTime : tab.getElementsByClass("timeplayed")) {
                                try {
                                    String playedMap = mapTime.getElementsByClass("map").get(0).html();
                                    String timePlayed = mapTime.getElementsByClass("time").get(0).html().replace("&lt; ", "");
                                    classPlayTime += Integer.parseInt(timePlayed);

                                    toAdd.put("map-" + playedMap, timePlayed);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            toAdd.put("timeForClass", String.valueOf(classPlayTime));
                            times.add(toAdd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                ret.put("times", times);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private final ProgressDialog dialog = new ProgressDialog(ctx);

        @Override
        protected void onPostExecute(HashMap<String, ArrayList<HashMap<String, String>>> result) {
            if (result.containsKey("playerSum")) {
                try {
                    String playerTitleFormatter = getString(R.string.player_title_format);
                    String username = result.get("playerSum").get(0).get("value");
                    setTitle(String.format(playerTitleFormatter, username));
                    userData = result;
                    /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
	  fragments for each of the sections. We use a
	  {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	  will keep every loaded fragment in memory. If this becomes too memory
	  intensive, it may be best to switch to a
	  {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
                    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    /*
      The {@link ViewPager} that will host the section contents.
	 */
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
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
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
            }
            return null;
        }
    }
}