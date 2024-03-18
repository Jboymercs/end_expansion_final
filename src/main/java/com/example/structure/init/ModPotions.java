package com.example.structure.init;


import com.example.structure.potion.PotionCorrupted;
import com.example.structure.util.ModReference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class ModPotions{
    private ModPotions(){}

    public static PotionCorrupted CORRUPTED = (PotionCorrupted) new PotionCorrupted(true, 0).setRegistryName(ModReference.MOD_ID, "corrupted");

    public static PotionType redcorruption = new PotionType("potionCorruption", new PotionEffect[]{new PotionEffect(ModPotions.CORRUPTED, 100)}).setRegistryName("poison_corruption");



    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                CORRUPTED
        );
    }
}
