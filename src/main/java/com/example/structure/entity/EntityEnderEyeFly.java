package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.AIRandomFly;
import com.example.structure.entity.ai.EntityFlyMoveHelper;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.ModUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityEnderEyeFly extends EntityModBase implements IAnimatable {
    /**
     * This is a small ambience mob that'll locate nearby Constructors and convert them into End Seekers
     */

    private final String ANIM_IDLE = "idle";
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityEnderEyeFly(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.4f, 0.4f);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
    }

    public EntityEnderEyeFly(World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.4f);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.2D);
    }

    protected boolean selectedEntity;
    protected EntityBuffker ItargetEntity;

    protected boolean hasReplaced = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<EntityBuffker> nearbyConstructors = this.world.getEntitiesWithinAABB(EntityBuffker.class, this.getEntityBoundingBox().grow(10D), e -> !e.getIsInvulnerable());

        if(!nearbyConstructors.isEmpty() && !selectedEntity) {
            for(EntityBuffker buffker : nearbyConstructors) {
                double buffkerHealth = buffker.getHealth()/buffker.getMaxHealth();
                if(buffkerHealth < 0.3) {
                    this.selectedEntity = true;
                    ItargetEntity = buffker;
                }
            }
        }
        //This is for the Eye to travel to the Constructor. If it's dead it resets
        if(ItargetEntity != null) {
            Vec3d pos = ItargetEntity.getPositionVector();
            this.getMoveHelper().setMoveTo(pos.x, pos.y, pos.z, 0.35);
            Vec3d distanceFrom = new Vec3d(pos.x, pos.y, pos.z);
            ModUtils.facePosition(ItargetEntity.getPositionVector(), this, 45, 45);
            double distSq = this.getDistanceSq(distanceFrom.x, distanceFrom.y, distanceFrom.z);
            double distance = Math.sqrt(distSq);
            if(distance < 1.5 && !hasReplaced) {
                this.replaceEntity(ItargetEntity);
            }

            if(ItargetEntity.isDead) {
                this.selectedEntity = false;
                ItargetEntity = null;
            }
        }
    }

    protected void replaceEntity(EntityBuffker target) {
        hasReplaced = true;
        if(!world.isRemote) {
            Vec3d taretPos = target.getPositionVector();
            EndSeeker seeker = new EndSeeker(world);
            seeker.setPosition(taretPos.x, taretPos.y, taretPos.z);
            world.spawnEntity(seeker);
            target.setDead();
            this.setDead();
        }
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(5, new AIRandomFly(this));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));

    }


    @Override
    public void travel(float strafe, float vertical, float forward) {
        ModUtils.aerialTravel(this, strafe, vertical, forward);
    }


    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
