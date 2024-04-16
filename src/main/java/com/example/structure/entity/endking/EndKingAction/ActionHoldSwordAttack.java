package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.IAction;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;
import java.util.function.Supplier;

public class ActionHoldSwordAttack implements IAction {

    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionHoldSwordAttack(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        //Upper Top Sword
        Function<Vec3d, Runnable> missile = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(60);
            int randomTimer = 100 +  (int) ModRand.getFloat(200);
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < randomTimer; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 5, 0)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());

                    projectile.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, randomTimer);

        };
        //Echeleon 1 Missile
        Function<Vec3d, Runnable> missile2 = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(60);
            int randomTimer = 100 +  (int) ModRand.getFloat(200);
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < randomTimer; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 4, 2)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());



                projectile.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, randomTimer);

        };
        //Echeleon 2 Missile
        Function<Vec3d, Runnable> missile3 = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(60);
            int randomTimer = 100 +  (int) ModRand.getFloat(200);
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < randomTimer; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 4, -2)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());

                projectile.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, randomTimer);

        };
        Function<Vec3d, Runnable> missile4 = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(60);
            int randomTimer = 100 +  (int) ModRand.getFloat(200);
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < randomTimer; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, -3)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());

                projectile.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, randomTimer);

        };
        Function<Vec3d, Runnable> missile5 = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(60);
            int randomTimer = 100 +  (int) ModRand.getFloat(200);
            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < randomTimer; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, 3)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }

            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(projectile.getPositionVector());

                projectile.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);

                ModUtils.addEntityVelocity(actor, vel.normalize());
            }, randomTimer);

        };
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),2);
        actor.addEvent(missile2.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),5);
        actor.addEvent(missile3.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),5);
        actor.addEvent(missile4.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),5);
        actor.addEvent(missile5.apply(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0))),5);
    }
}
