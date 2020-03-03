package com.bluestem.materialdropdownview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.res.getStringOrThrow
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.min


class MaterialDropdown
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attributeSet, defStyleAttr) {

    //region Members
    // UI
    private val spinner: Spinner
    private val textInputLayout: TextInputLayout
    private var widthWrapContentMeasured: Boolean = false

    // Colors
    private val widgetActiveColor: Int = ContextCompat.getColor(context, R.color.WidgetActive)
    private val widgetInactiveColor: Int = ContextCompat.getColor(context, R.color.WidgetInactive)
    //endregion


    //region Properties
    override fun isEnabled(): Boolean { return active }
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        active = enabled
    }
    private var active: Boolean = true
        set(value) {
            field = value
            spinner.isEnabled = value
            textInputLayout.isEnabled = value
        }

    var adapter: SpinnerAdapter
        get() { return spinner.adapter }
        set(value) { spinner.adapter = value }

    var label: CharSequence?
        get() { return textInputLayout.hint }
        set(value) { textInputLayout.hint = value }

    var labelTextStyle: Int = 0
        set(value) {
            field = value
            textInputLayout.setHintTextAppearance(value)
        }

    val labelTextSizePx: Int
        @SuppressLint("PrivateResource")
        get() {
            val titleTextStyleAttrs = context.theme.obtainStyledAttributes(labelTextStyle, R.styleable.TextAppearance)
            val defaultTitleTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, context.resources.displayMetrics).toInt()
            return titleTextStyleAttrs.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, defaultTitleTextSize)
        }

    var labelTextColor: Int = widgetActiveColor
        set(value) {
            field = value
            textInputLayout.defaultHintTextColor = makeColorStateList(labelTextColor, disabledLabelTextColor)
        }

    var disabledLabelTextColor: Int = widgetInactiveColor
        set(value) {
            field = value
            textInputLayout.defaultHintTextColor = makeColorStateList(labelTextColor, disabledLabelTextColor)
        }

    var borderColor: Int = widgetActiveColor
        set(value) {
            field = value
            textInputLayout.setBoxStrokeColorStateList(makeColorStateList(borderColor, disabledBorderColor))
        }

    var disabledBorderColor: Int = widgetInactiveColor
        set(value) {
            field = value
            textInputLayout.setBoxStrokeColorStateList(makeColorStateList(borderColor, disabledBorderColor))
        }
    //endregion


    //region Lifecycle functions
    init {
        // Inflate layout.
        inflate(getContext(), R.layout.material_dropdown,this);
        spinner = findViewById(R.id.spinner)
        textInputLayout = findViewById(R.id.border)

        // Read attributes.
        readAttributeSet(attributeSet)
    }

    private fun readAttributeSet(attributeSet: AttributeSet?) {
        val attrs = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.MaterialDropdown,
            0, 0
        )
        try {
            // Active state
            active = attrs.getBoolean(R.styleable.MaterialDropdown_android_enabled, true)

            // Label text
            label = attrs.getStringOrThrow(R.styleable.MaterialDropdown_label)

            // Label style
            labelTextStyle = attrs.getResourceId(R.styleable.MaterialDropdown_labelTextStyle, 0)

            // Label color
            labelTextColor = attrs.getColor(R.styleable.MaterialDropdown_labelTextColor, labelTextColor)
            disabledLabelTextColor = attrs.getColor(R.styleable.MaterialDropdown_disabledLabelTextColor, disabledLabelTextColor)

            // Border color
            borderColor = attrs.getColor(R.styleable.MaterialDropdown_borderColor, borderColor)
            disabledBorderColor = attrs.getColor(R.styleable.MaterialDropdown_disabledBorderColor, disabledBorderColor)
        } finally {
            attrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (layoutParams.width == WRAP_CONTENT && !widthWrapContentMeasured) {
            visibility = View.INVISIBLE
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (changed) {
            // Calculate WRAP_CONTENT width if applicable.
            if (layoutParams.width == WRAP_CONTENT && !widthWrapContentMeasured) {
                widthWrapContentMeasured = true

                spinner.addOnLayoutChangeListener { _, left, _, right, _, _, _, _, _ ->
                    textInputLayout.waitForLayout { visibility = View.VISIBLE }
                    textInputLayout.setLayoutParamsWidth(right - left)
                }
                spinner.setLayoutParamsWidth(WRAP_CONTENT)
            }

            // Set the TextInputLayout's height to match the Spinner's height.
            textInputLayout.setLayoutParamsHeight(spinner.height)

            // Shift the Spinner down to account for the TextInputLayout's hint size.
            spinner.translationY = labelTextSizePx.toFloat() / 4
        }
    }
    //endregion


    //region Helper functions
    private fun makeColorStateList(enabledColor: Int, disabledColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf( intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled) ),
            intArrayOf(enabledColor, disabledColor)
        )
    }
    //endregion
}