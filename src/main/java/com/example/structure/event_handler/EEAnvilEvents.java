package com.example.structure.event_handler;

import com.example.structure.init.ModItems;
import com.example.structure.util.ModReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EEAnvilEvents {

    @SubscribeEvent
    public static void addGoopAnvilRecipes(AnvilUpdateEvent event) {

        ItemStack leftInput = event.getLeft();
        ItemStack rightInput = event.getRight();
        ItemStack output = event.getOutput();
        if(rightInput.getItem() == ModItems.ULTRA_GOOP) {
            Item[] endExpansionTools = new Item[]{ModItems.BOSS_SWORD, ModItems.KNIGHT_SWORD, ModItems.ENDFALL_SWORD, ModItems.AMBER_SWORD,
                    ModItems.AMBER_DAGGER, ModItems.AMBER_AXE, ModItems.UNHOLY_AXE, ModItems.PURE_AXE, ModItems.AMBER_PICKAXE, ModItems.INFUSED_PICKAXE,
                    ModItems.GUN_LAUNCHER, ModItems.ENDFALL_STAFF, ModItems.ENDFALL_BOW, ModItems.DURABLE_SHIELD,
                    ModItems.DARK_HELMET, ModItems.DARK_CHESTPLATE, ModItems.LIDOPED_HELMET,
                    ModItems.AMBER_HELMET, ModItems.AMBER_CHESTPLATE, ModItems.AMBER_LEGGINGS, ModItems.AMBER_BOOTS,
                    ModItems.ENDFALL_HELMET, ModItems.ENDFALL_CHESTPLATE, ModItems.ENDFALL_LEGGINGS, ModItems.ENDFALL_BOOTS};

            for (int i = 0; i < endExpansionTools.length; i++) {
                if (leftInput.getItem() == endExpansionTools[i]) {
                    Item[] outputItem = new Item[]{ModItems.BOSS_SWORD, ModItems.KNIGHT_SWORD, ModItems.ENDFALL_SWORD, ModItems.AMBER_SWORD,
                            ModItems.AMBER_DAGGER, ModItems.AMBER_AXE, ModItems.UNHOLY_AXE, ModItems.PURE_AXE, ModItems.AMBER_PICKAXE, ModItems.INFUSED_PICKAXE,
                            ModItems.GUN_LAUNCHER, ModItems.ENDFALL_STAFF, ModItems.ENDFALL_BOW, ModItems.DURABLE_SHIELD,
                            ModItems.DARK_HELMET, ModItems.DARK_CHESTPLATE, ModItems.LIDOPED_HELMET,
                            ModItems.AMBER_HELMET, ModItems.AMBER_CHESTPLATE, ModItems.AMBER_LEGGINGS, ModItems.AMBER_BOOTS,
                            ModItems.ENDFALL_HELMET, ModItems.ENDFALL_CHESTPLATE, ModItems.ENDFALL_LEGGINGS, ModItems.ENDFALL_BOOTS};
                    output = new ItemStack(outputItem[i]);
                    NBTTagCompound tags = leftInput.getTagCompound();
                    output.setTagCompound(tags);
                    output.setItemDamage(0);
                    event.setOutput(output);
                    event.setMaterialCost(1);
                    event.setCost(10);
                }
            }
        }

    }
}
