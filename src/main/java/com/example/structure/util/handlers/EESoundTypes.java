package com.example.structure.util.handlers;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class EESoundTypes {

  //  public static final SoundType MOSS = new SoundType(1, 1, SoundsHandler.MOSS_BREAK, SoundsHandler.MOSS_STEP,
  //          SoundsHandler.MOSS_PLACE, SoundsHandler.MOSS_HIT, SoundsHandler.MOSS_HIT);

    public static final SoundType ASH = new SoundType(1, 1, ModSoundHandler.ASH_BREAK, ModSoundHandler.ASH_STEP, ModSoundHandler.ASH_PLACE,
            ModSoundHandler.ASH_HIT, ModSoundHandler.ASH_HIT);

    public static final SoundType ASH_BRICK = new SoundType(1, 1, ModSoundHandler.ASH_BRICK_BREAK, ModSoundHandler.ASH_BRICK_STEP, ModSoundHandler.ASH_BRICK_PLACE,
            ModSoundHandler.ASH_HIT, ModSoundHandler.ASH_HIT);

    public static final SoundType BARREND_LOG = new SoundType(1, 1, ModSoundHandler.BARE_LOG_BREAK, ModSoundHandler.BARE_LOG_STEP, ModSoundHandler.BARE_LOG_PLACE,
            ModSoundHandler.BARE_LOG_HIT, ModSoundHandler.BARE_LOG_HIT);

    public static final SoundType CRYSTAL_PURPLE = new SoundType(1, 1.4F, ModSoundHandler.CRYSTAL_BREAK, SoundEvents.BLOCK_GLASS_STEP, ModSoundHandler.CRYSTAL_PLACE,
            SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_HIT);

    public static final SoundType CRYSTAL_GREEN = new SoundType(1, 1.0F, ModSoundHandler.CRYSTAL_BREAK, SoundEvents.BLOCK_GLASS_STEP, ModSoundHandler.CRYSTAL_PLACE,
            SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_HIT);

    public static final SoundType CRYSTAL_RED = new SoundType(1, 0.6F, ModSoundHandler.CRYSTAL_BREAK, SoundEvents.BLOCK_GLASS_STEP, ModSoundHandler.CRYSTAL_PLACE,
            SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_HIT);
}
