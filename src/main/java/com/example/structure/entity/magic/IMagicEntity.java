package com.example.structure.entity.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public interface IMagicEntity {

      boolean getDoesEntityMove();

     boolean isDodgeable();

    Entity getOwnerFromMagic();
}
