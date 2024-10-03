package com.example.structure.entity.ai;

import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class EntityAILidopedHome<T extends EntityLidoped> extends EntityAIBase {

    private final T entity;

    private final double moveSpeedAmp;

    private float lookSpeed;

    private int harvestTime = 900 + ModRand.range(100, 500);

    private int sleepTime = 3600;

    private int wanderTime = 3200;

    private int idleTime = 100 + ModRand.range(20, 100);
    private boolean setTooSleep = false;
    private boolean setTooWander = false;


    public EntityAILidopedHome(T entity, double moveSpeedAmp) {
        this(entity, moveSpeedAmp, 30.0F);
    }

    public EntityAILidopedHome(T entity, double moveSpeedAmp, float lookSpeed) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAmp;
        this.lookSpeed = lookSpeed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return entity.getAttackTarget() == null && this.entity.isHasHome();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute());
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }

    private boolean hasDoneAction = false;

    private boolean wanderForResource = false;
    private BlockPos homePos;
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();
        if(target != null) {
            return;
        }

        homePos = this.entity.getHomePos();

        if(this.setTooSleep) {
            //quick action to set to sleep
            if(!hasDoneAction) {
                this.setSleep();
            }
            if(sleepTime < 0) {
                //end sleep
                this.EndSleep();

            } else {
                sleepTime--;
            }
        } else if (this.setTooWander) {
            //wander timer
            if(wanderTime < 0) {
                this.setTooWander = false;
                this.entity.getNavigator().clearPath();
                this.idleTime = 100;
                this.wanderTime = 3200;
            } else {
                wanderTime--;
            }

            Vec3d Pos = this.getPosition();

            if(Pos == null) {
                return;
            }

            if(idleTime < 0) {

                if (this.entity.getNavigator().noPath() && !this.hasDoneAction) {
                    this.entity.getNavigator().tryMoveToXYZ(Pos.x, Pos.y, Pos.z, moveSpeedAmp - 0.3);
                    this.hasDoneAction = true;
                } else if (this.entity.getNavigator().noPath() && this.hasDoneAction) {
                    this.idleTime = 100 + ModRand.range(20, 100);
                    this.hasDoneAction = false;
                }
            } else {
                idleTime--;
            }

            //do occasional wandering

        } else if (this.wanderForResource) {
            //wander timer
            if(wanderTime < 0) {
                this.wanderForResource = false;
                this.entity.getNavigator().clearPath();
                this.idleTime = 100;
                this.wanderTime = 3200;
            } else {
                wanderTime--;
            }

            Vec3d Pos = this.getPosition();

            if(Pos == null) {
                return;
            }

            if(idleTime < 0) {

                if (this.entity.getNavigator().noPath() && !this.hasDoneAction) {
                    this.entity.getNavigator().tryMoveToXYZ(Pos.x, Pos.y, Pos.z, moveSpeedAmp - 0.3);
                    this.hasDoneAction = true;
                } else if (this.entity.getNavigator().noPath() && this.hasDoneAction) {
                    this.idleTime = 100 + ModRand.range(20, 100);
                    this.hasDoneAction = false;
                }
            } else {
                idleTime--;
            }

            //do occasional wandering

        }


        if(this.entity.world.getBlockState(homePos).getBlock() != ModBlocks.BARE_BARK_HOLE && this.entity.world.getBlockState(homePos).getBlock() != ModBlocks.BARE_BARK_HOLE_FILLED) {
            //if the current home is broken or doesn't match, find a new one
            this.entity.setHasHome(false);
        } else if (!this.entity.isHarvest() && !this.entity.isHasHarvestedItem() && !this.setTooWander && !this.setTooSleep){
            //now search for blood weed while wandering
            if (this.entity.world.rand.nextInt(10) == 0 || selectedBlockPos != null) {
                searchForResource();
            } else {
                this.wanderForResource = true;
                this.wanderTime = 1000;
            }
        } else if (this.entity.isHasHarvestedItem()) {
            //Head back to home with item
            takeResourceHome(homePos);
        }


        if(this.entity.isHarvest()) {
            this.entity.setImmovable(true);

            if(harvestTime < 0) {
                if(isAtBlockLoc) {
                    this.endHarvest();
                }
            } else {
                harvestTime--;
            }
        }


    }


    private BlockPos selectedBlockPos;
    private boolean isAtBlockLoc = false;

    private int createNewHomeCheck = 600;

    public void takeResourceHome(BlockPos homePos) {
        if(homePos != null) {
            this.entity.setImmovable(false);
            this.entity.getNavigator().tryMoveToXYZ(homePos.getX(), homePos.getY(), homePos.getZ(), moveSpeedAmp);
            this.entity.getLookHelper().setLookPosition(homePos.getX(), homePos.getY(), homePos.getZ(), this.lookSpeed, this.lookSpeed);
            double distanceTOO = this.entity.getDistanceSq(homePos);

            if(distanceTOO <= 4 && this.entity.isHasHarvestedItem()) {
                //we're doing this incase our little bug like creature can't get to there home
                if(createNewHomeCheck < 0) {
                    AxisAlignedBB box = this.entity.getEntityBoundingBox().grow(2, 1, 2);
                    BlockPos searchForNewHome = ModUtils.searchForBlocks(box, this.entity.world, this.entity, ModBlocks.BARE_BARK.getDefaultState());
                    if(searchForNewHome != null) {
                        this.entity.setHomePos(searchForNewHome);
                        this.entity.world.setBlockState(searchForNewHome, ModBlocks.BARE_BARK_HOLE.getDefaultState());
                        createNewHomeCheck = 1200;
                        return;
                    }
                } else {
                    createNewHomeCheck--;
                }
            }
            if(distanceTOO <= 2) {
                this.entity.getNavigator().clearPath();
                this.entity.setHasHarvestedItem(false);
                createNewHomeCheck = 600;
                //deposit item into bark hole

                this.entity.equipBugBackItem(EntityLidoped.LIDOPED_BACK.BACK, new ItemStack(ModItems.INVISIBLE));
                this.entity.world.setBlockState(homePos, ModBlocks.BARE_BARK_HOLE_FILLED.getDefaultState());
                if(this.entity.world.rand.nextInt(3) == 0) {
                    this.setTooSleep =true;
                } else {
                    this.entity.getNavigator().clearPath();
                    this.setTooWander = true;
                }
            }
        }
    }


    public void setSleep() {
        this.entity.setOnSleep(true);
        this.hasDoneAction = true;
    }

    public void EndSleep() {
        this.entity.setOnSleep(false);
        sleepTime = 3600;
        this.hasDoneAction = false;
        this.entity.lockLook = false;
        this.entity.setImmovable(false);
        this.setTooSleep = false;
    }

    public void searchForResource() {
        AxisAlignedBB box = this.entity.getEntityBoundingBox().grow(4, 2, 4);
        BlockPos searchForBloodWeed = ModUtils.searchForBlocks(box, this.entity.world, this.entity, ModBlocks.BARE_GRASS.getDefaultState());
        if(selectedBlockPos == null && searchForBloodWeed != null) {
            selectedBlockPos = searchForBloodWeed;
            this.entity.getNavigator().clearPath();
        }

        if(selectedBlockPos != null) {
            this.wanderTime = 3200;
            this.idleTime = 100;
            this.hasDoneAction = false;
            this.wanderForResource = false;
            this.entity.getNavigator().tryMoveToXYZ(selectedBlockPos.getX(), selectedBlockPos.getY(), selectedBlockPos.getZ(), moveSpeedAmp);
            this.entity.getLookHelper().setLookPosition(selectedBlockPos.getX(), selectedBlockPos.getY(), selectedBlockPos.getZ(), this.lookSpeed, this.lookSpeed);
            double distance = this.entity.getDistanceSq(selectedBlockPos);

            if(distance < 1 && !isAtBlockLoc) {
                //begin harvesting
                this.doHarvest();
            }
        }
    }


    @Nullable
    protected Vec3d getPosition()
    {
        return RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
    }

    private void doHarvest() {
        this.entity.setStartHarvest(true);
        this.isAtBlockLoc = true;
        this.entity.getNavigator().clearPath();
        this.harvestTime = 900 + ModRand.range(100, 600);

        this.entity.addEvent(()-> {
            this.entity.setStartHarvest(false);
            this.entity.setHarvest(true);
        }, 10);
    }

    private void endHarvest() {
        this.entity.setHarvest(false);
        this.entity.setEndHarvest(true);
        this.entity.setImmovable(false);
        this.entity.setHasHarvestedItem(true);
        this.isAtBlockLoc =false;

        this.entity.addEvent(()-> {
            this.entity.setEndHarvest(false);
            this.selectedBlockPos = null;
            this.entity.equipBugBackItem(EntityLidoped.LIDOPED_BACK.BACK, new ItemStack(ModItems.BLOODWEED_REFINED));
            //set so that it has an item
        }, 10);
    }
}
