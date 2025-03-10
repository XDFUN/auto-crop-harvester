package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.blocks.MaturableBlock;
import com.xdfun.autocropharvester.configuration.manager.ConfigurationManager;
import com.xdfun.autocropharvester.planter.PlayerAutoPlanter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private void onPlayerAttackBlock(CallbackInfoReturnable<Boolean> cir) {
        var configuration = ConfigurationManager.Companion.getInstance().getPlayerConfiguration();

        if(!configuration.getEnableAutoPlant()){
            return;
        }

        var checkedWorld = world;

        if (checkedWorld == null) {
            return;
        }

        BlockHitResult blockHitResult = (BlockHitResult) crosshairTarget;

        if (blockHitResult != null) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState state = checkedWorld.getBlockState(blockPos);
            Block block = state.getBlock();

            if (block instanceof MaturableBlock) {
                var planter = PlayerAutoPlanter.Companion.getInstance();

                if(planter == null) {
                    return;
                }

                var blockItem = block.asItem();

                if(configuration.getEnablePrematureAutoPlant()) {
                    planter.notifyBlockBreakRequest(blockItem, blockPos);
                }

                if(((MaturableBlock) block).isMature(state, blockPos)) {
                    planter.notifyBlockBreakRequest(blockItem, blockPos);
                }
            }
        }
    }
}
