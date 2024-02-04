package com.svxf.legitvisuals.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.Event;

public class AttackEntityEvent extends Event {
    private EntityPlayer playerIn;
    private Entity target;

    public AttackEntityEvent(EntityPlayer playerIn, Entity target)
    {
        this.playerIn = playerIn;
        this.target = target;
    }


    public EntityPlayer getPlayerIn()
    {
        return playerIn;
    }

    public Entity getTarget()
    {
        return target;
    }
}