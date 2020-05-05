package com.dapathy.togglemaps;

import android.content.Context;
import android.content.pm.PackageManager;

import com.topjohnwu.superuser.Shell;

public class Package {
	private Context context;
	private String name;

	public Package(Context context, String name) {
		this.context = context;
		this.name = name;
	}

	public void toggle(boolean status) {
		String command = status ? "disable" : "enable";
		Shell.su(String.format("pm %s %s", command, this.name)).exec();
	}

	public boolean getStatus() throws PackageManager.NameNotFoundException {
		return context.getPackageManager().getApplicationInfo(this.name, 0).enabled;
	}
}
