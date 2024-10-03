package com.example.structure.blocks.arenaBlocks;

import net.minecraft.util.IStringSerializable;

public enum BlockEnumArenaStates implements IStringSerializable {
    //one state thats default
    //one state thats for when the arena is active
    //one statie for when it is ejecting rewards

    INACTIVE("inactive", 0, false),
    ACTIVE("active", 4, true),
    EJECTING("ejecting", 4, false);

    private final String name;
    private final int light_level;
    private final boolean is_active;


    BlockEnumArenaStates(String name, int light_level, boolean is_active) {
        this.name = name;
        this.light_level = light_level;
        this.is_active = is_active;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLightLevel() {
        return light_level;
    }

    public boolean isActive() {
        return is_active;
    }

}
