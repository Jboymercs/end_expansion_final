package com.example.structure.entity;

import com.example.structure.entity.barrend.ultraparasite.ActionParasiteBomb;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileParasiteBomb extends Projectile{

    private static final int PARTICLE_AMOUNT = 1;
    private static final int EXPOSION_AREA_FACTOR = 2;

    public ProjectileParasiteBomb(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        //this.setNoGravity(true);
    }

    public ProjectileParasiteBomb(World worldIn) {
        super(worldIn);
        this.setNoGravity(false);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.GREEN, new Vec3d(0, 0.1, 0));
        }
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.circleCallback(2, 30, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(5.6)), ModColors.GREEN, pos.normalize().scale(0.3).add(ModUtils.yVec(0.1)));
        } );
    }
    @Override
    protected void onHit(RayTraceResult result) {
        if(!world.isRemote) {
            new ActionParasiteBomb().performAction(this, null);
        }
        this.playSound(SoundEvents.BLOCK_SLIME_PLACE, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }

}
