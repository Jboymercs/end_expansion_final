package com.example.structure.entity;

import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileAcid extends Projectile{

    private static final int PARTICLE_AMOUNT = 1;
    private static final int EXPOSION_AREA_FACTOR = 2;

    public ProjectileAcid(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileAcid(World worldIn) {
        super(worldIn);
    }

    public ProjectileAcid(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public Item getItemToRender() {
        return ModItems.ACID_PROJECTILE;
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.GREEN, new Vec3d(0, 0.1, 0));
        }

    }


    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .stoppedByArmorNotShields().build();

        ModUtils.handleAreaImpact(EXPOSION_AREA_FACTOR, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source);
        this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
