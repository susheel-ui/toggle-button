package com.sverr.toggle_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes

class custom_toggle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var toggleBackground: View
    private lateinit var toggleThumb: View

    private var isChecked = false
    private var animationDuration = 200L

    private var toggleOnDrawable: Int = R.drawable.bg_toggle_on
    private var toggleOffDrawable: Int = R.drawable.bg_toggle_off
    private var thumbOnDrawable: Int = R.drawable.bg_toggle_thumb_on
    private var thumbOffDrawable: Int = R.drawable.bg_toggle_thumb_off

    private var listener: ((Boolean) -> Unit)? = null

    init {

        LayoutInflater.from(context)
            .inflate(R.layout.toggle_botton, this, true)

        toggleBackground = findViewById(R.id.toggleBackground)
        toggleThumb = findViewById(R.id.toggleThumb)

        setupAttributes(attrs)

        post { updateUI(false) }

        setOnClickListener { toggle() }
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        attrs?.let {

            context.withStyledAttributes(
                it,
                R.styleable.custom_toggle
            ) {

                isChecked =
                    getBoolean(R.styleable.custom_toggle_isChecked, false)

                toggleOnDrawable =
                    getResourceId(
                        R.styleable.custom_toggle_toggleOnDrawable,
                        toggleOnDrawable
                    )

                toggleOffDrawable =
                    getResourceId(
                        R.styleable.custom_toggle_toggleOffDrawable,
                        toggleOffDrawable
                    )

                thumbOnDrawable =
                    getResourceId(
                        R.styleable.custom_toggle_thumbOnDrawable,
                        thumbOnDrawable
                    )

                thumbOffDrawable =
                    getResourceId(
                        R.styleable.custom_toggle_thumbOffDrawable,
                        thumbOffDrawable
                    )

                animationDuration =
                    getInt(
                        R.styleable.custom_toggle_animationDuration,
                        200
                    ).toLong()

            }
        }
    }

    private val View.marginStartValue: Int
        get() = (layoutParams as MarginLayoutParams).marginStart

    private fun toggle() {
        isChecked = !isChecked
        updateUI(true)
        listener?.invoke(isChecked)
    }

    private fun updateUI(animate: Boolean) {

        val moveDistance =
            toggleBackground.width -
                    toggleThumb.width -
                    (toggleThumb.marginStartValue * 2)

        if (isChecked) {

            toggleBackground.setBackgroundResource(toggleOnDrawable)
            toggleThumb.setBackgroundResource(thumbOnDrawable)

            if (animate) {
                toggleThumb.animate()
                    .translationX(moveDistance.toFloat())
                    .setDuration(animationDuration)
                    .start()
            } else {
                toggleThumb.translationX = moveDistance.toFloat()
            }

        } else {

            toggleBackground.setBackgroundResource(toggleOffDrawable)
            toggleThumb.setBackgroundResource(thumbOffDrawable)

            if (animate) {
                toggleThumb.animate()
                    .translationX(0f)
                    .setDuration(animationDuration)
                    .start()
            } else {
                toggleThumb.translationX = 0f
            }
        }
    }

    fun setChecked(value: Boolean) {
        isChecked = value
        updateUI(false)
    }

    fun isChecked(): Boolean = isChecked

    fun setOnCheckedChangeListener(callback: (Boolean) -> Unit) {
        listener = callback
    }
}