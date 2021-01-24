package io.driverdoc.testapp.ui.customview

interface DrawableClickListener {
    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    };
    fun onClick(target: DrawablePosition)
}