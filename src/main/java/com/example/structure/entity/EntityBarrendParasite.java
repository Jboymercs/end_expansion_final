package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.barrend.EntityBarrendGolem;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
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

import java.util.List;

public class EntityBarrendParasite extends EntityModBase implements IAnimatable {

    private final String ANIM_CRYSTAL = "pop_up";

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityBarrendParasite(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.9f, 0.9f);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public EntityBarrendParasite(World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 0.9f);
        this.noClip = true;
        this.setNoGravity(true);
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

        if(ticksExisted <= 14) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted == 2) {
            BlockPos pos = this.getPosition();
            if(world.getBlockState(pos).isFullBlock()) {
                this.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                this.setImmovable(true);
            } else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.BARE_PLANT || world.isAirBlock(pos.down())) {
                this.setPosition(pos.getX() + 0.5, pos.getY() - 1, pos.getZ() + 0.5);
                this.setImmovable(true);
            } else {
                this.setImmovable(true);
            }
        }

        if(ticksExisted == 18) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityBarrendGolem || e instanceof EntityBarrendParasite)));
            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.35));
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.MOB)
                            .directEntity(this)
                            .build();
                    float damage = (float) ((ModConfig.barrend_golem_attack_damage * ModConfig.barrend_golem_attack_multiplier) * ModConfig.biome_multiplier);
                    ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0F, 0, false);
                }
            }
        }

        if(this.ticksExisted == 30) {
            this.setDead();
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY - 1, this.posZ));
        if(block.isFullBlock()) {
            if (id == ModUtils.PARTICLE_BYTE) {
                ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(ModUtils.yVec(0.75f))), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
            }
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
        data.addAnimationController(new AnimationController(this, "controller_spike", 0, this::predicateAttack));
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CRYSTAL, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
