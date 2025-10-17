package com.xdfun.autocropharvester.gui

import com.mojang.serialization.Codec
import com.xdfun.autocropharvester.viewmodels.EnableDisableStatus
import net.minecraft.client.option.SimpleOption
import java.util.Optional
import java.util.stream.Stream

object Callbacks {
    val enableDisableStatus = SimpleOption.LazyCyclingCallbacks(
        {
            Stream.concat(
                Stream.of(EnableDisableStatus.Enable),
                Stream.of(EnableDisableStatus.Disable)
            ).toList()
        },
        { value: EnableDisableStatus -> Optional.of(value) },
        Codec.INT.xmap(
            { value: Int -> EnableDisableStatus.entries[value] },
            { value: EnableDisableStatus -> value.ordinal }
        ))
}