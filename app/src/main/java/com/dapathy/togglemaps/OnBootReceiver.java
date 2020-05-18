package com.dapathy.togglemaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.preference.PreferenceManager;

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// don't start service if not enabled.
		boolean isEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.Toggle_Automatically_Preference_Name, false);
		if (!isEnabled) return;

		CheckCarModeJobService.schedule(context);
	}
}
