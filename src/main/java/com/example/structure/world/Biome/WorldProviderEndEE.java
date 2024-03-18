package com.example.structure.world.Biome;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class WorldProviderEndEE extends WorldProviderEnd {
    private DragonFightManager dragonFightManager;

    @Override
    public void init()
    {
        this.biomeProvider = new EndBiomeProvider(world.getSeed(), world.getWorldType());

        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
        this.dragonFightManager = this.world instanceof WorldServer ? new DragonFightManager((WorldServer)this.world, nbttagcompound.getCompoundTag("DragonFight")) : null;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new WorldChunkGeneratorEE(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
    }

    public void onWorldSave()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (this.dragonFightManager != null)
        {
            nbttagcompound.setTag("DragonFight", this.dragonFightManager.getCompound());
        }

        this.world.getWorldInfo().setDimensionData(this.world.provider.getDimension(), nbttagcompound);
    }

    public void onWorldUpdateEntities()
    {
        if (this.dragonFightManager != null)
        {
            this.dragonFightManager.tick();
        }
    }

    @Nullable
    public DragonFightManager getDragonFightManager()
    {
        return this.dragonFightManager;
    }

    /**
     * Called when a Player is added to the provider's world.
     */
    @Override
    public void onPlayerAdded(net.minecraft.entity.player.EntityPlayerMP player)
    {
        if (this.dragonFightManager != null)
        {
            this.dragonFightManager.addPlayer(player);
        }
    }




    /**
     * Called when a Player is removed from the provider's world.
     */
    @Override
    public void onPlayerRemoved(net.minecraft.entity.player.EntityPlayerMP player)
    {
        if (this.dragonFightManager != null)
        {
            this.dragonFightManager.removePlayer(player);
        }
    }
}
