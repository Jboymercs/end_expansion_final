package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ActionAOESimple implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {

        ModUtils.circleCallback(2, 5, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(3, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        actor.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f);

        actor.addEvent(()-> ModUtils.circleCallback(4, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 5);
        actor.addEvent(()-> actor.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 5);

        actor.addEvent(()-> ModUtils.circleCallback(5, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 10);
        actor.addEvent(()-> actor.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 10);

        actor.addEvent(()-> ModUtils.circleCallback(6, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 15);
        actor.addEvent(()-> actor.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 15);

        actor.addEvent(()-> ModUtils.circleCallback(7, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 20);
        actor.addEvent(()-> actor.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 20);
    }
}
