package si.komp.tribesascendstats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TribesUtils {
    @NonNull
    private final Context context;

    @NonNull
    public static final Map<String, Integer> CLASS_DRAWABLES = new HashMap<>(11), CLASS_STRINGS = new HashMap<>(11), MAP_DRAWABLES = new HashMap<>();

    static {
        CLASS_DRAWABLES.put("pathfinder", R.drawable.pathfinder);
        CLASS_DRAWABLES.put("n. pathfinder", R.drawable.pathfinder);
        CLASS_DRAWABLES.put("infiltrator", R.drawable.infiltrator);
        CLASS_DRAWABLES.put("sentinel", R.drawable.sentinel);
        CLASS_DRAWABLES.put("soldier", R.drawable.soldier);
        CLASS_DRAWABLES.put("raider", R.drawable.raider);
        CLASS_DRAWABLES.put("technician", R.drawable.technician);
        CLASS_DRAWABLES.put("brute", R.drawable.brute);
        CLASS_DRAWABLES.put("doombringer", R.drawable.doombringer);
        CLASS_DRAWABLES.put("juggernaught", R.drawable.juggernaught);
        CLASS_DRAWABLES.put("juggernaut", R.drawable.juggernaught);

        CLASS_STRINGS.put("pathfinder", R.string.pathfinder);
        CLASS_STRINGS.put("n. pathfinder", R.string.pathfinder);
        CLASS_STRINGS.put("infiltrator", R.string.infiltrator);
        CLASS_STRINGS.put("sentinel", R.string.sentinel);
        CLASS_STRINGS.put("soldier", R.string.soldier);
        CLASS_STRINGS.put("raider", R.string.raider);
        CLASS_STRINGS.put("technician", R.string.technician);
        CLASS_STRINGS.put("brute", R.string.brute);
        CLASS_STRINGS.put("doombringer", R.string.doombringer);
        CLASS_STRINGS.put("juggernaught", R.string.juggernaut);
        CLASS_STRINGS.put("juggernaut", R.string.juggernaut);

        MAP_DRAWABLES.put("airarena", R.drawable.airarena);
        MAP_DRAWABLES.put("arxnovena", R.drawable.arxnovena);
        MAP_DRAWABLES.put("bellaomega", R.drawable.bellaomega);
        MAP_DRAWABLES.put("blueshift", R.drawable.blueshift);
        MAP_DRAWABLES.put("canyoncrusaderevival", R.drawable.canyoncrusaderevival);
        MAP_DRAWABLES.put("crossfire", R.drawable.crossfire);
        MAP_DRAWABLES.put("dangerouscrossing", R.drawable.dangerouscrossing);
        MAP_DRAWABLES.put("drydock", R.drawable.drydock);
        MAP_DRAWABLES.put("drydocknight", R.drawable.drydocknight);
        MAP_DRAWABLES.put("fraytown", R.drawable.fraytown);
        MAP_DRAWABLES.put("hinterlands", R.drawable.hinterlands);
        MAP_DRAWABLES.put("icecoaster", null); // TODO
        MAP_DRAWABLES.put("inferno", R.drawable.inferno);
        MAP_DRAWABLES.put("katabatic", R.drawable.katabatic);
        MAP_DRAWABLES.put("lavaarena", R.drawable.lavaarena);
        MAP_DRAWABLES.put("miasma", R.drawable.miasma);
        MAP_DRAWABLES.put("nightabatic", R.drawable.nightabatic);
        MAP_DRAWABLES.put("outskirts", R.drawable.outskirts);
        MAP_DRAWABLES.put("perdition", null); // TODO
        MAP_DRAWABLES.put("permafrost", R.drawable.permafrost);
        MAP_DRAWABLES.put("quicksand", R.drawable.quicksand);
        MAP_DRAWABLES.put("raindance", R.drawable.raindance);
        MAP_DRAWABLES.put("stonehenge", R.drawable.stonehenge);
        MAP_DRAWABLES.put("sulfurcove", R.drawable.sulfurcove);
        MAP_DRAWABLES.put("sunstar", R.drawable.sunstar);
        MAP_DRAWABLES.put("tartarus", R.drawable.tartarus);
        MAP_DRAWABLES.put("templeruins", R.drawable.templeruins);
        MAP_DRAWABLES.put("terminus", null);// TODO
        MAP_DRAWABLES.put("undercroft", R.drawable.undercroft);
        MAP_DRAWABLES.put("walledin", R.drawable.walledin);
    }

    public TribesUtils(@NonNull Context context) {
        this.context = context;
    }

    /**
     * @param id A string ID from the Android string i18n system (Example: R.string.foo)
     * @return The searched string
     */
    @NonNull
    public String getString(int id) {
        return context.getResources().getString(id);
    }

    /**
     * @param className Name of a Tribes ascend class
     *                  {Pathfinder | N. Pathfinder | Infiltrator | Sentinel | Soldier | Raider | Technician | Brute | Doombringer | Juggernaught | Juggernaut}
     *                  Case insensitive
     * @return The translated name, if it is present into the Android string i18n system, the className itself otherwise
     */
    @NonNull
    public String translateClassName(@NonNull String className) {
        try {
            return getString(CLASS_STRINGS.get(className.toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
            return className;
        }
    }

    /**
     * @param className Name of a Tribes ascend class
     *                  {Pathfinder | N. Pathfinder | Infiltrator | Sentinel | Soldier | Raider | Technician | Brute | Doombringer | Juggernaught | Juggernaut}
     *                  Case insensitive
     * @return The ID of the drawable from tha Android resource system (Example: R.drawable.foo)
     */
    @Nullable
    public Integer getClassDrawableId(@NonNull String className) {
        try {
            return CLASS_DRAWABLES.get(className.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param mapName Name of a Tribes Ascend map
     *                {}
     *                Case insensitive
     * @return The ID of the drawable from tha Android resource system (Example: R.drawable.foo)
     */
    @Nullable
    public Integer getMapDrawableId(@NonNull String mapName) {
        try {
            return MAP_DRAWABLES.get(mapName.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
