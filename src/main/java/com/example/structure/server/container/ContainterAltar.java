package com.example.structure.server.container;

import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.server.container.slot.SlotOutput;
import com.example.structure.server.container.slot.SlotRestriction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import software.bernie.example.registry.BlockRegistry;

public class ContainterAltar extends Container {

    private static class SlotAltarOutput extends SlotOutput {

        public SlotAltarOutput(IInventory inventory, int index, int xPos, int yPos, Container container) {
            super(inventory, index, xPos, yPos, container);
        }

        @Override
        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
            if(thePlayer instanceof EntityPlayerMP) {
               //Play Some Advancement here if you want too
            }
            return super.onTake(thePlayer, stack);
        }
    }
    private final EntityPlayer player;

    private final TileEntityAltar tile;

    public ContainterAltar(EntityPlayer player, TileEntityAltar altar) {
        InventoryPlayer playerInventory = player.inventory;

        this.player = player;
        this.tile = altar;
        //Input
        addSlotToContainer(new SlotRestriction(tile, 0, 44, 70, 1));
        addSlotToContainer(new SlotRestriction(tile, 1, 80, 34, 1));
        addSlotToContainer(new SlotRestriction(tile, 2, 116, 70, 1));

        //Output
        addSlotToContainer(new SlotAltarOutput(tile, 3, 80, 70, this));

        for (int l = 0; l < 3; ++l)
            for (int j1 = 0; j1 < 9; ++j1)
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 102 + l * 18));

        for (int i1 = 0; i1 < 9; ++i1)
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 160));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (slotIndex > 4) {
                if (!this.mergeItemStack(stack1, 1, 4, false))
                    return ItemStack.EMPTY;

            } else if (!mergeItemStack(stack1, 7, inventorySlots.size(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;        }

        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        BlockPos pos = this.tile.getPos();
        if(playerIn.world.getTileEntity(pos) != this.tile) {
            return false;
        } else {
            return playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
        }
    }
}
