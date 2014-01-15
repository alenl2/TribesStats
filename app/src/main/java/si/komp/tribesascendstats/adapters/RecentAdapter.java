package si.komp.tribesascendstats.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import si.komp.tribesascendstats.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentAdapter extends BaseAdapter {
	private final ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public RecentAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		data = d;
		inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null){
			vi = inflater.inflate(R.layout.list_row_recent, null);
		}

		TextView text1 = (TextView) vi.findViewById(R.id.mapText);
		TextView text2 = (TextView) vi.findViewById(R.id.mapTimeText);
		TextView text3 = (TextView) vi.findViewById(R.id.kdText);
		TextView text4 = (TextView) vi.findViewById(R.id.deathsText);
		TextView text5 = (TextView) vi.findViewById(R.id.scoreText);
		TextView text6 = (TextView) vi.findViewById(R.id.assistsText);
		TextView text7 = (TextView) vi.findViewById(R.id.timeText);
		TextView text8 = (TextView) vi.findViewById(R.id.killsText);

		HashMap<String, String> dat = data.get(position);
		text1.setText(dat.get("mapPlayed"));
		
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa",Locale.getDefault());
		try {
			Date playTime = format1.parse(dat.get("timePlayed"));
			text2.setText(playTime.toString());
		} catch (ParseException e1) {
			text2.setText(dat.get("timePlayed"));
		}
		
		
		text3.setText(dat.get("gameKdRatio"));
		text4.setText(dat.get("gameDeaths"));
		text5.setText(dat.get("gameScore"));
		text6.setText(dat.get("gameAssists"));
		text7.setText(dat.get("gameTimeInMatch"));
		text8.setText(dat.get("gameKills"));

		ImageView viewb = (ImageView) vi.findViewById(R.id.mapImage);
		Locale l = Locale.getDefault();
		String imagePath = dat.get("imageUrl").toLowerCase(l);

		if (imagePath.contains("airarena")) {
			viewb.setImageResource(R.drawable.airarena);
		} else if (imagePath.contains("arxnovena")) {
			viewb.setImageResource(R.drawable.arxnovena);
		} else if (imagePath.contains("bellaomega")) {
			viewb.setImageResource(R.drawable.bellaomega);
		} else if (imagePath.contains("blueshift")) {
			viewb.setImageResource(R.drawable.blueshift);
		} else if (imagePath.contains("canyoncrusaderevival")) {
			viewb.setImageResource(R.drawable.canyoncrusaderevival);
		} else if (imagePath.contains("crossfire")) {
			viewb.setImageResource(R.drawable.crossfire);
		} else if (imagePath.contains("dangerouscrossing")) {
			viewb.setImageResource(R.drawable.dangerouscrossing);
		} else if (imagePath.contains("drydock")) {
			viewb.setImageResource(R.drawable.drydock);
		} else if (imagePath.contains("drydocknight")) {
			viewb.setImageResource(R.drawable.drydocknight);
		} else if (imagePath.contains("fraytown")) {
			viewb.setImageResource(R.drawable.fraytown);
		} else if (imagePath.contains("hinterlands")) {
			viewb.setImageResource(R.drawable.hinterlands);
		} else if (imagePath.contains("inferno")) {
			viewb.setImageResource(R.drawable.inferno);
		} else if (imagePath.contains("katabatic")) {
			viewb.setImageResource(R.drawable.katabatic);
		} else if (imagePath.contains("lavaarena")) {
			viewb.setImageResource(R.drawable.lavaarena);
		} else if (imagePath.contains("miasma")) {
			viewb.setImageResource(R.drawable.miasma);
		} else if (imagePath.contains("nightabatic")) {
			viewb.setImageResource(R.drawable.nightabatic);
		} else if (imagePath.contains("outskirts")) {
			viewb.setImageResource(R.drawable.outskirts);
		} else if (imagePath.contains("permafrost")) {
			viewb.setImageResource(R.drawable.permafrost);
		} else if (imagePath.contains("	uicksand")) {
			viewb.setImageResource(R.drawable.quicksand);
		} else if (imagePath.contains("raindance")) {
			viewb.setImageResource(R.drawable.raindance);
		} else if (imagePath.contains("stonehenge")) {
			viewb.setImageResource(R.drawable.stonehenge);
		} else if (imagePath.contains("sulfurcove")) {
			viewb.setImageResource(R.drawable.sulfurcove);
		} else if (imagePath.contains("sunstar")) {
			viewb.setImageResource(R.drawable.sunstar);
		} else if (imagePath.contains("tartarus")) {
			viewb.setImageResource(R.drawable.tartarus);
		} else if (imagePath.contains("templeruins")) {
			viewb.setImageResource(R.drawable.templeruins);
		} else if (imagePath.contains("undercroft")) {
			viewb.setImageResource(R.drawable.undercroft);
		} else if (imagePath.contains("walledin")) {
			viewb.setImageResource(R.drawable.walledin);
		} else if (imagePath.contains("whiteout")) {
			viewb.setImageResource(R.drawable.whiteout);
		}

		String classes = dat.get("classesPlayed");
		int[] imgs = new int[] { R.id.Image1, R.id.Image2, R.id.Image3,
				R.id.Image4, R.id.Image5, R.id.Image6, R.id.Image7,
				R.id.Image8, R.id.image9 };

        for (int img : imgs) {
            ImageView view = (ImageView) vi.findViewById(img);
            view.setImageDrawable(null);
        }
		int curImg = 0;
		for (String classPlayed : classes.split(",")) {
			ImageView view = (ImageView) vi.findViewById(imgs[curImg]);
			if (classPlayed.contains("Pathfinder")) {
				view.setImageResource(R.drawable.pathfinder);
			} else if (classPlayed.contains("Sentinel")) {
				view.setImageResource(R.drawable.sentinel);
			} else if (classPlayed.contains("Infiltrator")) {
				view.setImageResource(R.drawable.infiltrator);
			} else if (classPlayed.contains("Soldier")) {
				view.setImageResource(R.drawable.soldier);
			} else if (classPlayed.contains("Raider")) {
				view.setImageResource(R.drawable.raider);
			} else if (classPlayed.contains("Technician")) {
				view.setImageResource(R.drawable.technician);
			} else if (classPlayed.contains("Juggernaught")) {
				view.setImageResource(R.drawable.juggernaught);
			} else if (classPlayed.contains("Doombringer")) {
				view.setImageResource(R.drawable.doombringer);
			} else if (classPlayed.contains("Brute")) {
				view.setImageResource(R.drawable.brute);
			}else{
				view.setImageDrawable(null);
			}
			curImg++;
		}
		return vi;
	}
}
