package com.dapathy.togglemaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
	private Package mapsPackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			this.mapsPackage = new Package(this, Constants.Maps_Package_Name);
			setStatusTextView(this.mapsPackage.getStatus());
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onToggleMapsClick(View view) throws PackageManager.NameNotFoundException {
		boolean status = this.mapsPackage.getStatus();
		this.mapsPackage.toggle(status);
		setStatusTextView(!status);

		// create shortcut if enabling package and setting is enabled
		if (!status) {
			SharedPreferences sharedPreferences =
					PreferenceManager.getDefaultSharedPreferences(this);
			if (sharedPreferences.getBoolean(Constants.Shortcut_Preference_Name, false)) {
				createShortCut();
			}
		}
	}

	private void setStatusTextView(boolean status) {
		TextView textView = findViewById(R.id.package_status);
		String statusText = status ? "enabled" : "disabled";
		textView.setText(String.format("Maps is %s", statusText));
	}

	private void createShortCut() throws PackageManager.NameNotFoundException {
		Drawable icon = this.getPackageManager().getApplicationIcon(Constants.Maps_Package_Name);
		ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(this, "test")
				.setIntent(this.getPackageManager().getLaunchIntentForPackage(Constants.Maps_Package_Name).setAction(Intent.ACTION_MAIN))
				.setShortLabel("Maps")
				.setIcon(IconCompat.createWithBitmap(getBitmapFromDrawable(icon)))
				.build();
		ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null);
	}

	static private Bitmap getBitmapFromDrawable(Drawable drawable) {
		final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bmp);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bmp;
	}
}
