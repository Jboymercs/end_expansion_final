package com.example.structure.entity.tileentity;

import com.example.structure.blocks.atlar.BlockAltar;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.animation.IAnimatedEntity;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.PriorityQueue;

public class TileEntityAltar extends TileEntity implements IAnimatable, ITickable, IInventory {

    public NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    private final AnimationFactory factory = new AnimationFactory(this);


    public TileEntityAltar() {
        super();
    }

   public boolean beginRitual = false;
    public boolean isDoingRitual = false;
    public int animationPlay = 200;



    @Override
    public void update() {
        if(!world.isRemote) {
            //Infused Crystal Recipe
            if (getStackInSlot(0).getItem() == ModItems.RED_CRYSTAL_ITEM && getStackInSlot(1).getItem() == ModItems.INFUSION_CORE && getStackInSlot(2).getItem() == ModItems.PURPLE_CRYSTAL_ITEM && !beginRitual) {
                this.setBeginRitualInfusedCrystal();
            } else if (this.beginRitual) {
                animationPlay--;
            }

            if(animationPlay == 20 && isDoingRitual) {
                setInventorySlotContents(0, ItemStack.EMPTY);
                setInventorySlotContents(1, ItemStack.EMPTY);
                setInventorySlotContents(2, ItemStack.EMPTY);
                this.update();

            } else if (animationPlay == 10 && isDoingRitual) {

            }
            if(animationPlay == 0) {
                ItemStack stackInfused = new ItemStack(ModItems.INFUSED_CRYSTAL, 1);
                infusedCrystalSummonItem(stackInfused);
                this.update();
                beginRitual = false;
                isDoingRitual = false;
                animationPlay = 220;

            }


        }
    }

    public void setBeginRitualInfusedCrystal() {
        beginRitual = true;
        isDoingRitual = true;

    }

    public void infusedCrystalSummonItem (ItemStack itemStack) {
        this.setInventorySlotContents(3 ,itemStack);
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }


    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : inventory)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }


    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(inventory, index, count);
        if (!itemstack.isEmpty())
            this.markDirty();
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
        this.markDirty();
    }

    @Override
    public String getName() {
        return new TextComponentTranslation("tile.altar.name").getFormattedText();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 2;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return false;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
       inventory.clear();
    }

    public NBTTagCompound saveToNbt(NBTTagCompound nbt) {
        ItemStackHelper.saveAllItems(nbt, inventory, false);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }
    public void loadFromNbt(NBTTagCompound nbt) {
        inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (nbt.hasKey("Items", 9))
            ItemStackHelper.loadAllItems(nbt, inventory);

    }



    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        loadFromNbt(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        return saveToNbt(compound);
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.world.getBlockState(this.pos) == this.world.getBlockState(this.pos).withProperty(BlockAltar.ACTIVE, true)) {
            event.getController().setAnimation((new AnimationBuilder()).addAnimation("infuse", false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


}
