package com.xdfun.autocropharvester.mixin.client;

import com.xdfun.autocropharvester.callbacks.AutoPlanter;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow public abstract void onBlockUpdate(BlockUpdateS2CPacket packet);

	@Inject(at = @At("TAIL"), method = "onBlockUpdate")
	private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci){
		var instance = AutoPlanter.Companion.getINSTANCE();

		if(instance == null) {
			return;
		}

		instance.onBlockUpdate(packet);
	}
}