package com.example.structure.util;

import net.minecraft.entity.Entity;

public interface IEntityCameraOffset {
    public boolean applyOffset(Entity view, float partialTicks);
}
