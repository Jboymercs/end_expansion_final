package com.example.structure.world.Biome.bridgestructure;

import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class BridgeStructureTemplate extends ModStructureTemplate {

    private int distance;

    public BridgeStructureTemplate() {
    }

    public BridgeStructureTemplate(TemplateManager manager, String type, int distance, BlockPos pos, Rotation rotation, boolean overwriteIn) {
        super(manager, type, pos, rotation, overwriteIn);
        this.distance = distance;
    }

    @Override
    public String templateLocation() {
        return "bridge_structure";
    }

    @Override
    public int getDistance() {
        return this.distance;
    }

}
