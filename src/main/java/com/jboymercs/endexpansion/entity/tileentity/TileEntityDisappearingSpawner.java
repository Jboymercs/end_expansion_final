package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.init.ModBlocks;
import net.minecraft.util.ITickable;

public class TileEntityDisappearingSpawner extends tileEntityMobSpawner implements ITickable {
    @Override
    protected MobSpawnerLogic getSpawnerLogic() {
        return new DisappearingSpawnerLogic(() -> world, () -> pos, ModBlocks.DISAPPEARING_SPAWNER);
    }
}
