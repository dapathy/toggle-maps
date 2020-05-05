package com.dapathy.togglemaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.preference.PreferenceManager;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// don't start service is not enabled.
		boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.Toggle_Automatically_Preference_Name, false);
		if (!isEnabled) return;

		Intent serviceIntent = new Intent(context, CarModeObserverService.class);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			context.startForegroundService(serviceIntent);
		} else {
			context.startService(serviceIntent);
		}
	}
}
