package com.example.structure.event_handler;

import com.example.structure.init.ModItems;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EEAnvilEvents {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("a3578781-e4a8-4d70-9d32-cd952aeae1df"),
            UUID.fromString("e2d1f056-f539-48c7-b353-30d7a367ebd0"), UUID.fromString("db13047a-bb47-4621-a025-65ed22ce461a"),
            UUID.fromString("abb5df20-361d-420a-8ec7-4bdba33378eb")};

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

        if(rightInput.getItem() == ModItems.BLOODWEED_REINFORCEMENT) {
            if(leftInput.getItem() == ModItems.AMBER_SWORD) {
                int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
                int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), itemDamage);
                NBTTagCompound tags = leftInput.getTagCompound();
                output = new ItemStack(ModItems.REINFORCED_AMBER_SWORD);
                output.setItemDamage(calculatedDamage);
                output.setTagCompound(tags);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(15);
            }
            if(leftInput.getItem() == ModItems.AMBER_DAGGER) {
                int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
                int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), itemDamage);
                NBTTagCompound tags = leftInput.getTagCompound();
                output = new ItemStack(ModItems.REINFORCED_AMBER_DAGGER);
                output.setItemDamage(calculatedDamage);
                output.setTagCompound(tags);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(15);
            }
            if(leftInput.getItem() == ModItems.KNIGHT_SWORD) {
                int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
                int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), itemDamage);
                NBTTagCompound tags = leftInput.getTagCompound();
                output = new ItemStack(ModItems.REINFORCED_KNIGHT_SWORD);
                output.setItemDamage(calculatedDamage);
                output.setTagCompound(tags);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(15);
            }
            if(leftInput.getItem() == ModItems.UNHOLY_AXE) {
                int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
                int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), itemDamage);
                NBTTagCompound tags = leftInput.getTagCompound();
                output = new ItemStack(ModItems.REINFORCED_UNHOLY_AXE);
                output.setItemDamage(calculatedDamage);
                output.setTagCompound(tags);
                event.setOutput(output);
                event.setMaterialCost(2);
                event.setCost(15);
            }
        }

    }
}
