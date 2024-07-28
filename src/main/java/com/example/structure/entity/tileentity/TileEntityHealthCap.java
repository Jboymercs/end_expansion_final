package com.example.structure.entity.tileentity;

import com.example.structure.entity.trader.EntityAbstractAvalon;
import com.example.structure.entity.trader.EntityAvalon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityHealthCap extends TileEntity implements ITickable {
    @Override
    public void update() {
        //Hopefully via converting this to a box, it'll prevent the crash from placing them
        AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        List<EntityAvalon> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAvalon.class, box.grow(25D), e -> !e.getIsInvulnerable());

        if(!nearbyBoss.isEmpty() && !this.world.isRemote) {
            for(EntityAbstractAvalon blossom : nearbyBoss) {
                double health = blossom.getHealth() / blossom.getMaxHealth();
                EntityLivingBase target = blossom.getAttackTarget();
                if(target != null) {
                    if (health < blossom.getStatLine() - 0.02) {
                        blossom.heal(1.0f);
                    }
                }
            }
        }
    }
}
