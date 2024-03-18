package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectilePurple extends Projectile{
    /**
     * Just A simple Projectile used by the staff
     */

    private static final int PARTICLE_AMOUNT = 1;

    EntityLivingBase master;
    public ProjectilePurple(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, ModConfig.purp_projectile);
        this.setNoGravity(true);
        this.master = throwerIn;
    }

    public ProjectilePurple(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public ProjectilePurple(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

    }


    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / f;
        y = y / f;
        z = z / f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        x = x * velocity;
        y = y * velocity;
        z = z * velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        int ticksInGround = 0;
    }

    @Override
    public Item getItemToRender() {
        return ModItems.PROJECTILE_PURPLE;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.MAELSTROM, new Vec3d(0, 0.1, 0));
        }
    }


    @Override
    protected void onHit(RayTraceResult result) {
        boolean isShootingEntity = result != null && result.entityHit != null && result.entityHit == this.shootingEntity;
        boolean isPartOfShootingEntity = result != null && result.entityHit != null && (result.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) result.entityHit).parent == this.shootingEntity);
        if (isShootingEntity || isPartOfShootingEntity) {
            return;
        }
        float damage = ModConfig.purp_projectile;
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .build();
        ModUtils.handleAreaImpact(1, (e) -> damage, this.shootingEntity, this.getPositionVector(), source, 0.2f, 0);
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.4F));

        super.onHit(result);

    }
}
