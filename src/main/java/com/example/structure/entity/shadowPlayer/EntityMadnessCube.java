package com.example.structure.entity.shadowPlayer;

import com.example.structure.config.MobConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.magic.IMagicEntity;
import com.example.structure.init.ModPotions;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityMadnessCube extends EntityModBase implements IAnimatable, IAnimationTickable, IMagicEntity {

    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_CRYSTAL = "idle";

    public EntityMadnessCube(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.2F, 1.95F);
        this.setNoGravity(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    public EntityMadnessCube(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 1.95F);
        this.setNoGravity(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    private EntityLivingBase target;

    private EntityLivingBase owner;
    public EntityMadnessCube(World world, EntityLivingBase target, EntityLivingBase owner) {
        super(world);
        this.target = target;
        this.owner = owner;
        this.setSize(1.2F, 1.95F);
        this.setNoGravity(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(2, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.GREEN, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1.5)), ModColors.GREEN, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.GREEN, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(ticksExisted > 400) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            this.setDead();
        }
        if(target != null && !world.isRemote) {

            //travel too target
            Vec3d posToTravelToo = target.getPositionVector().add(ModUtils.yVec(1.0D));
            Vec3d currPos = this.getPositionVector();
            Vec3d dir = posToTravelToo.subtract(currPos).normalize();
            ModUtils.addEntityVelocity(this, dir.scale(0.1));

            //Explode on close proximity

            List<EntityLivingBase> targetTooExplode = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityModBase)));

            if(!targetTooExplode.isEmpty()) {
                for(EntityLivingBase base : targetTooExplode) {
                    if(!(base instanceof EntityShadowPlayer)) {
                        base.addPotionEffect(new PotionEffect(ModPotions.MADNESS, 200, 0));
                        base.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0, false, false));
                        Vec3d offset = base.getPositionVector().add(ModUtils.yVec(1.0D));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                        float damage = (float) MobConfig.shadow_player_damage * 2;
                        ModUtils.handleAreaImpact(0.75f, (e)-> damage, this, offset, source, 0.2f, 0, false);
                        world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.8f));
                        this.setDead();
                    }
                }
            }
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
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
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_spike", 0, this::predicateAttack));
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CRYSTAL, true));
        return PlayState.CONTINUE;
    }


    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }



    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }


    @Override
    public boolean getDoesEntityMove() {
        return true;
    }

    @Override
    public boolean isDodgeable() {
        return true;
    }

    @Override
    public EntityLivingBase getOwnerFromMagic() {
        return owner;
    }
}
