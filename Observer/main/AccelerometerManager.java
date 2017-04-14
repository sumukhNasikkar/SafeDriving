package com.android.mk.driving.safety.main;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.android.mk.driving.safety.activity.MainActivity;

/**
 * Android Accelerometer Sensor Manager Archetype
 * @author antoine vianey
 * under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 */
public class AccelerometerManager {

	/** Accuracy configuration */
	private static float threshold 	= 0.2f;
	private static int interval 	= 1000;

	private static Sensor sensor;
	private static SensorManager sensorManager;
	// you could use an OrientationListener array instead
	// if you plans to use more than one listener
	private static AccelerometerListener listener;

	/** indicates whether or not Accelerometer Sensor is supported */
	private static Boolean supported;
	/** indicates whether or not Accelerometer Sensor is running */
	private static boolean running = false;

	/**
	 * Returns true if the manager is listening to orientation changes
	 */
	public static boolean isListening() {
		return running;
	}

	/**
	 * Unregisters listeners
	 */
	public static void stopListening() {
		running = false;
		try {
			if (sensorManager != null && sensorEventListener != null) {
				sensorManager.unregisterListener(sensorEventListener);
			}
		} catch (Exception e) {}
	}

	/**
	 * Returns true if at least one Accelerometer sensor is available
	 */
	public static boolean isSupported() {
		if (supported == null) {
			if (MainActivity.getContext() != null) {
				sensorManager = (SensorManager) MainActivity.getContext().
						getSystemService(Context.SENSOR_SERVICE);
				List<Sensor> sensors = sensorManager.getSensorList(
						Sensor.TYPE_ACCELEROMETER);
				supported = new Boolean(sensors.size() > 0);
			} else {
				supported = Boolean.FALSE;
			}
		}
		return supported;
	}

	/**
	 * Configure the listener for shaking
	 * @param threshold
	 * 			minimum acceleration variation for considering shaking
	 * @param interval
	 * 			minimum interval between to shake events
	 */
	public static void configure(int threshold, int interval) {
		AccelerometerManager.threshold = threshold;
		AccelerometerManager.interval = interval;
	}

	/**
	 * Registers a listener and start listening
	 * @param accelerometerListener
	 * 			callback for accelerometer events
	 */
	public static void startListening(
			AccelerometerListener accelerometerListener) {
		sensorManager = (SensorManager) MainActivity.getContext().
				getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(
				Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensor = sensors.get(0);
			running = sensorManager.registerListener(
					sensorEventListener, sensor, 
					SensorManager.SENSOR_DELAY_GAME);
			listener = accelerometerListener;
		}
	}

	/**
	 * Configures threshold and interval
	 * And registers a listener and start listening
	 * @param accelerometerListener
	 * 			callback for accelerometer events
	 * @param threshold
	 * 			minimum acceleration variation for considering shaking
	 * @param interval
	 * 			minimum interval between to shake events
	 */
	public static void startListening(
			AccelerometerListener accelerometerListener, 
			int threshold, int interval) {
		configure(threshold, interval);
		startListening(accelerometerListener);
	}

	/**
	 * The listener that listen to events from the accelerometer listener
	 */
	private static SensorEventListener sensorEventListener = 
			new SensorEventListener() {

		private float mLastX, mLastY, mLastZ;
		private boolean mInitialized = false;;
		private final float NOISE = (float) 2.0;
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		public void onSensorChanged(SensorEvent event) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			if (!mInitialized) {
				mLastX = x;
				mLastY = y;
				mLastZ = z;
				/*tvX.setText("0.0");
				tvY.setText("0.0");
				tvZ.setText("0.0");*/
				mInitialized = true;

			} else {

				float deltaX = (mLastX - x);
				float deltaY = (mLastY - y);
				float deltaZ = (mLastZ - z);

				float absdeltaX = Math.abs(mLastX - x);
				float absdeltaY = Math.abs(mLastY - y);
				float absdeltaZ = Math.abs(mLastZ - z);

				if (absdeltaX < NOISE) deltaX = (float)0.0;
				if (absdeltaY < NOISE) deltaY = (float)0.0;
				if (absdeltaZ < NOISE) deltaZ = (float)0.0;

				mLastX = x;
				mLastY = y;
				mLastZ = z;
				
				listener.onAccelerationChanged(deltaX, deltaY, deltaZ);

				/*tvX.setText(Float.toString(deltaX));
				tvY.setText(Float.toString(deltaY));
				tvZ.setText(Float.toString(deltaZ));
				iv.setVisibility(View.VISIBLE);

				if (deltaX > deltaY) {
					iv.setImageResource(R.drawable.shaker_fig_1);

				} else if (deltaY > deltaX) {
					iv.setImageResource(R.drawable.shaker_fig_2);
				} else {
					iv.setVisibility(View.INVISIBLE);
				}*/
			}
		}

	};

}