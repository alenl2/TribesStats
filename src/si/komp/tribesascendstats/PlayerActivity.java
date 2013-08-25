package si.komp.tribesascendstats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import si.komp.tribesascendstats.adapters.Adapter;
import si.komp.tribesascendstats.adapters.RecentAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PlayerActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	public static String url = "http://account.hirezstudios.com/tribesascend/stats.aspx?player=";
	public static String user;

	static View playerSum = null;
	static Context ctx;
	static String viewState ="";
	public static HashMap<String,ArrayList<HashMap<String, String>>> userData; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_player);
		ctx = this;
		
		Intent intent = getIntent();
		String userID = intent.getStringExtra("userName");
		user = userID;
		asyncDownloadData();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		         String result=data.getStringExtra("showUser");  
		         user = result;
		         asyncDownloadData();
		     }
		  }
	}
	
	void asyncDownloadData(){
		if(isNetworkAvailable()){
			DownloadPage task = new DownloadPage();
			task.execute(new String[] { url+user});
		} else {
			Toast.makeText(ctx, "No internet access", Toast.LENGTH_SHORT).show();
			finish();
		}

	}
	
	HashMap<String, String> setVal(String name, String value){
		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("name", name);
		ret.put("value", value);
		return ret;
	}
	
	public class DownloadPage extends AsyncTask<String, Void, HashMap<String,ArrayList<HashMap<String, String>>>> {
		 private ProgressDialog dialog = new ProgressDialog(ctx);
		 
		 @Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setCancelable(false);
	        this.dialog.setMessage("Please wait");
	        this.dialog.show();
		}
		
		    
		@Override
		protected HashMap<String,ArrayList<HashMap<String, String>>>  doInBackground(String... urls) {
			
			HashMap<String,ArrayList<HashMap<String, String>>> ret = new HashMap<String,ArrayList<HashMap<String, String>>> ();
			
			for (String url : urls) {
				try {
					Document doc = Jsoup.connect(url).get();
					viewState = doc.getElementById("__VIEWSTATE").attr("value");
					
					//PLAYER
					ArrayList<HashMap<String, String>> playerSummary = new ArrayList<HashMap<String, String>>();
					playerSummary.add(setVal("Name", doc.getElementById("psName").html()));
					playerSummary.add(setVal("Level", doc.getElementById("psLevel").html()));
					playerSummary.add(setVal("Last Online", doc.getElementById("psLogin").html()));
					playerSummary.add(setVal("User Created", doc.getElementById("psCreated").html()));
					playerSummary.add(setVal("Matches Completed",doc.getElementById("lblMatchesCompleted").html()));
					playerSummary.add(setVal("Kills",doc.getElementById("lblKills").html()));
					playerSummary.add(setVal("Deaths",doc.getElementById("lblDeaths").html()));
					playerSummary.add(setVal("Assists",doc.getElementById("lblAssists").html()));
					playerSummary.add(setVal("KDR",doc.getElementById("lblKDR").html()));
					playerSummary.add(setVal("Ski Distance",doc.getElementById("lblSkiDistance").html()));
					playerSummary.add(setVal("Top Speed",doc.getElementById("lblTopSpeed").html()));
					playerSummary.add(setVal("Belt Kills",doc.getElementById("lblBeltKills").html()));
					playerSummary.add(setVal("Sprees",doc.getElementById("lblSprees").html()));
					playerSummary.add(setVal("Multi Kill",doc.getElementById("lblMultiKill").html()));
					playerSummary.add(setVal("Melee Kills",doc.getElementById("lblMeleeKills").html()));
					playerSummary.add(setVal("Midairs",doc.getElementById("lblMidairs").html()));
					playerSummary.add(setVal("Call Ins Made",doc.getElementById("lblCallInsMade").html()));
					playerSummary.add(setVal("Call In Kills",doc.getElementById("lblCallInKills").html()));
					playerSummary.add(setVal("Full Regeneration",doc.getElementById("lblFullRegeneration").html()));
					playerSummary.add(setVal("Headshots",doc.getElementById("lblHeadshots").html()));
					playerSummary.add(setVal("Flag Caps",doc.getElementById("lblFlagCaps").html()));
					playerSummary.add(setVal("Flag Returns",doc.getElementById("lblFlagReturns").html()));
					playerSummary.add(setVal("Highspeed Grabs",doc.getElementById("lblHighspeedGrabs").html()));
					playerSummary.add(setVal("Gens Destroyed",doc.getElementById("lblGensDestroyed").html()));
					playerSummary.add(setVal("Base Assets Destroyed",doc.getElementById("lblBaseAssetsDestroyed").html()));
					playerSummary.add(setVal("Base Repairs",doc.getElementById("lblBaseRepairs").html()));
					playerSummary.add(setVal("Roadkills",doc.getElementById("lblRoadkills").html()));
					playerSummary.add(setVal("Vehicles Destroyed",doc.getElementById("lblVehiclesDestroyed").html()));
					playerSummary.add(setVal("Vehicle Kills",doc.getElementById("lblVehicleKills").html()));
					playerSummary.add(setVal("Base Upgrades",doc.getElementById("lblBaseUpgrades").html()));
					ret.put("playerSum", playerSummary);
					
					//RECENT MATCHES
					ArrayList<HashMap<String, String>> recent = new ArrayList<HashMap<String, String>>();
					Element recentMatches = doc.getElementById("historyTab");
					Elements games = recentMatches.getElementsByClass("containerPh");
					for (Element game : games) {
						HashMap<String, String> playedGame = new HashMap<String, String>();
						
						playedGame.put("imageUrl", game.getElementsByClass("mapIcon").attr("src"));
						Elements classesPlayed = game.getElementById("iconContainer").getElementsByClass("classIcon");
						String classes ="";
						for(Element classPlayed: classesPlayed){
							classes+= classPlayed.attr("src")+",";
						}
						playedGame.put("classesPlayed",classes);
						playedGame.put("mapPlayed", game.getElementById("lblMapName").html());
						playedGame.put("timePlayed", game.getElementById("lblMapEntryDatetime").html());
						playedGame.put("matchDetails", game.getElementById("btnGoToMap").attr("name"));
						

						Element gameTable = game.getElementById("historyTable");
						Iterator<Element> ite = gameTable.select("span").iterator();
						playedGame.put("gameKills", ite.next().html());
						playedGame.put("gameDeaths", ite.next().html());
						playedGame.put("gameAssists", ite.next().html());
						playedGame.put("gameKdRatio", ite.next().html());
						playedGame.put("gameScore", ite.next().html());
						playedGame.put("gameTimeInMatch", ite.next().html());
						recent.add(playedGame);
					}
					ret.put("recent", recent);
					
					//TIME PLAYED
					//TODO this to tab3
					Elements statsTab = doc.getElementById("statsTab").getAllElements();
					for(Element tab: statsTab){
						if(tab.hasAttr("id")){
							String currentClass = tab.getElementById("lblTimePlayedClass").html();
							for(Element mapTime: tab.getElementsByClass("timeplayed")){
								String playedMap = mapTime.getElementsByClass("map").get(0).html();
								String timePlayed = mapTime.getElementsByClass("time").get(0).html();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ret;
		}

		@Override
		protected void onPostExecute(HashMap<String,ArrayList<HashMap<String, String>>>  result) {
			try{
				setTitle("Stats for:"+result.get("playerSum").get(0).get("value") + " - Level:"+result.get("playerSum").get(1).get("value"));
				userData = result;
				mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
				mViewPager = (ViewPager) findViewById(R.id.pager);
				mViewPager.setAdapter(mSectionsPagerAdapter);
		        if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
			} catch(Exception ex){
				Toast.makeText(ctx, "An error occurred. Check internet connection", Toast.LENGTH_SHORT).show();
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

	
	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			Integer num = getArguments().getInt(ARG_SECTION_NUMBER);
			if(num == 1){
				View rootView = inflater.inflate(R.layout.fragment_main_summary,container, false);
				if(userData != null){
					Adapter adapter=new Adapter((Activity) ctx, userData.get("playerSum"));
					ListView lw = ((ListView) rootView.findViewById(R.id.list));
					lw.setAdapter(adapter);
			        // Click event for single list row
			        lw.setOnItemClickListener(new OnItemClickListener() {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			                String name = userData.get("playerSum").get(position).get("name");
			                String value = userData.get("playerSum").get(position).get("value");
			                Toast.makeText(ctx, name+": "+value, Toast.LENGTH_SHORT).show();
			            }

			        });
				}
				return rootView;
			} else if(num == 2){
				View rootView = inflater.inflate(R.layout.fragment_main_recent,container, false);
				if(userData != null){
					RecentAdapter adapter=new RecentAdapter((Activity) ctx, userData.get("recent"));
					ListView lw = ((ListView) rootView.findViewById(R.id.listPlayer));
					lw.setScrollingCacheEnabled(false);
					lw.setAdapter(adapter);
			        lw.setOnItemClickListener(new OnItemClickListener() {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			            	Intent intent = new Intent(ctx, MatchDetailsActivity.class);
			            	intent.putExtra("matchDetails", userData.get("recent").get(position).get("matchDetails"));
			            	getActivity().startActivityForResult(intent, 1);
			            }

			        });
				}
				return rootView;
			}
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,container, false);
			return rootView;
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		asyncDownloadData();
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
