package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.blocks.MaturableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin implements MaturableBlock {
    @Shadow public abstract boolean isMature(BlockState state);

    @Override
    public boolean hasCrop(@NotNull BlockState state, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public boolean isMature(@NotNull BlockState blockState, @NotNull BlockPos blockPos){
        return isMature(blockState);
    }
}
