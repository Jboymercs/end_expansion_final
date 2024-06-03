package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityFireBall;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;
import java.util.function.Supplier;

public class ActionThrowFireball implements IAction {


    Supplier<EntityFireBall> projectileSupplier;
    float velocity;

    public ActionThrowFireball(Supplier<EntityFireBall> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }


    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Function<Vec3d, Runnable> missile = (offset) -> () -> {
            EntityFireBall projectile = projectileSupplier.get();
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < 25; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 7, 0)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d targetPosRandom = target.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(ModRand.range(-4, 4), 0, ModRand.range(-4, 4))));
                Vec3d vel = targetPosRandom.add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());

                projectile.shoot(vel.x, vel.y, vel.z, 2.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, 20);

        };
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),0);
    }
}
