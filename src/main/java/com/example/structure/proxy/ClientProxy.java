package com.example.structure.proxy;

import com.example.structure.blocks.BlockLeavesBase;
import com.example.structure.entity.util.data.GlowingMetadataSection;
import com.example.structure.entity.util.data.GlowingMetadataSectionSerializer;
import com.example.structure.event_handler.ClientRender;
import com.example.structure.event_handler.client.MusicHandlerEE;
import com.example.structure.gui.book.GuiBook;
import com.example.structure.sky.EndSkyHandler;
import com.example.structure.util.handlers.RenderHandler;
import com.example.structure.util.particles.ParticlePixel;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void setFancyGraphics(BlockLeavesBase block, boolean isFancy) {
        block.setFancyGraphics(isFancy);
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
        //Music handler for the Ash Wastelands
        registerEvent(new MusicHandlerEE());
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

    @Override
    public void spawnParticle(int particle, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        minecraft.effectRenderer.addEffect(getFactory(particle).createParticle(0, world, posX, posY, posZ, speedX, speedY, speedZ, parameters));
    }

    /**
     * This is used by the Particle Spawning as an ID system for out Particles.
     * We do not require Ids for Particles, it's just more convenient for sending over packets!
     * */
    @SideOnly(Side.CLIENT)
    public static IParticleFactory getFactory(int particleId)
    {
        switch(particleId)
        {
            default:
            case 1:
                return new ParticlePixel.Factory();
        }
    }

    @Override
    public boolean doesPlayerHaveXAdvancement(EntityPlayer player, ResourceLocation Id) {
        if(player instanceof EntityPlayerSP) {
            ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
            Advancement advancement = manager.getAdvancementList().getAdvancement(Id);
            if(advancement == null) {
                System.out.println("advancement is NULL");
                return false;
            }
            AdvancementProgress progress = manager.advancementToProgress.get(advancement);
            return progress != null && progress.isDone();
        }

        return super.doesPlayerHaveXAdvancement(player, Id);
    }

    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(EndSkyHandler.class);
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
        if (id == GUI_ALTAR) {
        }
        return null;
    }


    }
