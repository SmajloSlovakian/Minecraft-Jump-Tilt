package smsk.jumptilt.mixin;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.MinecraftClient;
import smsk.jumptilt.JT;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "reloadResources",at = @At("HEAD"))
    void onResReload(CallbackInfoReturnable<CompletableFuture<Void>> cir){
        JT.updateConfig();
    }
}
