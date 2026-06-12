package com.example.structure.entity.ai;

import com.example.structure.Main;
import com.example.structure.entity.EntityModBase;
import com.example.structure.packets.MessageModParticles;
import com.example.structure.util.EnumModParticles;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionAerialTeleport implements IAction{
    Vec3d teleportColor;
    Vec3d endPoint;

    public ActionAerialTeleport(Vec3d teleportColor, Vec3d endPoint) {
        this.teleportColor = teleportColor;
        this.endPoint = endPoint;
    }

    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        for(int i = 0; i < 50; i++) {

            boolean canSee = actor.world.rayTraceBlocks(target.getPositionEyes(1), endPoint, false, true, false) == null;
            Vec3d prevPos = actor.getPositionVector();
            if(canSee && ModUtils.attemptTeleport(endPoint, actor)){
                ModUtils.lineCallback(prevPos, endPoint, 50, (particlePos, j) ->
                        Main.network.sendToAllTracking(new MessageModParticles(EnumModParticles.EFFECT, particlePos, Vec3d.ZERO, teleportColor), actor));
                actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE);
                break;
            }
        }
    }
}
