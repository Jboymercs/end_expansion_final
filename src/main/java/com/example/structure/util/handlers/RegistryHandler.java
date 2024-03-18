package com.example.structure.util.handlers;


import com.example.structure.blocks.atlar.BlockAltar;
import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.items.CrystalBallItem;
import com.example.structure.items.Items;
import com.example.structure.renderer.*;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.geckolib3.GeckoLib;


@Mod.EventBusSubscriber
public class RegistryHandler {

    private static IForgeRegistry<Item> itemRegistry;
    private static IForgeRegistry<Block> blockRegistry;


    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        itemRegistry = event.getRegistry();
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
       // Items.ALTAR = registerItem(new ItemBlock(Items.ALTAR_BLOCK), "altar");
    }


    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
       event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
       // blockRegistry = event.getRegistry();
       // Items.ALTAR_BLOCK = new BlockAltar();
       // Items.ALTAR_BLOCK.setCreativeTab(ModCreativeTabs.ITEMS);
      //  registerGeckoBlock(Items.ALTAR_BLOCK, "altar");

    }


    public static void registerGeckoBlock(Block block, String name) {
        registerGeckoBlock(block, new ResourceLocation(ModReference.MOD_ID, name));
    }

    public static void registerGeckoBlock(Block block, ResourceLocation name) {
        blockRegistry.register(block.setRegistryName(name).setTranslationKey(name.toString().replace(":", ".")));
    }

    public static <T extends Item> T registerItem(T item, String name) {
        registerItem(item, new ResourceLocation(ModReference.MOD_ID, name));
        return item;
    }

    public static <T extends Item> T registerItem(T item, ResourceLocation name) {
        itemRegistry.register(item.setRegistryName(name).  setTranslationKey(name.toString().replace(":", ".")));
        return item;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegister(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ModItems.CRYSTAL_BALL, 0, new ModelResourceLocation(ModReference.MOD_ID + ":crystalball","inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.BOSS_SWORD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":sword", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.SPIN_SWORD_ITEM, 0, new ModelResourceLocation(ModReference.MOD_ID + ":spinsword", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.ENDFALL_SWORD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":endfall_sword", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.DURABLE_SHIELD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":durable_shield", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.ENDFALL_STAFF, 0, new ModelResourceLocation(ModReference.MOD_ID + ":efstaff", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.PROJECTILE_PURPLE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":projpurp", "inventory"));
       //ModelLoader.setCustomModelResourceLocation(Items.ALTAR, 0, new ModelResourceLocation(ModReference.MOD_ID + ":altar", "inventory"));

        ModItems.CRYSTAL_BALL.setTileEntityItemStackRenderer(new RenderCrystalBall());
        ModItems.BOSS_SWORD.setTileEntityItemStackRenderer(new RenderBossSword());
        ModItems.SPIN_SWORD_ITEM.setTileEntityItemStackRenderer(new RenderSpinSword());
        ModItems.ENDFALL_SWORD.setTileEntityItemStackRenderer(new RenderEndFallSword());
        ModItems.DURABLE_SHIELD.setTileEntityItemStackRenderer(new RenderDurableShield());
        ModItems.ENDFALL_STAFF.setTileEntityItemStackRenderer(new RenderEndfallStaff());
        ModItems.PROJECTILE_PURPLE.setTileEntityItemStackRenderer(new RenderPurple());

        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
        for (Block block : ModBlocks.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

    }
}
