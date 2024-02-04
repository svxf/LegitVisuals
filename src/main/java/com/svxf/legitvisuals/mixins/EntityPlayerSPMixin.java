package com.svxf.legitvisuals.mixins;

import com.mojang.authlib.GameProfile;
import com.svxf.legitvisuals.events.MotionEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 995, value = EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {

    public EntityPlayerSPMixin(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    private void onUpdateWalkingPlayerPre(CallbackInfo ci) {
        MotionEvent event = new MotionEvent(posX, posY, posZ, rotationYaw, rotationPitch, onGround);
        System.out.println("WALK PLAYER EVENT");
        EventBus.callEvent(event);
        if(event.isCancelled()) {
            ci.cancel();
            return;
        }

        posX = event.getX();
        posY = event.getY();
        posZ = event.getZ();

        onGround = event.isOnGround();

        rotationYaw = event.getYaw();
        rotationPitch = event.getPitch();
    }
}
