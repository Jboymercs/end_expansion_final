package com.example.structure.world.Biome.altardungeon;

import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class AltarTemplate extends ModStructureTemplate {

    public AltarTemplate() {

    }

    public AltarTemplate(TemplateManager templateManager, String type, BlockPos blockPos, Rotation rotation, int distance, boolean overwriteIn) {
        super(templateManager, type, blockPos, rotation, overwriteIn);
    }
    @Override
    public String templateLocation() {
        return "altar_dungeon";
    }
}
