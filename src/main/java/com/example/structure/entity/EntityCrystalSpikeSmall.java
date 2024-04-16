package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCrystalSpikeSmall extends Projectile{
    private static final int PARTICLE_AMOUNT = 1;



    private final String ANIM_IDLE = "idle";


    public EntityCrystalSpikeSmall(World worldIn, EntityLivingBase throwerIn, float damage, ItemStack stack) {
        super(worldIn, throwerIn, (float) (ModConfig.crystal_damage * ModConfig.lamented_multiplier));
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

    public EntityCrystalSpikeSmall(World worldIn) {
        super(worldIn);
    }

    public EntityCrystalSpikeSmall(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }


    @Override
    public Item getItemToRender() {
        return ModItems.CRYSTAL_BALL;
    }




    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }
@Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.AZURE, new Vec3d(0, 0.1, 0));
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .stoppedByArmorNotShields().build();
        ModUtils.handleAreaImpact(1, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source, 0.2f, 0);
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.4F));

        super.onHit(result);

    }


}
