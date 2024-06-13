package smsk.jumptilt.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import smsk.jumptilt.Config;
import smsk.jumptilt.JT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private float tiltDegrees = 0;

    private float clampingAmount = 0.1f;

    @Inject(method = "render",at = @At("HEAD"))
    private void frame(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        try {
            float targetTilt = (float) JT.mc.player.getVelocity().y * -Config.cfg.amount;

            targetTilt = customClamp(JT.mc.player.prevPitch + targetTilt, -90 - Config.cfg.upperClamping, 90 + Config.cfg.lowerClamping, clampingAmount, clampingAmount) - JT.mc.player.prevPitch;
            if (JT.mc.player.isOnGround()) targetTilt = 0;
            
            tiltDegrees = MathHelper.clamp((tiltDegrees - targetTilt) * (float) Math.pow(Config.cfg.speed, JT.mc.getLastFrameDuration()) + targetTilt, -90, 90);
        } catch (Exception e) {}
    }

    @Inject(method = "bobView",at = @At("TAIL"))
    private void jumpTiltTest(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tiltDegrees));
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
