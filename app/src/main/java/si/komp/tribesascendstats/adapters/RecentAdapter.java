package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import si.komp.tribesascendstats.R;
import si.komp.tribesascendstats.TribesUtils;

public class RecentAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    @NonNull
    private final ArrayList<HashMap<String, String>> data;
    @NonNull
    private final TribesUtils utils;

    public RecentAdapter(@NonNull Activity a, @NonNull ArrayList<HashMap<String, String>> d) {
        data = d;
        utils = new TribesUtils(a);
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
        if (convertView == null) {
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

        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.getDefault());
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
        String mapPlayed = dat.get("imageUrl").replace("images/icons/maps/", "").replace(".jpg", "");
        Integer mapDrawableId = utils.getMapDrawableId(mapPlayed);
        if (mapDrawableId != null)
            viewb.setImageResource(mapDrawableId);

        String classes = dat.get("classesPlayed");
        int[] imgs = new int[]{R.id.Image1, R.id.Image2, R.id.Image3,
                R.id.Image4, R.id.Image5, R.id.Image6, R.id.Image7,
                R.id.Image8, R.id.image9};

        for (int img : imgs) {
            ImageView view = (ImageView) vi.findViewById(img);
            view.setImageDrawable(null);
        }
        int curImg = 0;
        for (String classPlayed : classes.split(",")) {
            Integer classDrawableId = utils.getClassDrawableId(classPlayed);
            if (classDrawableId != null)
                ((ImageView) vi.findViewById(imgs[curImg])).setImageResource(classDrawableId);
            curImg++;
        }
        return vi;
    }
}
