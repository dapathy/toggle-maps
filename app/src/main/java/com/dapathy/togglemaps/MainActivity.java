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

import com.topjohnwu.superuser.Shell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
	private final static String Package_Name = "com.google.android.apps.maps";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			setStatusTextView(getPackageStatus());
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
		boolean status = getPackageStatus();
		togglePackage(status);
		setStatusTextView(!status);

		// create shortcut if enabling package and setting is enabled
		if (!status) {
			SharedPreferences sharedPreferences =
					PreferenceManager.getDefaultSharedPreferences(this);
			if (sharedPreferences.getBoolean("create_shortcut_setting", false)) {
				createShortCut();
			}
		}
	}

	private void togglePackage(boolean status) {
		String command = status ? "disable" : "enable";
		Shell.su(String.format("pm %s %s", command, Package_Name)).exec();
	}

	private void setStatusTextView(boolean status) {
		TextView textView = findViewById(R.id.package_status);
		String statusText = status ? "enabled" : "disabled";
		textView.setText(String.format("Maps is %s", statusText));
	}

	private boolean getPackageStatus() throws PackageManager.NameNotFoundException {
		return this.getPackageManager().getApplicationInfo(Package_Name, 0).enabled;
	}

	private void createShortCut() throws PackageManager.NameNotFoundException {
		Drawable icon = this.getPackageManager().getApplicationIcon(Package_Name);
		ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(this, "test")
				.setIntent(this.getPackageManager().getLaunchIntentForPackage(Package_Name).setAction(Intent.ACTION_MAIN))
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
