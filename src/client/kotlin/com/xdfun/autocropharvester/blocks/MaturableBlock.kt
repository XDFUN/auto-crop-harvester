package com.xdfun.autocropharvester.blocks

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

interface MaturableBlock {
    fun isMature(state: BlockState, pos: BlockPos): Boolean
}