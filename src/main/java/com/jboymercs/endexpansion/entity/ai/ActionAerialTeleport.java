package com.jboymercs.endexpansion.entity.ai;

import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.entity.EntityModBase;
import com.jboymercs.endexpansion.packets.MessageModParticles;
import com.jboymercs.endexpansion.util.EnumModParticles;
import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionAerialTeleport implements IAction{
    Vec3d teleportColor;

    public ActionAerialTeleport(Vec3d teleportColor) {
        this.teleportColor = teleportColor;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        for(int i = 0; i < 50; i++) {
            Vec3d pos = ModRand.randVec().normalize().scale(12)
                    .add(target.getPositionVector());

            boolean canSee = actor.world.rayTraceBlocks(target.getPositionEyes(1), pos, false, true, false) == null;
            Vec3d prevPos = actor.getPositionVector();
            if(canSee && ModUtils.attemptTeleport(pos, actor)){
                ModUtils.lineCallback(prevPos, pos, 50, (particlePos, j) ->
                        Main.network.sendToAllTracking(new MessageModParticles(EnumModParticles.EFFECT, particlePos, Vec3d.ZERO, teleportColor), actor));
                actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE);
                break;
            }
        }
    }
}
