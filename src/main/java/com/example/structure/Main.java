package com.example.structure;

import com.example.structure.advancements.EEAdvancements;
import com.example.structure.config.ModConfig;
import com.example.structure.init.ModDimensions;
import com.example.structure.init.ModEntities;
import com.example.structure.init.ModProfressions;
import com.example.structure.init.ModRecipes;
import com.example.structure.items.gecko.AmberArmorSet;
import com.example.structure.proxy.CommonProxy;
import com.example.structure.renderer.armor.AmberArmorRenderer;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.util.handlers.FogHandler;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.StructureHandler;
import com.example.structure.world.Biome.WorldProviderEndEE;
import com.example.structure.world.WorldGenCustomStructure;
import com.example.structure.world.api.structures.MapGenKingFortress;
import git.jbredwards.nether_api.mod.common.compat.stygian_end.StygianEndHandler;
import git.jbredwards.nether_api.mod.common.world.WorldProviderNether;
import git.jbredwards.nether_api.mod.common.world.WorldProviderTheEnd;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import javax.annotation.Nonnull;


@Mod(modid = ModReference.MOD_ID, name = ModReference.NAME, version = ModReference.VERSION)
public class Main {

    /**
     * I'd like to give a huge thank you to Barribob, for the spectacular work done on Maelstrom and this project
     * exsisting cause of it. Credit to a lot of the code modified from Maelstrom source. Yes, I am revamping that mod but
     * Barribob still deserves the credit.
     *
     * Credits also go to - UnOriginal for the structure method used in this project, and a few other functions used
     *
     * FakeDrayn for sound design, and animation work
     *
     * Lastly, this mod is for the ModJam 2023 Summer for the 1.12.2 Modded Coalition Discord Server
     * link - https://discord.gg/Hmvek4Axrv
     */

    @SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;

    public static final Logger LOGGER = LogManager.getLogger(ModReference.MOD_ID);

    @Mod.Instance
    public static Main instance;

    private static Logger logger;

    public Main() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GeckoLib.initialize();
        logger = event.getModLog();
        //Sky Box Registry
        ModDimensions.registerDimensionChanges();

        //Advancements
        EEAdvancements.Initialization();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        //Register Entities
        ModEntities.registerEntities();
        ModEntities.RegisterEntitySpawns();
        //Register World Gen
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructure(), 3);
        //Register Fog
        handleClientFog();
        proxy.init();
    }

    // Register dimension overrides
    @Mod.EventHandler
    static void serverAboutToStart(@Nonnull final FMLServerAboutToStartEvent event) {
        if(ModConfig.isSkyBoxEnalbed) {
            DimensionManager.unregisterDimension(1);
            DimensionType END = DimensionType.register("End", "_end", 1, WorldProviderEndEE.class, false);
            DimensionManager.registerDimension(1, END);
        }
    }


    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void registerRenderers(FMLPreInitializationEvent event) {
        //Specific for blocks/Armor handling geckolib data
        //GeoArmorRenderer.registerArmorRenderer(AmberArmorSet.class, new AmberArmorRenderer());

    }

    public MapGenStructure fortress = new MapGenKingFortress(ModConfig.fortress_spacing, 0, ModConfig.fortress_odds);


    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

        BiomeRegister.registerBiomes();
        StructureHandler.handleStructureRegistries();
        ModSoundHandler.registerSounds();
        ModRecipes.init();
        ModProfressions.associateCareersAndTrades();

        if(ModConfig.isSkyBoxEnalbed) {
            //Sky Stuff
            proxy.registerEventHandlers();
        }
    }


    public static void addSkyBoxTooOverride() {
        DimensionManager.unregisterDimension(1);
        DimensionType END = DimensionType.register("End", "_end", 1, WorldProviderEndEE.class, false);
        System.out.println("Registering Sky Box in Main");
        DimensionManager.registerDimension(1, END);
    }


    public static ResourceLocation locate(String location)
    {
        return new ResourceLocation(ModReference.MOD_ID, location);
    }



    public static void handleClientFog() {
        MinecraftForge.EVENT_BUS.register(new FogHandler());
    }
}
