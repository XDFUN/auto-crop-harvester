package com.xdfun.autocropharvester.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

interface BlockUpdateCallback {
    companion object {
        var Event: Event<BlockUpdateCallback> = EventFactory.createArrayBacked(
            BlockUpdateCallback::class.java
        ) { listeners ->
            object : BlockUpdateCallback {
                override fun onBlockUpdate(blockPos: BlockPos, blockState: BlockState) {
                    for (listener in listeners) {
                        listener.onBlockUpdate(blockPos, blockState)
                    }
                }
            }
        }
    }

    fun onBlockUpdate(blockPos: BlockPos, blockState: BlockState)
}