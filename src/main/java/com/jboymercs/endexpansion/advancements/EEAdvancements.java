package com.jboymercs.endexpansion.advancements;

import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;

public class EEAdvancements {

    public static DefeatMobTrigger DEFEAT_LAMENTOR;


    public static void Initialization() {
        DEFEAT_LAMENTOR = CriteriaTriggers.register(new DefeatMobTrigger(new ResourceLocation(ModReference.MOD_ID, "defeat_lamentor")));
    }

}
