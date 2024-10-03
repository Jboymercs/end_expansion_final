package com.example.structure.init;

import com.example.structure.blocks.fluid.BareAcid;
import com.example.structure.util.ModReference;
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
            .setUnlocalizedName("bare_acid");

    public static void registerFluids()
    {
        System.out.println("Registering acid");
        FluidRegistry.registerFluid(ACID);
        FluidRegistry.addBucketForFluid(ACID);
//        if (ACID.isBucketEnabled())
//        {
//            FluidRegistry.addBucketForFluid(ACID);
//        }
    }
}
