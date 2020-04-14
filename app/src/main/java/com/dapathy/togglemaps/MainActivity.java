package com.dapathy.togglemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.ShellUtils;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onToggleMapsClick(View view) throws PackageManager.NameNotFoundException {
		final String packageName = "com.google.android.apps.maps";
		togglePackage(packageName);
	}

	private void togglePackage(String packageName) throws PackageManager.NameNotFoundException {
		ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(packageName, 0);
		String command = applicationInfo.enabled ? "disable" : "enable";
		Shell.su(String.format("pm %s %s", command, packageName)).exec();
	}
}
