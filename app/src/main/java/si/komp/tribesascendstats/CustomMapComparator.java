package si.komp.tribesascendstats;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Map;

public class CustomMapComparator implements Comparator<Map<String, String>> {
    @NonNull
    String key;

    public CustomMapComparator(@NonNull String key) {
        this.key = key;
    }

    /**
     * Gets the values indicated by the {@link #key} from the maps
     * If one of the maps is null or an Exception occurs, 0 is returned
     *
     * @param o1 First map
     * @param o2 Second map
     * @return The inverse comparison of the two values, 0 if an an rreor occurs
     */
    @Override
    public int compare(Map<String, String> o1, Map<String, String> o2) {
        if (o1 == null || o2 == null)
            return 0;

        try {
            Integer val1 = Integer.parseInt(o1.get(key)), val2 = Integer.parseInt(o2.get(key));
            return val2.compareTo(val1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
