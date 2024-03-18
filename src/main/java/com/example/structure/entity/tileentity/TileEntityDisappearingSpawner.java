package com.example.structure.entity.tileentity;

import com.example.structure.init.ModBlocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ITickable;

public class TileEntityDisappearingSpawner extends tileEntityMobSpawner implements ITickable {
    @Override
    protected MobSpawnerLogic getSpawnerLogic() {
        return new DisappearingSpawnerLogic(() -> world, () -> pos, ModBlocks.DISAPPEARING_SPAWNER);
    }
}
