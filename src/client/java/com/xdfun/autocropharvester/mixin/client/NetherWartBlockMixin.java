package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.blocks.MaturableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetherWartBlock.class)
public abstract class NetherWartBlockMixin implements MaturableBlock {
    @Accessor("MAX_AGE")
    private static int accessMaxAge() {
        throw new AssertionError();
    }

    @Accessor("AGE")
    private static IntProperty getAgeProperty() {
        throw new AssertionError();
    }

    @Override
    public boolean hasCrop(@NotNull BlockState state, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public boolean isMature(@NotNull BlockState blockState, @NotNull BlockPos blockPos) {
        return blockState.get(getAgeProperty()) >= accessMaxAge();
    }
}
