package com.dapathy.togglemaps;

import android.app.IntentService;
import android.app.Notification;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


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
		
		registerReceiver(new CarModeReceiver(), new IntentFilter(UiModeManager.ACTION_ENTER_CAR_MODE));
	}

	private class CarModeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

		}
	}
}
