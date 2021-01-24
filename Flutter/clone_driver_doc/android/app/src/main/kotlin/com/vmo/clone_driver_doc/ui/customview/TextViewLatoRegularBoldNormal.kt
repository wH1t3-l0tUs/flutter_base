package io.driverdoc.testapp.ui.customview

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import io.driverdoc.testapp.ui.utils.TypefaceUtil

class TextViewLatoRegularBoldNormal : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {

            if (!isInEditMode) {
                val style = attrs.getAttributeIntValue(
                        "http://schemas.android.com/apk/res/android",
                        "textStyle",
                        Typeface.NORMAL)
                if (style == Typeface.BOLD) {
                    val myTypeface = TypefaceUtil.get(context, "Lato-Regular.ttf")
                    setTypeface(myTypeface, Typeface.BOLD)
                } else {
                    val myTypeface = TypefaceUtil.get(context, "Lato-Regular.ttf")
                    setTypeface(myTypeface, Typeface.NORMAL)
                }
            }

        }
    }
}