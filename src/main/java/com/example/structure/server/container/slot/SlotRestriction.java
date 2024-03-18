package com.example.structure.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRestriction extends Slot {
    int maxItems;

    public SlotRestriction(IInventory inventory, int slotIndex, int x, int y, int maxItems) {
        super(inventory, slotIndex, x, y);
        this.maxItems = maxItems;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return true;
    }
    @Override
    public int getSlotStackLimit()
    {
        return maxItems;
    }
}
