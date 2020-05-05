package com.dapathy.togglemaps;

import android.app.IntentService;
import android.app.Notification;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class CarModeObserverService extends IntentService {
	public CarModeObserverService() {
		super("CarModeObserverService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent == null) return;

		// empty notification to keep service running.
		startForeground(0, new Notification());

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UiModeManager.ACTION_ENTER_CAR_MODE);
		intentFilter.addAction(UiModeManager.ACTION_EXIT_CAR_MODE);
		registerReceiver(new CarModeReceiver(), intentFilter);
	}

	private class CarModeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Package maps = new Package(context, Constants.Maps_Package_Name);
			if (!isEnabled(context)) return;

			if (intent.getAction().equals(UiModeManager.ACTION_ENTER_CAR_MODE)) {
				maps.toggle(false);
			} else if (intent.getAction().equals(UiModeManager.ACTION_EXIT_CAR_MODE)) {
				maps.toggle(true);
			}
		}

		private boolean isEnabled(Context context) {
			SharedPreferences sharedPreferences =
					PreferenceManager.getDefaultSharedPreferences(context);
			return sharedPreferences.getBoolean(Constants.Toggle_Automatically_Preference_Name, false);
		}
	}
}
