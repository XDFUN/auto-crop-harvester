package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.blocks.MaturableBlock;
import com.xdfun.autocropharvester.blocks.OffsetBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SoulSandBlock.class)
public abstract class SoulSandMixin implements MaturableBlock, OffsetBlock {
    @Unique
    private static ClientWorld getWorld() {
        var instance = MinecraftClient.getInstance();

        if(instance != null) {
            return instance.world;
        }

        return null;
    }

    @Override
    public boolean isMature(@NotNull BlockState state, @NotNull BlockPos pos){
        var world = getWorld();

        if(world == null) {
            return false;
        }

        var cropPos = pos.up();
        var cropState = world.getBlockState(cropPos);

        if(cropState.getBlock() instanceof MaturableBlock) {
            return ((MaturableBlock)cropState.getBlock()).isMature(cropState, cropPos);
        }

        return false;
    }

    @Override
    @NotNull
    public BlockPos offsetAttack(@NotNull BlockState blockState, @NotNull BlockPos blockPos) {
        return blockPos.up();
    }
}
