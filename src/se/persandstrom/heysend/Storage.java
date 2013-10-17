package se.persandstrom.heysend;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Singleton to supervise the use of SharedPreferences
 * @author Per Sandstrom
 *
 */
public class Storage {

	static final String TAG = Storage.class.getSimpleName();

	private static final String STORAGE_NAME = "Storage";

	private static Storage instance;

	public static synchronized Storage getInstance(Context context) {
		if (instance == null) {
			instance = new Storage(context.getApplicationContext());
		}
		return instance;
	}

	private SharedPreferences sharedPreferences;

	private Storage(Context context) {
		sharedPreferences = context.getSharedPreferences(STORAGE_NAME,
				Context.MODE_PRIVATE);
	}

	public void setStorable(Storable storable, String value) {
		Editor edit = sharedPreferences.edit();
		edit.putString(Storable.GCM_REG_ID.toString(), value);
		edit.commit();
	}

	public String getStorable(Storable storable) {
		return sharedPreferences
				.getString(Storable.GCM_REG_ID.toString(), null);
	}

	public enum Storable {
		GCM_REG_ID,	//registration id for gcm 
		REG_ID_APP_VERSION	//the app version when we registered for gcm
		;
	}

}
