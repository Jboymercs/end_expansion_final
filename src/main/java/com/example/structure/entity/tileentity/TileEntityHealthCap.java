package com.example.structure.entity.tileentity;

import com.example.structure.entity.trader.EntityAbstractAvalon;
import com.example.structure.entity.trader.EntityAvalon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityHealthCap extends TileEntity implements ITickable {
    @Override
    public void update() {
        List<EntityAvalon> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAvalon.class, this.getRenderBoundingBox().grow(25D), e -> !e.getIsInvulnerable());

        if(!nearbyBoss.isEmpty() && !this.world.isRemote) {
            for(EntityAbstractAvalon blossom : nearbyBoss) {
                double health = blossom.getHealth() / blossom.getMaxHealth();
                EntityLivingBase target = blossom.getAttackTarget();
                if(target != null) {
                    if (health < blossom.stateLine - 0.02) {
                        blossom.heal(1.0f);
                    }
                } else {
                    this.world.setBlockToAir(pos);

                }
            }
        }
    }
}
