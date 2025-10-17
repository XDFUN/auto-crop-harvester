package com.xdfun.autocropharvester.gui

import com.xdfun.autocropharvester.viewmodels.EnableDisableStatus
import com.xdfun.autocropharvester.utils.TranslationKeys
import net.minecraft.client.option.SimpleOption
import net.minecraft.client.option.SimpleOption.ValueTextGetter
import net.minecraft.client.option.SimpleOption.emptyTooltip
import net.minecraft.text.Text

object SimpleOptionFactory {
    fun getEnableDisableOption(
        key: String,
        defaultValue: EnableDisableStatus,
        initialValue: EnableDisableStatus,
        changeCallback: (EnableDisableStatus) -> Unit
    ): SimpleOption<EnableDisableStatus> {
        return getEnableDisableOption(key, emptyTooltip(), defaultValue, initialValue, changeCallback)
    }

    fun getEnableDisableOption(
        key: String,
        tooltipFactory: SimpleOption.TooltipFactory<EnableDisableStatus>,
        defaultValue: EnableDisableStatus,
        initialValue: EnableDisableStatus,
        changeCallback: (EnableDisableStatus) -> Unit
    ): SimpleOption<EnableDisableStatus> {
        return SimpleOption(
            key,
            tooltipFactory,
            getEnableDisableTextGetter(),
            Callbacks.enableDisableStatus,
            defaultValue,
            changeCallback
        ).also {
            it.value = initialValue
        }
    }

    fun get128RadiusSliderOption(
        key: String,
        defaultValue: Double,
        initialValue: Double,
        onChange: (Double) -> Unit
    ): SimpleOption<Double> {
        val radius = 128

        fun toSliderProgress(value: Double): Double {
            return value / radius
        }

        fun toValue(progress: Double): Double {
            return radius * progress
        }

        // we cannot use SimpleOption.DoubleSliderCallbacks.INSTANCE.withModifier as the return type is package-private and cannot be used in this module
        // So we must make sure that every get and set of the slider value is changed to the actual radius value
        return SimpleOption(
            key,
            emptyTooltip(),
            { _, progress: Double -> Text.translatable(key, String.format("%.2f", toValue(progress))) },
            SimpleOption.DoubleSliderCallbacks.INSTANCE,
            defaultValue
        ) { progress: Double -> onChange(toValue(progress)) }.also {
            it.value = toSliderProgress(initialValue)
        }
    }

    private fun getEnableDisableTextGetter(): ValueTextGetter<EnableDisableStatus> {
        return ValueTextGetter { _, value: EnableDisableStatus ->
            when (value) {
                EnableDisableStatus.Enable -> Text.translatable(TranslationKeys.EnableDisableStatus.ENABLE)
                EnableDisableStatus.Disable -> Text.translatable(TranslationKeys.EnableDisableStatus.DISABLE)
            }
        }
    }
}