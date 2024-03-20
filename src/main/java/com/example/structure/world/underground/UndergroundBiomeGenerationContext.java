package com.example.structure.world.underground;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UndergroundBiomeGenerationContext {


        public final List<BlockPos> floorList = new LinkedList<>();
        public final List<BlockPos> ceilingList = new LinkedList<>();
        public final List<BlockPos> insideList = new LinkedList<>();

        public final Map<BlockPos, EnumFacing> wallMap = new HashMap<>();


}
