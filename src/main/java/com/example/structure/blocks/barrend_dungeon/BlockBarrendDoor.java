package com.example.structure.blocks.barrend_dungeon;

import com.example.structure.blocks.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBarrendDoor extends BlockBase {
    public BlockBarrendDoor(String name, Material material) {
        super(name, material);
    }

    public BlockBarrendDoor(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }
}
