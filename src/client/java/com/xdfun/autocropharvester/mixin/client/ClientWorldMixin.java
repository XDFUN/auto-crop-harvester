package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.events.BlockUpdateCallback;
import com.xdfun.autocropharvester.utils.ModIdUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Unique
    private static final Logger _logger = LoggerFactory.getLogger(ModIdUtils.MOD_ID);

    @Inject(at = @At("HEAD"), method="processPendingUpdate")
    private void processPendingUpdate(BlockPos pos, BlockState state, Vec3d playerPos, CallbackInfo ci){
        _logger.trace("processPendingUpdate [Pos: {}, State: {}]", pos, state);

        BlockUpdateCallback.Companion.getEvent().invoker().onBlockUpdate(pos, state);
    }
}
