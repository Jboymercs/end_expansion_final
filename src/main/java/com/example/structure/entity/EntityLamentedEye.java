package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.barrend.EntityBarrendGolem;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.entity.trader.EntityAvalon;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
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

public class EntityLamentedEye extends EntityModBase implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "spin";
    protected EntityPlayer playerToo;

    public EntityLamentedEye(World worldIn, float x, float y, float z, EntityPlayer player) {
        super(worldIn, x, y, z);
        this.setNoAI(true);
        this.setSize(0.2f, 0.2f);
        this.setNoGravity(true);
        this.noClip = true;
        playerToo = player;
    }

    public EntityLamentedEye(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.setSize(0.9f, 2.0f);
        this.setNoGravity(true);
        this.noClip = true;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_spinToo", 0, this::predicateAttack));
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
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

        if(playerToo != null) {
          Vec3d stayPos = playerToo.getPositionVector();
          ModUtils.setEntityPosition(this, stayPos);
        }

        List<EntityLivingBase> nearbyTargets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5D), e-> !e.getIsInvulnerable() && (!(e instanceof EntityPlayer || e instanceof EntityLamentedEye)));

        if(!nearbyTargets.isEmpty()) {
            for(EntityLivingBase entity : nearbyTargets) {
                //Checking nerf for preventing any of the below bosses being exploited
                if (ModConfig.enable_pvp_abilities) {
                    if (!(entity instanceof EntityEndKing) && !(entity instanceof EntityBarrendGolem) && !(entity instanceof EndSeekerPrime) && !(entity instanceof EntityAvalon)) {
                        Vec3d currentPos = entity.getPositionVector();
                        Vec3d newRandPos = new Vec3d(currentPos.x + ModRand.range(-16, 16), currentPos.y + ModRand.range(1, 16), currentPos.z + ModRand.range(-16, 16));
                        ModUtils.attemptTeleport(newRandPos, entity);
                        this.setDead();
                    }
                } else if (!(entity instanceof EntityPlayer)) {
                    if (!(entity instanceof EntityEndKing) && !(entity instanceof EntityBarrendGolem) && !(entity instanceof EndSeekerPrime) && !(entity instanceof EntityAvalon)) {
                        Vec3d currentPos = entity.getPositionVector();
                        Vec3d newRandPos = new Vec3d(currentPos.x + ModRand.range(-16, 16), currentPos.y + ModRand.range(1, 16), currentPos.z + ModRand.range(-16, 16));
                        ModUtils.attemptTeleport(newRandPos, entity);
                        this.setDead();
                    }
                }
            }
        }

    }


    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
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
    public AnimationFactory getFactory() {
        return factory;
    }
}
