package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ActionEncirclingAOE implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionVector();

    }
}
