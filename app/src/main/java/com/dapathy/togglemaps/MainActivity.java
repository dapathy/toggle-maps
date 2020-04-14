package com.dapathy.togglemaps;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.topjohnwu.superuser.Shell;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private final static String Package_Name = "com.google.android.apps.maps";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			setStatusTextView();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void onToggleMapsClick(View view) throws PackageManager.NameNotFoundException {
		togglePackage();
		setStatusTextView();
	}

	private void togglePackage() throws PackageManager.NameNotFoundException {
		String command = getPackageStatus() ? "disable" : "enable";
		Shell.su(String.format("pm %s %s", command, Package_Name)).exec();
	}

	private void setStatusTextView() throws PackageManager.NameNotFoundException {
		TextView textView = findViewById(R.id.package_status);
		String status = getPackageStatus() ? "enabled" : "disabled";
		textView.setText(String.format("App is: %s", status));
	}

	private boolean getPackageStatus() throws PackageManager.NameNotFoundException {
		return getApplicationContext().getPackageManager().getApplicationInfo(Package_Name, 0).enabled;
	}
}
