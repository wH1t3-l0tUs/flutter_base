package io.driverdoc.testapp.ui.detectdocument;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Created by Denis on 17.09.2016.
 */
public interface ISettingsFragment {
	boolean save(@NonNull SharedPreferences.Editor editor);
}
