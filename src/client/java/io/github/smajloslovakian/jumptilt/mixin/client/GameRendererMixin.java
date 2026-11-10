package io.github.smajloslovakian.jumptilt.mixin.client;

import org.joml.Matrix3x2fStack;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.DeltaTracker;
//import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
//import net.minecraft.client.renderer.block.model.BlockElementRotation.RotationValue;
import io.github.smajloslovakian.jumptilt.Config;
import io.github.smajloslovakian.jumptilt.JT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private float tiltDegrees = 0;
    private Vec3 lastPosition = null;
    private float deltaY = 0;

    @ModifyVariable(method = "renderLevel", at = @At("STORE"), name = "cameraState")
    public CameraRenderState render(CameraRenderState cameraState) {
        if (lastPosition == null) {
            lastPosition = cameraState.pos;
        }
        deltaY = -(float) (lastPosition.y - cameraState.pos.y);
        lastPosition = cameraState.pos;
        JT.print(deltaY);
        return cameraState;
    }

    @Inject(method = "bobView", at = @At("TAIL"))
    private void tiltScreen(CameraRenderState cameraState, PoseStack matrices, CallbackInfo ci) {
        try {
            float targetTilt = deltaY * -Config.cfg.amount;

            targetTilt = customClamp(JT.mc.player.xRotO + targetTilt, -90 - Config.cfg.upperClamping, 90 + Config.cfg.lowerClamping, Config.cfg.upperClampBreak, Config.cfg.lowerClampBreak) - JT.mc.player.xRotO;
            if (JT.mc.player.onGround()) targetTilt = 0;
            
            tiltDegrees = Math.clamp((tiltDegrees - targetTilt) * (float) Math.pow(Config.cfg.speed, JT.mc.getDeltaTracker().getGameTimeDeltaTicks()) + targetTilt, -90, 90);
            //matrices.mulPose((Matrix4fc) Axis.XP.rotationDegrees(tiltDegrees));
            matrices.rotateDegrees(Axis.XP, targetTilt);
            
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
