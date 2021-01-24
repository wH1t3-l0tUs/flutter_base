package io.driverdoc.testapp.ui.customview

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import android.util.AttributeSet
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.*
import io.driverdoc.testapp.ui.utils.TypefaceUtil


class EditTextMedium : TextInputEditText {

    private var drawableRight: Drawable? = null
    private var drawableLeft: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null

    var actionX: Int = 0;
    var actionY: Int = 0

    private var clickListener: DrawableClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?,
                                      right: Drawable?, bottom: Drawable?) {
        if (left != null) {
            drawableLeft = left
        }
        if (right != null) {
            drawableRight = right
        }
        if (top != null) {
            drawableTop = top
        }
        if (bottom != null) {
            drawableBottom = bottom
        }
        super.setCompoundDrawables(left, top, right, bottom)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var bounds: Rect?
        if (event.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()
            if (drawableBottom != null && drawableBottom!!.bounds.contains(actionX, actionY)) {
                clickListener!!.onClick(DrawableClickListener.DrawablePosition.BOTTOM)
                return super.onTouchEvent(event)
            }

            if (drawableTop != null && drawableTop!!.bounds.contains(actionX, actionY)) {
                clickListener!!.onClick(DrawableClickListener.DrawablePosition.TOP)
                return super.onTouchEvent(event)
            }

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                bounds = null
                bounds = drawableLeft!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()

                x = actionX
                y = actionY

                if (!bounds!!.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping.  */
                    x = actionX - extraTapArea
                    y = (actionY - extraTapArea).toInt()

                    if (x <= 0)
                        x = actionX
                    if (y <= 0)
                        y = actionY

                    /** Creates square from the smallest value  */
                    if (x < y) {
                        y = x
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener!!
                            .onClick(DrawableClickListener.DrawablePosition.LEFT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false

                }
            }

            if (drawableRight != null) {

                bounds = null
                bounds = drawableRight!!.bounds

                var x: Int
                var y: Int
                val extraTapArea = 13

                x = actionX + extraTapArea
                y = (actionY - extraTapArea).toInt()

                x = width - x

                if (x <= 0) {
                    x += extraTapArea
                }

                if (y <= 0)
                    y = actionY

                /**If drawble bounds contains the x and y points then move ahead. */
                if (bounds!!.contains(x, y) && clickListener != null) {
                    clickListener!!
                            .onClick(DrawableClickListener.DrawablePosition.RIGHT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
                return super.onTouchEvent(event)
            }

        }
        return super.onTouchEvent(event)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        drawableRight = null
        drawableBottom = null
        drawableLeft = null
        drawableTop = null
//        super.finalize()
    }

    fun setDrawableClickListener(listener: DrawableClickListener) {
        this.clickListener = listener
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            this.setLongClickable(false);
            if (!isInEditMode) {


                val style = attrs.getAttributeIntValue(
                        "http://schemas.android.com/apk/res/android",
                        "textStyle",
                        Typeface.NORMAL)
                if (style == Typeface.BOLD) {

                    val myTypeface = TypefaceUtil.get(context, "Lato-Medium.ttf")
                    setTypeface(myTypeface, Typeface.BOLD)


                } else {


                    val myTypeface = TypefaceUtil.get(context, "Lato-Medium.ttf")
                    setTypeface(myTypeface, Typeface.NORMAL)
                }
            }

        }
    }


    override fun onTextContextMenuItem(id: Int): Boolean {

        when (id) {
            android.R.id.copy -> {
                return false
            }

        }
        return false
    }


}