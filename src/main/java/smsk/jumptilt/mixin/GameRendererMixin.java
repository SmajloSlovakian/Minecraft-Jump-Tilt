package smsk.jumptilt.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import smsk.jumptilt.Config;
import smsk.jumptilt.JT;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private float tiltDegrees = 0;

    @Inject(method = "bobView",at = @At("TAIL"))
    private void tiltScreen(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        try {
            float targetTilt = (float) JT.mc.player.getVelocity().y * -Config.cfg.amount;

            targetTilt = customClamp(JT.mc.player.prevPitch + targetTilt, -90 - Config.cfg.upperClamping, 90 + Config.cfg.lowerClamping, Config.cfg.lowerClampBreak, Config.cfg.upperClampBreak) - JT.mc.player.prevPitch;
            if (JT.mc.player.isOnGround()) targetTilt = 0;
            
            tiltDegrees = MathHelper.clamp((tiltDegrees - targetTilt) * (float) Math.pow(Config.cfg.speed, JT.mc.getRenderTickCounter().getLastFrameDuration()) + targetTilt, -90, 90);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tiltDegrees));
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
