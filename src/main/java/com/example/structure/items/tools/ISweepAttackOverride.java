package com.example.structure.items.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface ISweepAttackOverride {
    public void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase entity);
}
