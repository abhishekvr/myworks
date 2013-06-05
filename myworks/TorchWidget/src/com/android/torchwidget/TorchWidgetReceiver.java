package com.android.torchwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TorchWidgetReceiver extends BroadcastReceiver {
	private static boolean isLightOn = false;
	private static Camera camera;

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.activity_main);


		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		appWidgetManager.updateAppWidget(new ComponentName(context,
				TorchWidgetProvider.class), views);

		if (isLightOn) {
			if (camera != null) {
                
                // stop camera preview
                
                log.i("appwidget","stopping preview");
				camera.stopPreview();
				camera.release();
				camera = null;
				isLightOn = false;
			}

		} else {
            
			// Open the default i.e. the first rear facing camera.
			camera = Camera.open();

			if (camera == null) {
				Toast.makeText(context, "no camera", Toast.LENGTH_SHORT).show();
			} else {
                
				// Set the torch flash mode
                // setting the camera to flash mode
                
				Parameters param = camera.getParameters();
				param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                
                // setting camera parameters will enter catch if flash is not working
                
				try {
					camera.setParameters(param);
					camera.startPreview();
					isLightOn = true;
				} catch (Exception e) {
					Toast.makeText(context, "no flash", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}
}