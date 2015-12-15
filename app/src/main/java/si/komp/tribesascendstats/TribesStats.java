package si.komp.tribesascendstats;



import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class TribesStats extends Application  {
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            //TODO: get google-services.json uncomment this
            //mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
