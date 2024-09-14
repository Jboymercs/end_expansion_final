package com.example.structure.items.tools;


import com.example.structure.config.ItemConfig;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ToolRedSword extends ToolSword implements ISweepAttackOverride{

    private String info_loc;

    public ToolRedSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        this.setMaxDamage(700);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void doSweepAttack(EntityPlayer player, EntityLivingBase target) {
        ModUtils.doSweepAttack(player, target, (e) -> {
            e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, ItemConfig.unholy_sword_potion * 20, 1));
        });

        if (target != null) {
            target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, ItemConfig.unholy_sword_potion * 20, 1));

        }
    }


}
