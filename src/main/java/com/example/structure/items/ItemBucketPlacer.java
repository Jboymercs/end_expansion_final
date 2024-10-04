package com.example.structure.items;


import com.example.structure.Main;
import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemBucketPlacer extends ItemMonsterPlacer implements IHasModel {

    public ItemBucketPlacer(String name)
    {

        this.setRegistryName(name);
        this.setTranslationKey(name);
        ModItems.ITEMS.add(this);
        this.setCreativeTab(ModCreativeTabs.ITEMS);

        this.maxStackSize = 1;

        for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
        {
            String thisEntity = entitylist$entityegginfo.spawnedID.toString();

            this.addPropertyOverride(new ResourceLocation(thisEntity), new IItemPropertyGetter()
            {
                @SideOnly(Side.CLIENT)
                public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
                {
                    if (ItemMonsterPlacer.getNamedIdFrom(stack) != null && ItemMonsterPlacer.getNamedIdFrom(stack).toString().contains(thisEntity)) return 1.0F;
                    else return 0.0F;
                }
            });
        }
        this.setCreativeTab(ModCreativeTabs.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    { return EnumActionResult.PASS; }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult rtresult = this.rayTrace(worldIn, playerIn, false);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, rtresult);
        if (ret != null) return ret;

        if (rtresult == null || rtresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            BlockPos blockpos = rtresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            else
            {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockpos1 = flag1 && rtresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(rtresult.sideHit);

                if (!playerIn.canPlayerEdit(blockpos1, rtresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else if (!playerIn.isSneaking() || playerIn.isSneaking())
                {
                    if (playerIn instanceof EntityPlayerMP)
                    { CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack); }

                    if (!worldIn.isRemote)
                    {
                        NBTTagCompound entityTag = (NBTTagCompound) itemstack.getTagCompound().getTag("EntityTag");

                        Entity entity = EntityList.createEntityFromNBT(entityTag, worldIn);
                        if(itemstack.hasDisplayName())
                        {
                            entity.setCustomNameTag(itemstack.getDisplayName());
                        }

                        if (entity instanceof EntityLiving) ((EntityLiving) entity).enablePersistence();
                        entity.setPosition((double)blockpos1.getX() + 0.5, (double)blockpos1.getY(), (double)blockpos1.getZ() + 0.5);
                        worldIn.spawnEntity(entity);
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET)) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
        }
    }

    public boolean tryPlaceWater(@Nullable EntityPlayer player, World worldIn, BlockPos posIn)
    {
        IBlockState iblockstate = worldIn.getBlockState(posIn);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

        if (!worldIn.isAirBlock(posIn) && !flag && !flag1)
        {
            return false;
        }
        else
        {
            if (worldIn.provider.doesWaterVaporize())
            {
                int l = posIn.getX();
                int i = posIn.getY();
                int j = posIn.getZ();
                worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int k = 0; k < 8; ++k)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            }
            else
            {
                if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);
                }

                worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(posIn, Blocks.FLOWING_WATER.getDefaultState(), 11);
            }
            return true;
        }
    }

    public static void recordEntityNBT(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (!player.world.isRemote)
        {
            NBTTagCompound tags = stack.getTagCompound();

            if (tags == null)
            {
                tags = new NBTTagCompound();
                stack.setTagCompound(tags);
            }
            if (!tags.hasKey("EntityTag"))
            {
                NBTTagCompound entityTag = new NBTTagCompound();
                entity.writeToNBTOptional(entityTag);
                entityTag.removeTag("Pos");
                entityTag.removeTag("Motion");
                //entityTag.removeTag("Rotation");
                entityTag.removeTag("Fire");
                entityTag.removeTag("FallDistance");
                entityTag.removeTag("Dimension");
                entityTag.removeTag("PortalCooldown");
                entityTag.removeTag("UUIDMost");
                entityTag.removeTag("UUIDLeast");
                entityTag.removeTag("Leashed");
                entityTag.removeTag("Leash");

                entity.setDead();

                tags.setTag("EntityTag", entityTag);
            }
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values())
            {
                if (entitylist$entityegginfo.spawnedID.toString().equals("ee:lidoped"))
                {
                    ItemStack itemstack = new ItemStack(this, 1);
                    applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                    items.add(itemstack);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
