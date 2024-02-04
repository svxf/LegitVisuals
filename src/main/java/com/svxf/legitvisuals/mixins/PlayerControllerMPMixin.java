package com.svxf.legitvisuals.mixins;

import com.svxf.legitvisuals.events.AttackEntityEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"))
    public void onAttackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {
        System.out.println("ATTACK ENTITY EVENT");
        EventBus.callEvent(new AttackEntityEvent(playerIn, targetEntity));
    }
}
