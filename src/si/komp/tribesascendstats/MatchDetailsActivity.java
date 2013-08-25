package si.komp.tribesascendstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import si.komp.tribesascendstats.adapters.DetailsAdapter;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDetailsActivity extends Activity {
	
	String detailsLink;
	Context ctx;
	String appTitle ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		setContentView(R.layout.activity_main_match);

			Intent intent = getIntent();
			detailsLink = intent.getStringExtra("matchDetails");

			if(isNetworkAvailable()){
				DownloadWebPageTask task = new DownloadWebPageTask();
				task.execute(new String[] { PlayerActivity.url + PlayerActivity.user });
			} else {
				Toast.makeText(ctx, "No internet access", Toast.LENGTH_SHORT).show();
				finish();
			}	
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
		private ProgressDialog dialog = new ProgressDialog(ctx);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setCancelable(false);
	        this.dialog.setMessage("Please wait");
	        this.dialog.show();
		}
	    
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... urls) {
			ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
			for (String url : urls) {
				try {
					Document document = Jsoup.connect(url).data("__VIEWSTATE", PlayerActivity.viewState).data(detailsLink, "Match Details").method(Method.POST).post();

					appTitle = document.title();
					Element details = document.getElementById("detailsContent");
					Elements players = details.getElementsByClass("detailsItemTemplateTable");
					for(Element player: players){
						HashMap<String, String> playerInfo = new HashMap<String, String>();
						
						playerInfo.put("name",player.getElementsByClass("name").html());
						Iterator<Element> ite = player.select("span").iterator();
						while(ite.hasNext()){
							String uClass = ite.next().html();
							playerInfo.put(uClass+"ClassDemage", ite.next().html());
							playerInfo.put(uClass+"ClassKills", ite.next().html());
							playerInfo.put(uClass+"ClassTimePlayed", ite.next().html());
						}
						ret.add(playerInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ret;
		}
		@Override
		protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
			try {
				setTitle(appTitle);
				DetailsAdapter adapter=new DetailsAdapter((Activity) ctx, result);
				ListView lw = ((ListView) findViewById(R.id.listRecent));
				lw.setAdapter(adapter);
		        // Click event for single list row
		        lw.setOnItemClickListener(new OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		            	Toast.makeText(ctx, "User "+result.get(position).get("name"), Toast.LENGTH_SHORT).show();
		            	Intent returnIntent = new Intent();
		            	 returnIntent.putExtra("showUser",result.get(position).get("name"));
		            	 setResult(RESULT_OK,returnIntent);     
		            	 finish();
		            }

		        });
		        if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
			} catch (Exception e) {
				Toast.makeText(ctx, "An error occurred. Check internet connection", Toast.LENGTH_SHORT).show();
				finish();
			}
			
		}
	}
}
