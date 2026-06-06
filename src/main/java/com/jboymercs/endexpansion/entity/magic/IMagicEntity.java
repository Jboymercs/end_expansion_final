package com.jboymercs.endexpansion.entity.magic;

import net.minecraft.entity.Entity;

public interface IMagicEntity {

      boolean getDoesEntityMove();

     boolean isDodgeable();

    Entity getOwnerFromMagic();
}
