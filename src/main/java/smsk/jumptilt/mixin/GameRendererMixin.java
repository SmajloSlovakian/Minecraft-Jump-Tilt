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
    private float velYbuffer=0;

    @Inject(method = "render",at = @At("HEAD"))
    private void frame(float tickDelta, long startTime, boolean tick, CallbackInfo ci){
        try{
            velYbuffer=MathHelper.lerp((float)Math.pow(tickDelta, Config.cfg.speed), velYbuffer, (float)JT.mc.player.getVelocity().y);
        }catch(Exception e){}
    }

    @Inject(method = "bobView",at = @At("TAIL"))
    private void jumpTiltTest(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.clamp(JT.mc.player.prevPitch+velYbuffer*-Config.cfg.amount,-90,90)-JT.mc.player.prevPitch));
    }
}
