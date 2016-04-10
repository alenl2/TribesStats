package si.komp.tribesascendstats;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TribesUtils {
    /**
     * Maps each Tribes Ascend class to it's corresponding Android string resource ID (Like R.string.xyz)
     * All class names are lower case, with no spacing
     */
    @NonNull
    public static final Map<String, Integer> CLASS_STRINGS = new HashMap<>(9);

    /**
     * Maps each Tribes Ascend class to it's corresponding Android drawable resource ID (Like R.drawable.xyz)
     * All class names are lower case, with no spacing
     */
    @NonNull
    public static final Map<String, Integer> CLASS_DRAWABLES = new HashMap<>(9);

    /**
     * Maps Tribes strings from the website to resource strings
     */
    @NonNull
    public static final Map<String, Integer> STAT_STRINGS = new HashMap<>();

    /**
     * Maps Tribes game modes to translatable resource
     */
    @NonNull
    public static final Map<String, Integer> GAMEMODE_STRINGS = new HashMap<>();

    static {
        CLASS_DRAWABLES.put("pathfinder", R.drawable.pathfinder);
        CLASS_DRAWABLES.put("infiltrator", R.drawable.infiltrator);
        CLASS_DRAWABLES.put("sentinel", R.drawable.sentinel);
        CLASS_DRAWABLES.put("soldier", R.drawable.soldier);
        CLASS_DRAWABLES.put("raider", R.drawable.raider);
        CLASS_DRAWABLES.put("technician", R.drawable.technician);
        CLASS_DRAWABLES.put("brute", R.drawable.brute);
        CLASS_DRAWABLES.put("doombringer", R.drawable.doombringer);
        CLASS_DRAWABLES.put("juggernaught", R.drawable.juggernaught);

        CLASS_STRINGS.put("pathfinder", R.string.pathfinder);
        CLASS_STRINGS.put("infiltrator", R.string.infiltrator);
        CLASS_STRINGS.put("sentinel", R.string.sentinel);
        CLASS_STRINGS.put("soldier", R.string.soldier);
        CLASS_STRINGS.put("raider", R.string.raider);
        CLASS_STRINGS.put("technician", R.string.technician);
        CLASS_STRINGS.put("brute", R.string.brute);
        CLASS_STRINGS.put("doombringer", R.string.doombringer);
        CLASS_STRINGS.put("juggernaught", R.string.juggernaut);

        GAMEMODE_STRINGS.put("arena", R.string.pathfinder);
        GAMEMODE_STRINGS.put("cah", R.string.infiltrator);
        GAMEMODE_STRINGS.put("ctf", R.string.sentinel);
        GAMEMODE_STRINGS.put("ctfblitz", R.string.soldier);
        GAMEMODE_STRINGS.put("rabbit", R.string.raider);
        GAMEMODE_STRINGS.put("tdm", R.string.technician);

        STAT_STRINGS.put("K:D", R.string.key_kill_death_ratio);
        STAT_STRINGS.put("Assists", R.string.key_assists);
        STAT_STRINGS.put("Base Assets Destroyed", R.string.key_base_assets_destroyed);
        STAT_STRINGS.put("Base Repairs", R.string.key_base_repairs);
        STAT_STRINGS.put("Base Upgrades Purchased", R.string.key_belt_kills);
        STAT_STRINGS.put("Belt Kills", R.string.key_belt_kills);
        STAT_STRINGS.put("Callin Kills", R.string.key_call_in_kills);
        STAT_STRINGS.put("Callins Made", R.string.key_call_ins);
        STAT_STRINGS.put("Created Datetime", R.string.key_user_created);
        STAT_STRINGS.put("Deaths", R.string.key_deaths);
        STAT_STRINGS.put("Flag Caps", R.string.key_flag_caps);
        STAT_STRINGS.put("Flag Returns", R.string.key_flag_returns);
        STAT_STRINGS.put("Full Regenerations", R.string.key_full_regenerations);
        STAT_STRINGS.put("Generators Destroyed", R.string.key_generators_destroyed);
        STAT_STRINGS.put("Headshots", R.string.key_headshots);
        STAT_STRINGS.put("High Speed Flag Grabs", R.string.key_high_speed_grabs);
        STAT_STRINGS.put("Kills", R.string.key_kills);
        STAT_STRINGS.put("Kills In Vehicle", R.string.key_vehicle_kills);
        STAT_STRINGS.put("Last Login Datetime", R.string.key_last_online);
        STAT_STRINGS.put("Rank", R.string.key_playerRank);
        STAT_STRINGS.put("Matches Completed", R.string.key_matches_completed);
        STAT_STRINGS.put("Melee Kills", R.string.key_melee_kills);
        STAT_STRINGS.put("Midairs", R.string.key_midairs);
        STAT_STRINGS.put("Multikills", R.string.key_multi_kills);
        STAT_STRINGS.put("Ski Distance", R.string.key_ski_distance);
        STAT_STRINGS.put("Sprees", R.string.key_sprees);
        STAT_STRINGS.put("Top Speed", R.string.key_top_speed);
        STAT_STRINGS.put("Vehicle Roadkills", R.string.key_roadkills);
        STAT_STRINGS.put("Vehicles Destroyed", R.string.key_vehicles_destroyed);
        STAT_STRINGS.put("Stats Updated", R.string.key_stats_updated);
    }
}
