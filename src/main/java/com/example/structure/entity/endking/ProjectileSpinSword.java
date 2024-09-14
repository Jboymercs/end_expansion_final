package com.example.structure.entity.endking;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityGhostArm;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
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

import java.util.List;

public class ProjectileSpinSword extends Projectile {

    private static final int PARTICLE_AMOUNT = 1;

    public ProjectileSpinSword(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, (float) (MobConfig.projectile_sword_damage * ModConfig.biome_multiplier));
        this.setNoGravity(true);
        this.noClip = true;
    }

    public ProjectileSpinSword(World worldIn) {
        super(worldIn);
    }

    EntityLivingBase doNotTarget;

    public ProjectileSpinSword(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase free) {
        super(worldIn, throwerIn, (float) (MobConfig.projectile_sword_damage * ModConfig.biome_multiplier));
        this.doNotTarget = free;
    }

    public ProjectileSpinSword(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.noClip = true;

    }

    public boolean hasShot = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.ticksExisted == 2) {
            this.playSound(ModSoundHandler.SWORD_SUMMON, 0.8F, 1.0F / (rand.nextFloat() * 0.3F + 0.2F));
        }
        List<EntityEndKing> nearbyBoss = this.world.getEntitiesWithinAABB(EntityEndKing.class, this.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
        if(!nearbyBoss.isEmpty()) {
            hasShot = false;
        } else {
            hasShot = true;
        }

        if(doNotTarget != null) {
            //We want this to target everything that is close and that is not of End King Descendance or the KnightBase
            List<EntityLivingBase> nearbyPlayer = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2D), e -> !e.getIsInvulnerable() || e != doNotTarget);
            if(!nearbyPlayer.isEmpty() && hasShot) {
                for(EntityLivingBase player: nearbyPlayer) {
                    if(player != doNotTarget) {
                        Vec3d playerPos = player.getPositionVector().add(ModUtils.yVec(1));
                        Vec3d currentPos = this.getPositionVector();
                        this.motionX = (playerPos.x - currentPos.x) * 0.2;
                        double d1 = (playerPos.y - currentPos.y) * 0.008;
                        this.motionZ = (playerPos.z - currentPos.z) * 0.2;

                        Vec3d targetedPosVel = new Vec3d(0, d1, 0);
                        ModUtils.addEntityVelocity(this, targetedPosVel);
                    }
                }
            }
        } else {
            //We want this to target everything that is close and that is not of End King Descendance or the KnightBase
            List<EntityLivingBase> nearbyPlayer = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(8D), e -> !e.getIsInvulnerable() || !(e instanceof EntityEndKing || e instanceof EntityKnightBase || e instanceof EntityRedCrystal || e instanceof EntityGhostArm || e instanceof EntityGhostPhase));
            if(!nearbyPlayer.isEmpty() && hasShot) {
                for(EntityLivingBase player: nearbyPlayer) {
                    Vec3d playerPos = player.getPositionVector().add(ModUtils.yVec(1.5));
                    Vec3d currentPos = this.getPositionVector();
                    this.motionX = (playerPos.x - currentPos.x) * 0.2;
                    double d1 = (playerPos.y - currentPos.y) * 0.008;
                    this.motionZ = (playerPos.z - currentPos.z) * 0.2;

                    Vec3d targetedPosVel = new Vec3d(0, d1, 0);
                    ModUtils.addEntityVelocity(this, targetedPosVel);

                }
            }
        }


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
        return ModItems.SPIN_SWORD_ITEM;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }
    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.RED, new Vec3d(0, 0.1, 0));
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
        this.playSound(ModSoundHandler.SWORD_IMPACT, 0.7F, 1.0F / (rand.nextFloat() * 0.4F + 0.4F));
        if(rand.nextInt(8) == 0) {


        }





        super.onHit(result);

    }
}
