package si.komp.tribesascendstats.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import si.komp.tribesascendstats.R;
import si.komp.tribesascendstats.TribesUtils;


public class DetailsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;

    @NonNull
    private final ArrayList<HashMap<String, String>> data;

    @NonNull
    private final TribesUtils utils;

    /**
     * @param activity Parent activity
     * @param data     List of details of the players
     */
    public DetailsAdapter(@NonNull Activity activity, @NonNull ArrayList<HashMap<String, String>> data) {
        this.data = data;
        utils = new TribesUtils(activity);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    /**
     * If the given key corresponds to the searched class loads the details of the class into the given views
     *
     * @param origin         Map to load data from
     * @param key            Current key (from the map)
     * @param view           Starting view
     * @param layoutId       ID of the layout resource
     * @param playerClassId  ID of the playerClass view resource
     * @param playerDamageId ID of the playerDamage view resource
     * @param playerKillsId  ID of the playerKills view resource
     * @param playerTimeId   ID of the playerTime view resource
     * @param searchedClass  the string to search for the class
     * @param nameId         ID of the className string resource
     */
    private void tryLoadClass(@NonNull HashMap<String, String> origin, @NonNull String key, @NonNull View view, int layoutId, int playerClassId, int playerDamageId, int playerKillsId, int playerTimeId, @NonNull String searchedClass, int nameId) {
        try {
            if (key.startsWith(searchedClass)) {
                view.findViewById(layoutId).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(playerClassId)).setText(utils.getString(nameId));
                if (key.contains("ClassDemage"))
                    ((TextView) view.findViewById(playerDamageId)).setText(origin.get(key));
                if (key.contains("ClassKills"))
                    ((TextView) view.findViewById(playerKillsId)).setText(origin.get(key));
                if (key.contains("ClassTimePlayed")) {
                    String timeText = String.format(utils.getString(R.string.minutes_short_format), origin.get(key).replace(" minutes", ""));
                    ((TextView) view.findViewById(playerTimeId)).setText(timeText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param position    Index of the player in the {@link #data} list
     * @param convertView View to extend (or initialize, if null is given)
     * @param parent      Parent view
     * @return The view with the details of the given player for the current match
     */
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_match, null);

        TextView text1 = (TextView) vi.findViewById(R.id.playerName);

        HashMap<String, String> dat = data.get(position);
        text1.setText(dat.get("name"));
        for (String key : dat.keySet()) {
            tryLoadClass(dat, key, vi, R.id.layout1, R.id.playerClass1, R.id.playerDemage1, R.id.playerKills1, R.id.playerTime1, "Pathfinder", R.string.pathfinder);
            tryLoadClass(dat, key, vi, R.id.layout2, R.id.playerClass2, R.id.playerDemage2, R.id.playerKills2, R.id.playerTime2, "Soldier", R.string.soldier);
            tryLoadClass(dat, key, vi, R.id.layout3, R.id.playerClass3, R.id.playerDemage3, R.id.playerKills3, R.id.playerTime3, "Brute", R.string.brute);
            tryLoadClass(dat, key, vi, R.id.layout4, R.id.playerClass4, R.id.playerDemage4, R.id.playerKills4, R.id.playerTime4, "Sentinel", R.string.sentinel);
            tryLoadClass(dat, key, vi, R.id.layout5, R.id.playerClass5, R.id.playerDemage5, R.id.playerKills5, R.id.playerTime5, "Juggernaught", R.string.juggernaut);
            tryLoadClass(dat, key, vi, R.id.layout6, R.id.playerClass6, R.id.playerDemage6, R.id.playerKills6, R.id.playerTime6, "Technician", R.string.technician);
            tryLoadClass(dat, key, vi, R.id.layout7, R.id.playerClass7, R.id.playerDemage7, R.id.playerKills7, R.id.playerTime7, "Raider", R.string.raider);
            tryLoadClass(dat, key, vi, R.id.layout8, R.id.playerClass8, R.id.playerDemage8, R.id.playerKills8, R.id.playerTime8, "Infiltrator", R.string.raider);
            tryLoadClass(dat, key, vi, R.id.layout9, R.id.playerClass9, R.id.playerDemage9, R.id.playerKills9, R.id.playerTime9, "Doombringer", R.string.raider);
        }

        return vi;
    }
}
