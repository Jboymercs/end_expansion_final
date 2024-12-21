package com.example.structure.entity;

import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityExplosion extends Projectile{

    public EntityExplosion(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, 0);
    }

    protected int boss;
    @Override
    public Item getItemToRender() {
        return ModItems.INVISIBLE;
    }

    public EntityExplosion(World worldIn) {
        super(worldIn);
    }

    public EntityExplosion(World worldIn, double x, double y, double z, ItemStack stack, int bossId) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.boss = bossId;
    }



    @Override
    public void onUpdate() {
        super.onUpdate();
        if(ticksExisted == 20 && !world.isRemote) {
            if(boss == 1) {
                new EntityCrystalKnight(world).onSummon(this.getPosition(), this);
            }
            else if(boss == 2) {
                new EntityEndKing(world).onSummon(this.getPosition(), this);
            } else {
                this.setDead();
            }

        }

        if(ticksExisted > 40) {
            this.setDead();

        }
    }
}
