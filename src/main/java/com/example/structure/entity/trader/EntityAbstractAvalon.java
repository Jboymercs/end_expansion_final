package com.example.structure.entity.trader;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAIAvalon;
import com.example.structure.entity.endking.ProjectileSpinSword;
import com.example.structure.entity.util.IPitch;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModProfressions;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityAbstractAvalon extends EntityTrader implements IEntityMultiPart, IPitch {

    /**
     * A Base class to register the Hitboxes of the Avalon
     */


    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart hit_part_1 = new MultiPartEntityPart(this, "hit_part_1", 1.0F, 2.3F);
    private final MultiPartEntityPart hit_part_2 = new MultiPartEntityPart(this, "hit_part_2", 0.5F, 2.3F);
    private final MultiPartEntityPart hit_part_3 = new MultiPartEntityPart(this, "hit_part_3", 0.5F, 2.3F);
    private final MultiPartEntityPart hit_part_4= new MultiPartEntityPart(this, "hit_part_4", 0.5F, 2.3F);
    private final MultiPartEntityPart hit_part_5 = new MultiPartEntityPart(this, "hit_part_5", 0.5F, 2.3F);
    private final MultiPartEntityPart shield_1 = new MultiPartEntityPart(this, "shield_1", 0.6F, 2.5F);
    private final MultiPartEntityPart shield_2 = new MultiPartEntityPart(this, "shield_2", 0.6F, 2.5F);
    private final MultiPartEntityPart shield_3 = new MultiPartEntityPart(this, "shield_3", 0.6F, 2.5F);
    private final MultiPartEntityPart shield_4 = new MultiPartEntityPart(this, "shield_4", 0.6F, 2.5F);

    private static final DataParameter<Boolean> OPEN = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLOSE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OPEN_STATE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> I_AM_BOSS = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> AVALON_MODE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLOSE_STATE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_AOE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_LAZERS = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SMASH_ATTACK = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PROJECTILE_ATTACK = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT_ATTACK = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_STATE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractAvalon.class, DataSerializers.FLOAT);
    public boolean IAmAggroed = false;

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Open", this.dataManager.get(OPEN));
        nbt.setBoolean("Close", this.dataManager.get(CLOSE));
        nbt.setBoolean("Open_State", this.dataManager.get(OPEN_STATE));
        nbt.setBoolean("I_Am_Boss", this.dataManager.get(I_AM_BOSS));
        nbt.setBoolean("Avalon_Mode", this.dataManager.get(AVALON_MODE));
        nbt.setBoolean("Close_State", this.dataManager.get(CLOSE_STATE));
        nbt.setBoolean("Cast_Aoe", this.dataManager.get(CAST_AOE));
        nbt.setBoolean("Cast_Lazers", this.dataManager.get(CAST_LAZERS));
        nbt.setBoolean("Smash_Attack", this.dataManager.get(SMASH_ATTACK));
        nbt.setBoolean("Projectile_Attack", this.dataManager.get(PROJECTILE_ATTACK));
        nbt.setBoolean("Teleport_Attack", this.dataManager.get(TELEPORT_ATTACK));
        nbt.setBoolean("Summon_State", this.dataManager.get(SUMMON_STATE));
        nbt.setBoolean("Death_State", this.dataManager.get(DEATH_STATE));
        nbt.setFloat("Look", this.getPitch());
        nbt.setFloat("Stat_Line", this.getStatLine());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.dataManager.set(OPEN, nbt.getBoolean("Open"));
        this.dataManager.set(CLOSE, nbt.getBoolean("Close"));
        this.dataManager.set(OPEN_STATE, nbt.getBoolean("Open_State"));
        this.dataManager.set(I_AM_BOSS, nbt.getBoolean("I_Am_Boss"));
        this.dataManager.set(AVALON_MODE, nbt.getBoolean("Avalon_Mode"));
        this.dataManager.set(CLOSE_STATE, nbt.getBoolean("Close_State"));
        this.dataManager.set(CAST_AOE, nbt.getBoolean("Cast_Aoe"));
        this.dataManager.set(CAST_LAZERS, nbt.getBoolean("Cast_Lazers"));
        this.dataManager.set(SMASH_ATTACK, nbt.getBoolean("Smash_Attack"));
        this.dataManager.set(PROJECTILE_ATTACK, nbt.getBoolean("Projectile_Attack"));
        this.dataManager.set(TELEPORT_ATTACK, nbt.getBoolean("Teleport_Attack"));
        this.dataManager.set(SUMMON_STATE, nbt.getBoolean("Summon_State"));
        this.dataManager.set(DEATH_STATE, nbt.getBoolean("Death_State"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        this.setStateLine(nbt.getFloat("Stat_Line"));
    }

    public void setOpen(boolean value) {this.dataManager.set(OPEN, Boolean.valueOf(value));}
    public boolean isOpen() {return this.dataManager.get(OPEN);}
    public void setClose(boolean value) {this.dataManager.set(CLOSE, Boolean.valueOf(value));}
    public boolean isClose() {return this.dataManager.get(CLOSE);}
    public void setOpenState(boolean value) {this.dataManager.set(OPEN_STATE, Boolean.valueOf(value));}
    public boolean isOpenState() {return this.dataManager.get(OPEN_STATE);}
    public void setIAmBoss(boolean value) {this.dataManager.set(I_AM_BOSS, Boolean.valueOf(value));}
    public boolean isIAmBoss() {return this.dataManager.get(I_AM_BOSS);}

    public void setFightMode(boolean value) {this.dataManager.set(AVALON_MODE, Boolean.valueOf(value));}

    public boolean isFightMode() {return this.dataManager.get(AVALON_MODE);}
    public void setCloseState(boolean value) {this.dataManager.set(CLOSE_STATE, Boolean.valueOf(value));}
    public boolean isCloseState() {return this.dataManager.get(CLOSE_STATE);}
    public void setCastAOE(boolean value) {this.dataManager.set(CAST_AOE, Boolean.valueOf(value));}
    public boolean isCastAOE() {return this.dataManager.get(CAST_AOE);}
    public void setCastLazers(boolean value) {this.dataManager.set(CAST_LAZERS, Boolean.valueOf(value));}
    public boolean isCastLazers() {return this.dataManager.get(CAST_LAZERS);}
    public void setSmashAttack(boolean value) {this.dataManager.set(SMASH_ATTACK, Boolean.valueOf(value));}
    public boolean isSmashAttack() {return this.dataManager.get(SMASH_ATTACK);}
    public void setProjectileAttack(boolean value) {this.dataManager.set(PROJECTILE_ATTACK, Boolean.valueOf(value));}
    public boolean isProjectileAttack() {return this.dataManager.get(PROJECTILE_ATTACK);}
    public void setTeleportAttack(boolean value) {this.dataManager.set(TELEPORT_ATTACK, Boolean.valueOf(value));}
    public boolean isTeleportAttack() {return this.dataManager.get(TELEPORT_ATTACK);}
    public void setSummonState(boolean value) {this.dataManager.set(SUMMON_STATE, Boolean.valueOf(value));}
    public boolean isSummonState() {return this.dataManager.get(SUMMON_STATE);}
    public void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}

    public void setStateLine(float value) {
        this.dataManager.set(STAT_LINE, Float.valueOf(value));
    }
    public float getStatLine() {
        return this.dataManager.get(STAT_LINE);
    }
    protected boolean hasLazerMinions = false;


    public EntityAbstractAvalon(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.setSize(3F, 3F);
        this.iAmBossMob = true;
        this.hitboxParts = new MultiPartEntityPart[]{model, hit_part_1, shield_1, shield_2, shield_3, shield_4, hit_part_2, hit_part_3, hit_part_4, hit_part_5};
        this.experienceValue = 900;
    }

    @Override
    protected List<EntityVillager.ITradeList> getTrades() {
        return ModProfressions.AVALON_TRADER.getTrades(0);
    }

    @Override
    protected String getVillagerName() {
        return "The Avalon";
    }





    @Override
    public void entityInit() {

        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(OPEN_STATE, Boolean.valueOf(false));
        this.dataManager.register(CLOSE, Boolean.valueOf(false));
        this.dataManager.register(OPEN, Boolean.valueOf(false));
        this.dataManager.register(I_AM_BOSS, Boolean.valueOf(false));
        this.dataManager.register(CLOSE_STATE, Boolean.valueOf(false));
        this.dataManager.register(AVALON_MODE, Boolean.valueOf(false));
        this.dataManager.register(CAST_LAZERS, Boolean.valueOf(false));
        this.dataManager.register(CAST_AOE, Boolean.valueOf(false));
        this.dataManager.register(SMASH_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PROJECTILE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(TELEPORT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_STATE, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(STAT_LINE,  0.75F);
        super.entityInit();
    }

    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected boolean hasLaunchedBlocksOne = false;
    protected boolean hasLaunchedBlocksTwo = false;
    protected boolean hasLaunchedBlocksThree = false;
    public boolean hasSelectedTarget = false;
    protected boolean stateClose = false;
    protected boolean stateOpen = true;

    protected boolean ableTooDisableWhenTargetsAreGone = false;
    protected boolean hasRemovedAITasks;

    //Removes the tasks if there is no current target, this basically helps with the reset back to a Trader for players that lose the fight
    public void removeTasksOfBoss() {
        ModUtils.removeTaskOfType(this.tasks, EntityAIAvalon.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAINearestAttackableTarget.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAIHurtByTarget.class);
        hasRemovedAITasks = true;

        this.setDead();
        EntityAvalon avalon = new EntityAvalon(world);
        avalon.copyLocationAndAnglesFrom(this);
        world.spawnEntity(avalon);
    }

    protected int setTooDeathTimer = 800;
    @Override
    public void onUpdate() {
        super.onUpdate();

        List<EntityMiniValon> nearbySwords = this.world.getEntitiesWithinAABB(EntityMiniValon.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());

        if(!nearbySwords.isEmpty()) {
            this.hasLazerMinions = true;
        } else {
            this.hasLazerMinions = false;
        }


        EntityLivingBase target = this.getAttackTarget();
         /**
        if(target == null && this.ableTooDisableWhenTargetsAreGone) {
            //Hopefully this fixes any weird shenanigans with the boss just outright respawning a new one
            if(!hasRemovedAITasks && setTooDeathTimer < 0) {
                this.setAttackTarget(null);
                this.setIAmBoss(false);
                removeTasksOfBoss();
                this.IAmAggroed = false;
                this.hasSelectedTarget = false;
            } else {
                setTooDeathTimer--;
            }
        } else {
            setTooDeathTimer = 800;
        }
         */

        //This keeps the Players inside the arena basically forcefully pushing them back in
        if(this.isIAmBoss()) {
            if(target != null) {
                double distance = getDistance(target);
                double HealthFactor = this.getHealth() / this.getMaxHealth();
                if(distance > 16) {
                    //Heals The Avalon if you decide to leave it's arena. Only to the stat line point though
                    if(HealthFactor < this.getStatLine() + 0.23) {
                        this.heal(1.0F);
                    }

                    if(this.stateOpen && !this.stateClose) {
                        this.changeAggroSTateToClose();
                    }
                } else {
                    if(!this.stateOpen && this.stateClose && !this.isClose()) {
                        this.changeAggroSTateToOpen();
                    }
                }
            }


        }

        if(this.isCloseState()) {
            //This will lock the Avalon into a heal if the current target is out of range of it
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.setImmovable(true);

        }

        double currentHealth = this.getHealth() / this.getMaxHealth();

        //Initiates Blocks at 75% Health to heal the Entity
  if(this.isIAmBoss()) {
      if (currentHealth < 0.75 && !hasLaunchedBlocksOne) {
          for (int i = 0; i < 4; i++) {
              AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
              BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
              if (setTooPos != null && !world.isRemote) {
                  world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                  this.hasLaunchedBlocksOne = true;
              }
          }
      } else if (hasLaunchedBlocksOne) {
          AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
          BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
          if (setTooPos == null) {
              this.setStateLine(0.5F);
          }
      }
      //Initiates Blocks at 50% Health to heal the Entity
      if (currentHealth < 0.5 && !hasLaunchedBlocksTwo) {
          for (int i = 0; i < 8; i++) {
              AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
              BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
              if (setTooPos != null && !world.isRemote) {
                  world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                  this.hasLaunchedBlocksTwo = true;
              }
          }
      } else if (hasLaunchedBlocksTwo) {
          AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
          BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
          if (setTooPos == null) {
              this.setStateLine(0.25F);
          }
      }
      //Initiates Blocks at 25% Health to heal the Entity
      if (currentHealth < 0.25 && !hasLaunchedBlocksThree) {
          for (int i = 0; i < 12; i++) {
              AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
              BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
              if (setTooPos != null && !world.isRemote) {
                  world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                  this.hasLaunchedBlocksThree = true;
              }
          }
      } else if (hasLaunchedBlocksThree) {
          AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
          BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
          if (setTooPos == null) {

              this.setStateLine(0F);
          }
      }

  }


    }


    public void changeAggroSTateToClose() {
        this.setOpenState(false);
        this.setClose(true);
        this.stateClose = true;

        addEvent(()-> {
            this.setClose(false);
            this.stateOpen = false;
            this.setCloseState(true);
        }, 25);
    }

    public void changeAggroSTateToOpen() {
        this.setCloseState(false);
        this.setOpen(true);
        this.stateOpen = true;
        addEvent(()-> {
        this.setOpen(false);
        this.stateClose = false;
        this.setOpenState(true);
        }, 60);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }
        //Location of Hitboxes
        this.setHitBoxPos(hit_part_1, new Vec3d(0, 0.1, 0));
        this.setHitBoxPos(shield_1, new Vec3d(0.8, 0.1, 0.8));
        this.setHitBoxPos(shield_2, new Vec3d(-0.8, 0.1, 0.8));
        this.setHitBoxPos(shield_3, new Vec3d(0.8, 0.1, -0.8));
        this.setHitBoxPos(shield_4, new Vec3d(-0.8, 0.1, -0.8));

        this.setHitBoxPos(hit_part_2, new Vec3d(-0.75, 0.1, 0));
        this.setHitBoxPos(hit_part_3, new Vec3d(0.75, 0.1, 0));
        this.setHitBoxPos(hit_part_4, new Vec3d(0, 0.1, -0.75));
        this.setHitBoxPos(hit_part_5, new Vec3d(0, 0.1, 0.75));


        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

    protected boolean damageViable = false;

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    if(!this.damageViable && !source.isUnblockable()) {
        return false;
    }

    damageViable = false;
        return super.attackEntityFrom(source, amount);
    }




    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {

        if(!this.isCloseState() && this.isIAmBoss() && !this.isDeathState()) {
            if(part == this.hit_part_1 || part == this.hit_part_2 || part == this.hit_part_3 || part == this.hit_part_4 || part == this.hit_part_5) {
                damageViable = true;
                return this.attackEntityFrom(source, damage);
            }

            if (damage > 0.0F && !source.isUnblockable()) {
                if (!source.isProjectile()) {
                    Entity entity = source.getImmediateSource();

                    if (entity instanceof EntityLivingBase) {
                        this.blockUsingShield((EntityLivingBase) entity);
                    }

                }
                this.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0f, 0.6f + ModRand.getFloat(0.2f));
                return false;
            }
            return false;
        }
    return false;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }
}
