package com.example.structure.world.Biome.bridgestructure;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class BridgeStructure {
    /**
     * Goal of this is to generate bridges with bits and pieces scattered
     */

    private static String[] TOWERS = {"ash_cross_1"};
    private static String[] BRIDGES = {"ash_bridge_1"};

    private static final int SIZE = 3;

    //Center of the Broken Bridges
    public static void startFortress(World world, TemplateManager manager, BlockPos pos, Rotation rotation, List<StructureComponent> components, Random rand) {
        BridgeStructureTemplate template = new BridgeStructureTemplate(manager, "ash_cross_1", 0, pos, rotation, false);
        components.add(template);
       BridgeStructureTemplate.resetTemplateCount();
    }

    private static boolean generateBridge(TemplateManager manager, BridgeStructureTemplate parent, List<StructureComponent> components, Random random) {
    return false;
    }

    /**
     * Determines if the new template is overlapping with another template
     */
    private static boolean isColliding(TemplateManager manager, BridgeStructureTemplate childTemplate, List<StructureComponent> structures, Random rand) {
        StructureComponent collision = BridgeStructureTemplate.findIntersectingExclusive(structures, childTemplate.getBoundingBox());

        if (collision != null) {
            return true;
        }

        return false;
    }
}
