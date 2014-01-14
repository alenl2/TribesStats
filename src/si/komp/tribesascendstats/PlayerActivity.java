package si.komp.tribesascendstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.analytics.tracking.android.EasyTracker;

import si.komp.tribesascendstats.adapters.Adapter;
import si.komp.tribesascendstats.adapters.RecentAdapter;
import si.komp.tribesascendstats.adapters.TimeAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

	public static String url = "https://account.hirezstudios.com/tribesascend/stats.aspx?player=";
	public static String user;

	static View playerSum = null;
	static Context ctx;
	static String viewState = "";
	static boolean flag = true;
	
	public static HashMap<String, ArrayList<HashMap<String, String>>> userData;
	       
	int oldConfigInt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_player);
		ctx = this;

		Intent intent = getIntent();
		String userID = intent.getStringExtra("userName");
		user = userID;
		
		
		
	    if(flag){
	    	asyncDownloadData();
	    }
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    // TODO Auto-generated method stub
	    flag = false;
	    super.onConfigurationChanged(newConfig);
	}
	
	
	@Override
	public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);
	  }

	@Override
	public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);
	  }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("showUser");
				user = result;
				asyncDownloadData();
			}
		}
		
		if(requestCode == 2){
			if(resultCode == RESULT_OK){
				asyncDownloadData();
			}
		}
	}

	void asyncDownloadData() {
		if (isNetworkAvailable()) {
			DownloadPage task = new DownloadPage();
			task.execute(new String[] { url + user });
		} else {
			Toast.makeText(ctx, "No internet access", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	HashMap<String, String> setVal(String name, String value) {
		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("name", name);
		ret.put("value", value);
		return ret;
	}

	public class DownloadPage extends AsyncTask<String, Void, HashMap<String, ArrayList<HashMap<String, String>>>> {
		private ProgressDialog dialog = new ProgressDialog(ctx);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setCancelable(false);
			this.dialog.setMessage("Please wait. Downloading data.");
			this.dialog.show();
		}

		@Override
		protected HashMap<String, ArrayList<HashMap<String, String>>> doInBackground(String... urls) {

			HashMap<String, ArrayList<HashMap<String, String>>> ret = new HashMap<String, ArrayList<HashMap<String, String>>>();

			for (String url : urls) {
				try {
					Document doc = Jsoup.connect(url).get();
					try {
						if(doc.getElementById("lblError").html().contains("No player")){
							return ret;
						}
					} catch (Exception e) {
					}

					try {
						viewState = doc.getElementById("__VIEWSTATE").attr("value");
					} catch (Exception e) {
						e.printStackTrace();
					}

					// PLAYER
					ArrayList<HashMap<String, String>> playerSummary = new ArrayList<HashMap<String, String>>();
					try {
						playerSummary.add(setVal("Name", doc.getElementById("psName").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Level", doc.getElementById("psLevel").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Last Online", doc.getElementById("psLogin").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("User Created", doc.getElementById("psCreated").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Matches Completed", doc.getElementById("lblMatchesCompleted").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Kills", doc.getElementById("lblKills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Deaths", doc.getElementById("lblDeaths").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Assists", doc.getElementById("lblAssists").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("KDR", doc.getElementById("lblKDR").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Ski Distance", doc.getElementById("lblSkiDistance").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Top Speed", doc.getElementById("lblTopSpeed").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Belt Kills", doc.getElementById("lblBeltKills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Sprees", doc.getElementById("lblSprees").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Multi Kill", doc.getElementById("lblMultiKill").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Melee Kills", doc.getElementById("lblMeleeKills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Midairs", doc.getElementById("lblMidairs").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Call Ins Made", doc.getElementById("lblCallInsMade").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Call In Kills", doc.getElementById("lblCallInKills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Full Regeneration", doc.getElementById("lblFullRegeneration").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Headshots", doc.getElementById("lblHeadshots").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Flag Caps", doc.getElementById("lblFlagCaps").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Flag Returns", doc.getElementById("lblFlagReturns").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Highspeed Grabs", doc.getElementById("lblHighspeedGrabs").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Gens Destroyed", doc.getElementById("lblGensDestroyed").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Base Assets Destroyed", doc.getElementById("lblBaseAssetsDestroyed").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Base Repairs", doc.getElementById("lblBaseRepairs").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Roadkills", doc.getElementById("lblRoadkills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Vehicles Destroyed", doc.getElementById("lblVehiclesDestroyed").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Vehicle Kills", doc.getElementById("lblVehicleKills").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						playerSummary.add(setVal("Base Upgrades", doc.getElementById("lblBaseUpgrades").html()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						ret.put("playerSum", playerSummary);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// RECENT MATCHES
					try {
						ArrayList<HashMap<String, String>> recent = new ArrayList<HashMap<String, String>>();
						Elements games = doc.getElementById("historyTab").getElementsByClass("containerPh");
						for (Element game : games) {
							HashMap<String, String> playedGame = new HashMap<String, String>();

							try {
								playedGame.put("imageUrl", game.getElementsByClass("mapIcon").attr("src"));
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								Elements classesPlayed = game.getElementById("iconContainer").getElementsByClass("classIcon");
								String classes = "";
								for (Element classPlayed : classesPlayed) {
									try {
										classes += classPlayed.attr("src") + ",";
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								playedGame.put("classesPlayed", classes);
							} catch (Exception e) {
								e.printStackTrace();
							}

							try {
								playedGame.put("mapPlayed", game.getElementById("lblMapName").html());
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								playedGame.put("timePlayed", game.getElementById("lblMapEntryDatetime").html());
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								if(game.getElementById("btnGoToMap") != null){ //lots of null pointers here because hi-rez keeps only recent match history and when they dellete it the link is gone
									playedGame.put("matchDetails", game.getElementById("btnGoToMap").attr("name"));
								} else {
									playedGame.put("matchDetails", "NoDetails!!!");
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								Element gameTable = game.getElementById("historyTable");
								Iterator<Element> ite = gameTable.select("span").iterator();
								try {
									playedGame.put("gameKills", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									playedGame.put("gameDeaths", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									playedGame.put("gameAssists", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									playedGame.put("gameKdRatio", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									playedGame.put("gameScore", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									playedGame.put("gameTimeInMatch", ite.next().html());
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									recent.add(playedGame);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						try {
							ret.put("recent", recent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// TIME PLAYED
					// TODO this to tab3
					try {
						Elements statsTab = doc.getElementById("statsTab").getAllElements();
						
						ArrayList<HashMap<String, String>> times = new ArrayList<HashMap<String, String>>();
						
						ArrayList<String> test = new ArrayList<String>(); //used to check if that element was allredy in there because tribes ppl used id tag on one page 10 times >*
						
						for (Element tab : statsTab) {
							if (tab.hasAttr("id")) {
								HashMap<String, String> toAdd = new HashMap<String, String>();
								try {
									String currentClass = tab.getElementById("lblTimePlayedClass").html();
									if(test.contains(currentClass)){
										continue;
									}
									test.add(currentClass);
									toAdd.put("name", currentClass);
								} catch (Exception e) {
									e.printStackTrace();
								}
								Integer classPlayTime = 0;
								try {
									for (Element mapTime : tab.getElementsByClass("timeplayed")) {
										try {
											String playedMap = mapTime.getElementsByClass("map").get(0).html();
											String timePlayed = mapTime.getElementsByClass("time").get(0).html().replace("&lt; ", "");
											classPlayTime += Integer.parseInt(timePlayed);
											
											toAdd.put("map-"+playedMap, timePlayed);
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ret;
		}

		@Override
		protected void onPostExecute(HashMap<String, ArrayList<HashMap<String, String>>> result) {
			if(result.containsKey("playerSum")){
				try {
					setTitle("Stats for:" + result.get("playerSum").get(0).get("value") + " - Level:" + result.get("playerSum").get(1).get("value"));
					userData = result;
					mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
					mViewPager = (ViewPager) findViewById(R.id.pager);
					mViewPager.setAdapter(mSectionsPagerAdapter);
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				} catch (Exception ex) {
					Toast.makeText(ctx, "An error occurred. Check internet connection", Toast.LENGTH_SHORT).show();
					finish();
				}
			} else {
				Toast.makeText(ctx, "User not found", Toast.LENGTH_SHORT).show();
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Integer num = getArguments().getInt(ARG_SECTION_NUMBER);
			if (num == 1) {
				View rootView = inflater.inflate(R.layout.fragment_main_summary, container, false);
				if (userData != null) {
					Adapter adapter = new Adapter((Activity) ctx, userData.get("playerSum"));
					ListView lw = ((ListView) rootView.findViewById(R.id.list));
					lw.setAdapter(adapter);
					// Click event for single list row
					lw.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							String name = userData.get("playerSum").get(position).get("name");
							String value = userData.get("playerSum").get(position).get("value");
							Toast.makeText(ctx, name + ": " + value, Toast.LENGTH_SHORT).show();
						}

					});
				}
				return rootView;
			} else if (num == 2) {
				View rootView = inflater.inflate(R.layout.fragment_main_recent, container, false);
				if (userData != null) {
					RecentAdapter adapter = new RecentAdapter((Activity) ctx, userData.get("recent"));
					ListView lw = ((ListView) rootView.findViewById(R.id.listPlayer));
					lw.setScrollingCacheEnabled(false);
					lw.setAdapter(adapter);
					lw.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							if(userData.get("recent").get(position).get("matchDetails").equals("NoDetails!!!") == false){
								Intent intent = new Intent(ctx, MatchDetailsActivity.class);
								intent.putExtra("matchDetails", userData.get("recent").get(position).get("matchDetails"));
								getActivity().startActivityForResult(intent, 1);	
							} else {
								Toast.makeText(ctx, "Match was allredy removed", Toast.LENGTH_SHORT).show();
							}

						}

					});
				}
				return rootView;
			} else{
				View rootView = inflater.inflate(R.layout.fragment_main_time, container, false);
				if(userData != null){
					ArrayList<HashMap<String, String>> unsorted = userData.get("times");
					Collections.sort(unsorted, new CustomComparator2());
					
					
					TimeAdapter adapter = new TimeAdapter((Activity) ctx, unsorted);
					ListView lw = ((ListView) rootView.findViewById(R.id.listViewTime));
					lw.setScrollingCacheEnabled(false);
					lw.setAdapter(adapter);
					lw.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent it = new Intent(ctx, TimeDetails.class);
							it.putExtra("data", userData.get("times").get(position));
							getActivity().startActivityForResult(it, 2);
						}
					});
				}
				return rootView;
			}
		}
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
}


class CustomComparator2 implements Comparator<HashMap<String, String>> {
    @Override
    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
    	try {
            if(Integer.parseInt(o1.get("timeForClass")) >= Integer.parseInt(o2.get("timeForClass"))){
            	return -1;
            }
            return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

    }
}
