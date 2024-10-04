package com.example.structure.event_handler;


import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.init.ModItems;
import com.example.structure.items.ItemBucketPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class BucketLidopedEvent {

    @SubscribeEvent
    public static void RightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        EntityPlayer player = event.getEntityPlayer();

        Vec3d lookVector = player.getLook(1.0F);
        double playerReach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d eyePosition = player.getPositionEyes(1.0F);
        Vec3d traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);


        RayTraceResult rtresult = player.getEntityWorld().rayTraceBlocks(eyePosition, traceEnd, false, true, false);;

        if (rtresult == null) return;

        if (rtresult.typeOfHit == RayTraceResult.Type.BLOCK) {
            World world = player.getEntityWorld();

            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(rtresult.getBlockPos()).grow(0.01D));
            if (list.isEmpty()) return;

            for (Entity entity : list)
            {
                ResourceLocation entityId = EntityList.getKey(entity);
                if (entity instanceof EntityLidoped && entityId != null)
                {
                    AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
                    RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(eyePosition, traceEnd);

                    if (raytraceresult1 != null)
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onRightclickEntity(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();

        if ((stack.getItem() == Items.BUCKET && event.getTarget() != null && event.getTarget() instanceof EntityLidoped && !(event.getTarget() instanceof EntityPlayer)))
        {
            EntityLidoped entity = (EntityLidoped) event.getTarget();

            if (!entity.isDead)
            {
                player.swingArm(EnumHand.MAIN_HAND);

                event.setCanceled(true);

                if (!event.getWorld().isRemote)
                {
                    if (!player.capabilities.isCreativeMode)
                    {
                        stack.setCount(stack.getCount() - 1);
                    }

                    ItemStack newStack = new ItemStack(ModItems.LIDOPED_BUCKET);

                    ItemBucketPlacer.recordEntityNBT(newStack, player, entity);

                    if (entity.hasCustomName())
                    {
                        newStack.setStackDisplayName(entity.getCustomNameTag());
                    }

                    if (stack.isEmpty())
                    {
                        player.setHeldItem(EnumHand.MAIN_HAND, newStack);
                    }
                    else if (!player.inventory.addItemStackToInventory(newStack))
                    {
                        player.dropItem(newStack, false);
                    }
                    // Lazy solution to the game trying to instantly use the spawn bucket when given to the survival player
                    player.getCooldownTracker().setCooldown(newStack.getItem(), 1);

                    event.getTarget().setDead();
                }
            }
        }
    }
}
