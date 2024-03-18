package com.example.structure.server.container.slot;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.time.Year;

public class SlotOutput extends Slot {

    private Container container;

    public SlotOutput(IInventory inventory, int index, int xPos, int yPos, Container container) {
        super(inventory, index, xPos, yPos);
        this.container = container;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return false;
    }
}
