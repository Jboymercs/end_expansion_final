package com.jboymercs.endexpansion.init;

import com.jboymercs.endexpansion.blocks.fluid.BareAcid;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluid {
    public static final BareAcid ACID = (BareAcid) new BareAcid("bare_acid",
            new ResourceLocation(ModReference.MOD_ID, "blocks/bare_acid_still"),
            new ResourceLocation(ModReference.MOD_ID, "blocks/bare_acid_flow"))
            .setHasBucket(true)
            .setDensity(1100)
            .setGaseous(false)
            .setLuminosity(10)
            .setViscosity(900)
            .setTemperature(300)
            .setUnlocalizedName(ModReference.MOD_ID + ".bare_acid");

    public static void registerFluids()
    {
        FluidRegistry.registerFluid(ACID);
        FluidRegistry.addBucketForFluid(ACID);
//        if (ACID.isBucketEnabled())
//        {
//            FluidRegistry.addBucketForFluid(ACID);
//        }
    }
}
