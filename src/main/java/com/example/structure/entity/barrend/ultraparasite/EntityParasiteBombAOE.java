package com.example.structure.entity.barrend.ultraparasite;

import com.example.structure.config.MobConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.barrend.EntityBarrendMob;
import com.example.structure.entity.magic.IMagicEntity;
import com.example.structure.init.ModPotions;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityParasiteBombAOE extends EntityBarrendMob implements IAnimatable, IMagicEntity {
    /**
     * A simple low AI that just damages the player and disappears following, creates a nice effect for waves or AOE
     */
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "shoot";


    public EntityParasiteBombAOE(World worldIn) {
        super(worldIn);
        this.setImmovable(true);

        this.setSize(0.7f, 0.75f);
        this.noClip = true;
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(ticksExisted > 20 && ticksExisted < 27) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted == 20) {
            //this.playSound(SoundsHandler.SPORE_IMPACT, 0.75f, 1.0f);
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityBarrendMob)));

            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(base != this && !(base instanceof EntityBarrendMob)) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                        DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                        if(base != null && !world.isRemote) {
                                base.addPotionEffect(new PotionEffect(ModPotions.MADNESS, 400, 0));
                        }
                    }
                }
            }

        }
        if(ticksExisted == 30) {
            this.setDead();
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.GREEN, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.0D)), ModColors.GREEN, new Vec3d(0,0.1,0));
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue((double) (MobConfig.evolved_parasite_attack_damage * 0.8));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_controller", 0, this::predicateIdle));
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
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
