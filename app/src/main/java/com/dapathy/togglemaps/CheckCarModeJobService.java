package com.dapathy.togglemaps;

import android.app.UiModeManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;

public class CheckCarModeJobService extends JobService {
	public static final int Job_Id = 0;

	public static void schedule(Context context) {
		ComponentName componentName = new ComponentName(context, CheckCarModeJobService.class);
		JobInfo.Builder builder = new JobInfo.Builder(Job_Id, componentName)
				.setPeriodic(1000 * 60);
		JobScheduler jobScheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		jobScheduler.schedule(builder.build());
	}

	public static void cancel(Context context) {
		JobScheduler jobScheduler = (JobScheduler)context.getSystemService(JOB_SCHEDULER_SERVICE);
		jobScheduler.cancel(Job_Id);
	}
	@Override
	public boolean onStartJob(JobParameters params) {
		Package maps = new Package(this, Constants.Maps_Package_Name);
		if (this.isCarUiMode()) {
			maps.toggle(false);
		} else {
			maps.toggle(true);
		}
		jobFinished(params, false);
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		return false;
	}

	private boolean isCarUiMode() {
		UiModeManager uiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
		return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR;
	}
}
