package com.example.structure.entity.tileentity;

import com.example.structure.entity.trader.EntityAbstractAvalon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityHealthCap extends TileEntity implements ITickable {
    @Override
    public void update() {
        List<EntityAbstractAvalon> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAbstractAvalon.class, this.getRenderBoundingBox().grow(25D), e -> !e.getIsInvulnerable());

        if(!nearbyBoss.isEmpty()) {
            for(EntityAbstractAvalon blossom : nearbyBoss) {
                double health = blossom.getHealth() / blossom.getMaxHealth();
                if(health < blossom.stateLine - 0.02) {
                    blossom.heal(1.0f);
                }

            }
        }
    }
}
