package com.example.structure.entity.lamentorUtil;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.magic.IMagicEntity;
import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.entity.trader.EntityAvalon;
import com.example.structure.entity.trader.EntityMiniValon;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityLamentorWave extends EntityModBase implements IAnimatable, IMagicEntity {

    /**
     * A supplement from the Controller's Wave Entity but slower
     */
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityLamentorWave(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
        this.setSize(0.8F, 9.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionY =0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(world.isAirBlock(this.getPosition().add(0, -1, 0))) {
           this.setDead();
        }
        if (this.ticksExisted >= 39 && this.ticksExisted <= 45) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if (this.ticksExisted == 42) {
            List<EntityLivingBase> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.50f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);

            if (!nearbyPlayers.isEmpty()) {
                for (EntityLivingBase base : nearbyPlayers) {
                    if (!(base instanceof EntityLamentorWave) && !(base instanceof EntityCrystalKnight)) {
                        Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.4));
                        DamageSource source = ModDamageSource.builder()
                                .type(ModDamageSource.MOB)
                                .directEntity(this)
                                .build();
                        float damage = (float) ((MobConfig.attack_damage * 0.75) * ModConfig.lamented_multiplier);
                        ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 1.5F, 0, false);
                    }
                }
            }
        }

        if(this.ticksExisted == 45) {
            this.setDead();
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.AZURE, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.AZURE, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.AZURE, new Vec3d(0,0.1,0));
        }
    }


    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean getDoesEntityMove() {
        return false;
    }

    @Override
    public boolean isDodgeable() {
        return true;
    }

    @Override
    public Entity getOwnerFromMagic() {
        return this;
    }
}
