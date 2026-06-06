package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.entity.endking.EntityEndKing;
import com.jboymercs.endexpansion.entity.endking.EntityRedCrystal;
import com.jboymercs.endexpansion.entity.knighthouse.EntityKnightBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityTrap extends TileEntity implements ITickable {

    private static final int TRIGGER_COOLDOWN = 200;

    public int cooldown = 0;

    @Override
    public void update() {
        if (world.isRemote) {
            return;
        }

        if (cooldown < TRIGGER_COOLDOWN) {
            cooldown++;
            return;
        }

        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1,2,1));
        List<EntityLivingBase> players = this.world.getEntitiesWithinAABB(EntityLivingBase.class, box, e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndKing || e instanceof EntityRedCrystal || e instanceof EntityKnightBase)));
        if(!players.isEmpty()) {
            EntityRedCrystal spike = new EntityRedCrystal(this.world);
            spike.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.spawnEntity(spike);
            spike.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f);
            cooldown = 0;
        }
    }
}
