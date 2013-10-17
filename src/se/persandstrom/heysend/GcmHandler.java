package se.persandstrom.heysend;

import java.io.IOException;

import se.persandstrom.heysend.Storage.Storable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmHandler {

	private static final String TAG = GcmHandler.class.getSimpleName();

	private static final String SENDER_ID = "1067735700431";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	Activity activity;
	Context applicationContext;

	GoogleCloudMessaging gcm;

	public GcmHandler(Activity activity) {
		this.activity = activity;
		this.applicationContext = activity.getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(activity);
	}

	public boolean isRegistered() {
		return getRegistrationId() != null;
	}

	public void register() {
		Log.d(TAG, "register");
		new RegisterThread().execute();
	}

	public boolean checkPlayServices() {
		// TODO make it behave better... like show error dialogs etc
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				activity.finish();
			}
			return false;
		}
		return true;
	}


	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String regId) {
		Log.d(TAG,  "regId: "+regId);
		Log.d(TAG,  regId);
	}

	/**
	 * Gets the stored registrationID and validates against app version.
	 * 
	 * @return The registration ID, or null if there is no valid registration ID
	 */
	private String getRegistrationId() {
		Storage storage = Storage.getInstance(applicationContext);
		String registrationId = storage.getStorable(Storable.GCM_REG_ID);
		if (registrationId == null) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		String registeredVersion = storage.getStorable(Storable.REG_ID_APP_VERSION);
		String currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return null;
		}
		return registrationId;
	}

	private String getAppVersion() {
		try {
			PackageInfo pInfo = applicationContext.getPackageManager().getPackageInfo(
					applicationContext.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			// this should never happen...
			throw new RuntimeException(e);
		}
	}
	
	private class RegisterThread extends  AsyncTask<Void, Void, Exception> {

		private final ProgressDialog progressDialog;
		
		public RegisterThread() {
			progressDialog = new ProgressDialog(activity);
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog.setTitle("vänta plox");
			progressDialog.show();
		};

		@Override
		protected Exception doInBackground(Void... params) {
			try {
				String regId = gcm.register(SENDER_ID);
				Log.i(TAG, "Device registered, registration ID=" + regId);

				// You should send the registration ID to your server over HTTP,
				// so it can use GCM/HTTP or CCS to send messages to your app.
				// The request to your server should be authenticated if your app
				// is using accounts.
				sendRegistrationIdToBackend(regId);

				// For this demo: we don't need to send it because the device
				// will send upstream messages to a server that echo back the
				// message using the 'from' address in the message.

				// Persist the regID - no need to register again.
				Storage storage = Storage.getInstance(applicationContext);
				storage.setStorable(Storable.GCM_REG_ID, regId);
			} catch (IOException ex) {
				return ex;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception exception) {
			//TODO kolla så denna aldrig kastar exception
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(exception != null) {
				exception.printStackTrace();
				//TODO show error
			}
		}
	}

}
