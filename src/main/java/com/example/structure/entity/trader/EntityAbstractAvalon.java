package com.example.structure.entity.trader;

import com.example.structure.entity.EntityModBase;
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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.SoundEvents;
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

    private static final DataParameter<Boolean> OPEN = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLOSE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OPEN_STATE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> I_AM_BOSS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLOSE_STATE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_AOE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_LAZERS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SMASH_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.FLOAT);
    public boolean IAmAggroed = false;

    public void setOpen(boolean value) {this.dataManager.set(OPEN, Boolean.valueOf(value));}
    public boolean isOpen() {return this.dataManager.get(OPEN);}
    public void setClose(boolean value) {this.dataManager.set(CLOSE, Boolean.valueOf(value));}
    public boolean isClose() {return this.dataManager.get(CLOSE);}
    public void setOpenState(boolean value) {this.dataManager.set(OPEN_STATE, Boolean.valueOf(value));}
    public boolean isOpenState() {return this.dataManager.get(OPEN_STATE);}
    public void setIAmBoss(boolean value) {this.dataManager.set(I_AM_BOSS, Boolean.valueOf(value));}
    public boolean isIAmBoss() {return this.dataManager.get(I_AM_BOSS);}

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}

    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setCloseState(boolean value) {this.dataManager.set(CLOSE_STATE, Boolean.valueOf(value));}
    public boolean isCloseState() {return this.dataManager.get(CLOSE_STATE);}
    public void setCastAOE(boolean value) {this.dataManager.set(CAST_AOE, Boolean.valueOf(value));}
    public boolean isCastAOE() {return this.dataManager.get(CAST_AOE);}
    public void setCastLazers(boolean value) {this.dataManager.set(CAST_LAZERS, Boolean.valueOf(value));}
    public boolean isCastLazers() {return this.dataManager.get(CAST_LAZERS);}
    public void setSmashAttack(boolean value) {this.dataManager.set(SMASH_ATTACK, Boolean.valueOf(value));}
    public boolean isSmashAttack() {return this.dataManager.get(SMASH_ATTACK);}

    protected boolean hasLazerMinions = false;


    public EntityAbstractAvalon(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.setSize(3F, 3F);
        this.iAmBossMob = true;
        this.hitboxParts = new MultiPartEntityPart[]{model, hit_part_1, shield_1, shield_2, shield_3, shield_4, hit_part_2, hit_part_3, hit_part_4, hit_part_5};
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
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(CAST_LAZERS, Boolean.valueOf(false));
        this.dataManager.register(CAST_AOE, Boolean.valueOf(false));
        this.dataManager.register(SMASH_ATTACK, Boolean.valueOf(false));
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
    public double stateLine = 0.75;

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<EntityMiniValon> nearbySwords = this.world.getEntitiesWithinAABB(EntityMiniValon.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());

        if(!nearbySwords.isEmpty()) {
            this.hasLazerMinions = true;
        } else {
            this.hasLazerMinions = false;
        }


        double currentHealth = this.getHealth() / this.getMaxHealth();

        //Initiates Blocks at 75% Health to heal the Entity
        if(currentHealth < 0.75 && !hasLaunchedBlocksOne) {
            for(int i = 0; i < 4; i++) {
                AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
                if(setTooPos != null) {
                    world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                    this.hasLaunchedBlocksOne = true;
                }
            }
        }
       else if(hasLaunchedBlocksOne) {
            AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
            BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
            if(setTooPos == null) {
            stateLine = 0.5;
            }
        }

       if(currentHealth < 0.5 && !hasLaunchedBlocksTwo) {
           for(int i = 0; i < 8; i++) {
               AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
               BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
               if(setTooPos != null) {
                   world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                   this.hasLaunchedBlocksTwo = true;
               }
           }
       }        else if(hasLaunchedBlocksTwo) {
           AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
           BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
           if(setTooPos == null) {
               stateLine = 0.25;
           }
       }

       if(currentHealth < 0.25 && !hasLaunchedBlocksThree) {
           for(int i = 0; i < 12; i++) {
               AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
               BlockPos setTooPos = ModUtils.avalonSearchForBlocks(box, world, this, ModBlocks.EYED_OBSIDIEAN.getDefaultState());
               if(setTooPos != null) {
                   world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
                   this.hasLaunchedBlocksThree = true;
               }
           }
       }        else if(hasLaunchedBlocksThree) {
           AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
           BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.OBSIDIAN_HEALTH_BLOCK.getDefaultState());
           if(setTooPos == null) {
               stateLine = 0;
           }
       }



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

        if(!this.isCloseState() && this.isIAmBoss()) {
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
