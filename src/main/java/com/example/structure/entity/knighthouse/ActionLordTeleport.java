package com.example.structure.entity.knighthouse;

import com.example.structure.Main;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.packets.MessageModParticles;
import com.example.structure.util.EnumModParticles;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionLordTeleport implements IAction {

    Vec3d teleportColor;
    public ActionLordTeleport(Vec3d teleportColor) {
        this.teleportColor = teleportColor;
    }
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        for(int i = 0; i < 50; i++) {
            Vec3d pos = ModRand.randVec().normalize().scale(6)
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
