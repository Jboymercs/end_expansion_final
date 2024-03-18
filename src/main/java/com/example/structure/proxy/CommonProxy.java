package com.example.structure.proxy;

import com.example.structure.Main;
import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.event_handler.ModEvents;
import com.example.structure.gui.book.GuiBook;
import com.example.structure.packets.MessageDirectionForRender;
import com.example.structure.packets.MessageModParticles;
import com.example.structure.server.container.ContainterAltar;
import com.example.structure.util.ModReference;
import com.example.structure.world.Biome.WorldProviderEndEE;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class CommonProxy implements IGuiHandler {

    public static final int GUI_ALTAR = 1;
    public static final int GUI_BOOK = 2;
    public void registerItemRenderer(Item item, int meta, String id) {
    }


    public void setCustomState(Block block, IStateMapper mapper) {
    }
    public EntityPlayer getClientPlayer() {
        return null;
    }

    public World getClientWorld() {
        return getClientPlayer().world;
    }
    public void init() {
        MinecraftForge.EVENT_BUS.register(new ModEvents());
       // addnewBiome();
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.CHANNEL_NETWORK_NAME);
        int packetID = 0;
        Main.network.registerMessage(MessageModParticles.MessageHandler.class, MessageModParticles.class, packetID++, Side.CLIENT);
        Main.network.registerMessage(MessageDirectionForRender.Handler.class, MessageDirectionForRender.class, packetID++, Side.CLIENT);
    }

    public void addnewBiome() {
        DimensionManager.unregisterDimension(1);
        DimensionType endBiomes = DimensionType.register("End", "_end", 1, WorldProviderEndEE.class, false);
        DimensionManager.registerDimension(1, endBiomes);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        Entity entity = null;
        switch (ID) {
            case GUI_ALTAR: {
                if (tile instanceof TileEntityAltar)
                    return new ContainterAltar(player, (TileEntityAltar) tile);
                break;
            }
        }
        return null;
    }

    public void openGuiBook(ItemStack bestiary, EntityPlayer player) {

    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
