package com.example.structure.entity.ai;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.EntityWall;
import com.example.structure.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionDrawWalls implements IAction{

    int randomWallSelection = ModRand.range(1, 3);
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionVector();
        if(!actor.world.isRemote) {
            if (randomWallSelection == 1) {
                Vec3d newPos = targetPos.add(negativeOrPositive(), 0, negativeOrPositive());
                EntityWall wall = new EntityWall(actor.world);
                wall.setPosition(newPos.x, newPos.y, newPos.z);
                actor.world.spawnEntity(wall);
                EntityWall wall2 = new EntityWall(actor.world);
                wall2.setPosition(newPos.x - 1, newPos.y, newPos.z);
                actor.world.spawnEntity(wall2);
                EntityWall wall3 = new EntityWall(actor.world);
                wall3.setPosition(newPos.x + 1, newPos.y, newPos.z);
                actor.world.spawnEntity(wall3);
                EntityWall wall4 = new EntityWall(actor.world);
                wall4.setPosition(newPos.x - 2, newPos.y, newPos.z);
                actor.world.spawnEntity(wall4);
                EntityWall wall5 = new EntityWall(actor.world);
                wall5.setPosition(newPos.x - 2, newPos.y, newPos.z);
                actor.world.spawnEntity(wall5);
            }

            if (randomWallSelection == 2) {
                Vec3d newPos = targetPos.add(negativeOrPositive(), 0, negativeOrPositive());
                EntityWall wall = new EntityWall(actor.world);
                wall.setPosition(newPos.x, newPos.y, newPos.z);
                actor.world.spawnEntity(wall);
                EntityWall wall2 = new EntityWall(actor.world);
                wall2.setPosition(newPos.x, newPos.y, newPos.z + 1);
                actor.world.spawnEntity(wall2);
                EntityWall wall3 = new EntityWall(actor.world);
                wall3.setPosition(newPos.x, newPos.y, newPos.z - 1);
                actor.world.spawnEntity(wall3);
                EntityWall wall4 = new EntityWall(actor.world);
                wall4.setPosition(newPos.x, newPos.y, newPos.z - 2);
                actor.world.spawnEntity(wall4);
                EntityWall wall5 = new EntityWall(actor.world);
                wall5.setPosition(newPos.x, newPos.y, newPos.z + 2);
                actor.world.spawnEntity(wall5);
            }
            if (randomWallSelection == 3) {

            }
        }
    }

    public int negativeOrPositive() {
        Random rand = new Random();

        if(rand.nextBoolean()) {
            return ModRand.range(-6, -1);
        }
        return ModRand.range(1, 6);
    }



}
