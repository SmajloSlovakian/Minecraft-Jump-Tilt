package io.github.smajloslovakian.jumptilt.mixin.client;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;
import io.github.smajloslovakian.jumptilt.JT;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "reloadResourcePacks",at = @At("HEAD"))
    void onResReload(CallbackInfoReturnable<CompletableFuture<Void>> cir){
        JT.updateConfig();
    }

    /*@Inject(method = "openPauseMenu",at = @At("HEAD"))
    void onResReload(boolean b,CallbackInfo ci){
        JT.updateConfig();
    }/* */
}
