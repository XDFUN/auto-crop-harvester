package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.blocks.MaturableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin implements MaturableBlock {

    @Override
    public boolean isMature(@NotNull BlockState state, @NotNull BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;

        if(world == null) {
            return false;
        }

        BlockPos up = pos.up();
        var blockAbove = world.getBlockState(up).getBlock();

        return blockAbove instanceof SugarCaneBlock;
    }
}
