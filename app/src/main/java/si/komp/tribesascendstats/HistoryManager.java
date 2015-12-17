package si.komp.tribesascendstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Allows to store and load search history in the device storage
 */
public class HistoryManager {
    public final SharedPreferences preferences;
    public final SharedPreferences.Editor editor;

    /**
     * @param context The current context.
     */
    public HistoryManager(@NonNull Context context) {
        this.preferences = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        this.editor = this.preferences.edit();
    }

    /**
     * @return current search history
     */
    @NonNull
    public Set<String> getHistory() {
        // return preferences.getStringSet("usernameHistory", new HashSet<String>());
        // Requires API level 11  :-(

        Set<String> stringSet = new HashSet<>();
        String history = preferences.getString("usernameHistory", "");
        if (!history.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(history, "[,] ");
            while (tokenizer.hasMoreTokens())
                stringSet.add(tokenizer.nextToken());
        }
        return stringSet;
    }

    /**
     * @param newSearchHistory Search history to subtitute the current one
     */
    public void setHistory(@NonNull Set<String> newSearchHistory) {
        // editor.putStringSet("user", new HashSet<String>());
        // Requires API level 11 :-(

        editor.putString("usernameHistory", newSearchHistory.toString());
        editor.apply();
    }

    /**
     * Clear all the users in the history
     */
    public void resetHistory() {
        editor.remove("usernameHistory");
        editor.apply();
    }

    /**
     * @param username Username to be added
     */
    public void addUser(@NonNull String username) {
        Set<String> strings = getHistory();
        strings.add(username.trim());
        setHistory(strings);
    }
}
