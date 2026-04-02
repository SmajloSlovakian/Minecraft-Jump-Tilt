package io.github.smajloslovakian.jumptilt.mixin.client;

import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

//import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
//import net.minecraft.client.renderer.block.model.BlockElementRotation.RotationValue;
import io.github.smajloslovakian.jumptilt.Config;
import io.github.smajloslovakian.jumptilt.JT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private float tiltDegrees = 0;

    @Inject(method = "bobView",at = @At("TAIL"))
    private void tiltScreen(CameraRenderState cameraState, PoseStack matrices, CallbackInfo ci){
        try {
            float targetTilt = (float) JT.mc.player.getKnownSpeed().y * -Config.cfg.amount;

            targetTilt = customClamp(JT.mc.player.xRotO + targetTilt, -90 - Config.cfg.upperClamping, 90 + Config.cfg.lowerClamping, Config.cfg.upperClampBreak, Config.cfg.lowerClampBreak) - JT.mc.player.xRotO;
            if (JT.mc.player.onGround()) targetTilt = 0;
            
            tiltDegrees = Math.clamp((tiltDegrees - targetTilt) * (float) Math.pow(Config.cfg.speed, JT.mc.getDeltaTracker().getGameTimeDeltaTicks()) + targetTilt, -90, 90);
            matrices.mulPose(Axis.XP.rotationDegrees(tiltDegrees));
            
        } catch (Exception e) {}
    }

    private float customClamp(float val, float min, float max, float amountForMin, float amountForMax) {
        if (val < min) {
            val = (val - min) * amountForMin + min;
        } else if (val > max) {
            val = (val - max) * amountForMax + max;
        }
        return val;
    }
}
