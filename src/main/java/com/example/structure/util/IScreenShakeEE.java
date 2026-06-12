package com.example.structure.util;

import net.minecraft.entity.Entity;

public interface IScreenShakeEE {
    @Deprecated
    public float getShakeIntensity(Entity viewer, float partialTicks);


    public default float getShakeIntensity(Entity viewer) {
        return this.getShakeIntensity(viewer, 0);
    }
}
