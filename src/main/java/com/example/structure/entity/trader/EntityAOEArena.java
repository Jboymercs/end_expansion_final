package com.example.structure.entity.trader;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityAOEArena extends EntityModBase implements IAnimatable {

    /**
     * A spam entity that damages at a certain point
     */
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityAOEArena(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
        this.setSize(0.8F, 7.0F);
    }

    private Entity player;

    public EntityAOEArena(World worldIn, Entity playerIn) {
        super(worldIn);
        this.player = playerIn;
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

      if(player != null) {
          //Player Summoned Entity
          if (this.ticksExisted >= 19 && this.ticksExisted <= 25) {
              world.setEntityState(this, ModUtils.PARTICLE_BYTE);
          }

          if (this.ticksExisted == 22) {
              List<EntityLivingBase> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
              this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.50f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);

              if (!nearbyPlayers.isEmpty()) {
                  for (EntityLivingBase base : nearbyPlayers) {
                      if (ModConfig.enable_pvp_abilities) {
                          if (!(base instanceof EntityAOEArena) && base != player) {
                              Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.4));
                              DamageSource source = ModDamageSource.builder()
                                      .type(ModDamageSource.MOB)
                                      .directEntity(this)
                                      .build();
                              float damage = (float) (ModConfig.avalon_attack_damage * ModConfig.lamented_multiplier);
                              ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0F, 0, false);
                          }
                          //For NON PVP
                      } else if (!(base instanceof EntityAOEArena) && base != player && !(base instanceof EntityPlayer)){
                          Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.4));
                          DamageSource source = ModDamageSource.builder()
                                  .type(ModDamageSource.MOB)
                                  .directEntity(this)
                                  .build();
                          float damage = (float) (ModConfig.avalon_attack_damage * ModConfig.lamented_multiplier);
                          ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0F, 0, false);
                      }
                  }
              }
          }
      } else {
          if (this.ticksExisted >= 19 && this.ticksExisted <= 25) {
              world.setEntityState(this, ModUtils.PARTICLE_BYTE);
          }

          if (this.ticksExisted == 22) {
              List<EntityLivingBase> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
              this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.50f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);

              if (!nearbyPlayers.isEmpty()) {
                  for (EntityLivingBase base : nearbyPlayers) {
                      if (!(base instanceof EntityAOEArena) && !(base instanceof EntityMiniValon) && !(base instanceof EntityAvalon)) {
                          Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.4));
                          DamageSource source = ModDamageSource.builder()
                                  .type(ModDamageSource.MOB)
                                  .directEntity(this)
                                  .build();
                          float damage = (float) (ModConfig.avalon_attack_damage * ModConfig.lamented_multiplier);
                          ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0F, 0, false);
                      }
                  }
              }
          }
      }
        if(this.ticksExisted == 25) {
            this.setDead();
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            if(ModConfig.enableAvalonParticles) {
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.MAELSTROM, new Vec3d(0,0.1,0));
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.MAELSTROM, new Vec3d(0,0.1,0));
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.MAELSTROM, new Vec3d(0,0.1,0));
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

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
