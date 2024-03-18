package com.example.structure.proxy;

import com.example.structure.entity.animation.IAnimatedEntity;
import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.entity.util.data.GlowingMetadataSection;
import com.example.structure.entity.util.data.GlowingMetadataSectionSerializer;
import com.example.structure.event_handler.ClientRender;
import com.example.structure.gui.GuiAltar;
import com.example.structure.gui.book.GuiBook;
import com.example.structure.server.container.ContainterAltar;
import com.example.structure.util.handlers.RenderHandler;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }


    @Override
    public void init() {
        Minecraft mc = Minecraft.getMinecraft();


        // Add custom metadataserializers
        mc.metadataSerializer.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);

        //Registers Geckolib Entities
        RenderHandler.registerGeoEntityRenderers();
        super.init();
    }


    @SideOnly(Side.CLIENT)
    public static float getClientEffect(int selector, float defaultVal) {
        switch (selector) {
            case 1: return ClientRender.SCREEN_SHAKE;
            default: return defaultVal;
        }
    }

    //Will come back to this and clean it up and put it in with the other GUI's
    @SideOnly(Side.CLIENT)
    @Override
    public void openGuiBook(ItemStack stack, EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBook(stack, player));
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        Entity entity = null;
        switch (id) {
            case GUI_ALTAR: {
                if (tile instanceof TileEntityAltar)
                    return new GuiAltar(player, (TileEntityAltar) tile);
                break;
            }
        }
        return null;
    }


    }
