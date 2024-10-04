package com.example.structure.blocks.barrend_dungeon;

import com.example.structure.blocks.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBarrendDungeonTile extends BlockBase {
    public BlockBarrendDungeonTile(String name, Material material) {
        super(name, material);
    }

    public BlockBarrendDungeonTile(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.setBlockUnbreakable();
    }
}
