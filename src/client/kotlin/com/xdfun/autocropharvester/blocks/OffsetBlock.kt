package com.xdfun.autocropharvester.blocks

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

interface OffsetBlock {
    fun offsetAttack(blockState: BlockState, blockPos: BlockPos): BlockPos
}