package io.driverdoc.testapp.ui.utils

import android.content.Context
import android.graphics.Typeface
import java.util.*


class TypefaceUtil {
    companion object {

        fun overrideFont(context: Context, defaultFontNameToOverride: String, customFontFileNameInAssets: String) {
            try {
                val customFontTypeface = Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

                val defaultFontTypefaceField = Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
                defaultFontTypefaceField.isAccessible = true
                defaultFontTypefaceField.set(null, customFontTypeface)
            } catch (e: Exception) {
                //Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
            }

        }

        private val TAG = "Typefaces"

        private val cache = Hashtable<String, Typeface>()

        @JvmStatic
        fun get(c: Context, assetPath: String): Typeface? {
            synchronized(cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        val t = Typeface.createFromAsset(c.assets,
                                assetPath)
                        cache[assetPath] = t
                    } catch (e: Exception) {
                        android.util.Log.e(TAG, "Could not get typeface '" + assetPath
                                + "' because " + e.message)
                        return null
                    }

                }
                return cache[assetPath]
            }
        }
    }


}