package no.kriben.bussan;

import java.util.List;

import no.kriben.busstopstrondheim.model.BusStop;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BusStopMenuHandler {
    public void configureMenu(Activity activity, Menu menu, BusStop busStop) {
        List<Integer> favorites = getSavedFavoriteBusStops(activity);
        boolean isFavorite = favorites.contains(busStop.getCode());
        MenuItem addItem = menu.findItem(R.id.add_favorite);
        addItem.setVisible(!isFavorite);

        MenuItem removeItem = menu.findItem(R.id.remove_favorite);
        removeItem.setVisible(isFavorite);

        MenuItem showInMapItem = menu.findItem(R.id.show_in_map);
        showInMapItem.setVisible(true);
    }

    public boolean handleContextItemSelected(Activity activity, MenuItem item, BusStop busStop) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_favorite) {
            List<Integer> favorites = getSavedFavoriteBusStops(activity);
            favorites.add(busStop.getCode());
            saveFavoriteBusStops(activity, favorites);

            Toast.makeText(activity, "Added " + busStop.getName() + " to favorites!", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (itemId == R.id.remove_favorite) {
            List<Integer> favorites = getSavedFavoriteBusStops(activity);
            favorites.remove(new Integer(busStop.getCode()));
            saveFavoriteBusStops(activity, favorites);

            Toast.makeText(activity, "Removed " + busStop.getName() + " from favorites!", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (itemId == R.id.show_in_map) {
            // Use trick from here to center on a position with a marker
            // http://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android/4433117
            String uri = "geo:0,0?q="+ busStop.getPosition().getLatitude() + "," + busStop.getPosition().getLongitude() + " (" + busStop.getName() + ")";
            try {
                activity.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "Unable to show location in map.", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        else {
            return false;
        }
    }


    private void saveFavoriteBusStops(Activity activity, List<Integer> favorites) {
        SharedPreferences settings = activity.getSharedPreferences("BusStopPreferences", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("favorites", PreferencesUtil.encodeBusStopString(favorites));
        prefEditor.commit();

    }

    private List<Integer> getSavedFavoriteBusStops(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences("BusStopPreferences", Activity.MODE_PRIVATE);
        return PreferencesUtil.decodeBusStopString(settings.getString("favorites", activity.getString(R.string.default_busstops)));
    }
}