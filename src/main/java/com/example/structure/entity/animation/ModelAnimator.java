package com.example.structure.entity.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.processor.IBone;

import java.util.HashMap;


/**
 * An attempt at backporting parts of Citedal, mainly it's Animation Functions
 */
@SideOnly(Side.CLIENT)
public class ModelAnimator {
    private int tempTick;
    private int prevTempTick;
    private boolean correctAnimation;
    private IAnimatedEntity entity;
    private HashMap<IBone, Transform> transformMap;
    private HashMap<IBone, Transform> prevTransformMap;

    public float rotationPointX;
   public float rotationPointY;
   public float rotateAngleZ;

    public ModelAnimator() {
        this.tempTick = 0;
        this.correctAnimation = false;
        this.transformMap = new HashMap<>();
        this.prevTransformMap = new HashMap<>();
    }

    /**
     * @return a new ModelAnimator instance
     */
    public static ModelAnimator create() {
        return new ModelAnimator();
    }

    /**
     * @return the {@link IAnimatedEntity} instance. Null if {@link ModelAnimator#update}
     */
    public IAnimatedEntity getEntity() {
        return this.entity;
    }

    /**
     * Update the animations of this model.
     *
     * @param entity the entity instance
     */
    public void update(IAnimatedEntity entity) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = false;
        this.entity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
    }

    /**
     * Start an model
     *
     * @param animation the model instance
     * @return true if it's the current model
     */
    public boolean setAnimation(Animation animation) {
        this.tempTick = this.prevTempTick = 0;
        this.correctAnimation = this.entity.getAnimation() == animation;
        return this.correctAnimation;
    }

    /**
     * Start a keyframe for the current model.
     *
     * @param duration the keyframe duration
     */
    public void startKeyframe(int duration) {
        if (!this.correctAnimation) {
            return;
        }
        this.prevTempTick = this.tempTick;
        this.tempTick += duration;
    }

    /**
     * Add a static keyframe with a specific duration to the model.
     *
     * @param duration the keyframe duration
     */
    public void setStaticKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    /**
     * Reset this keyframe to its original state
     *
     * @param duration the keyframe duration
     */
    public void resetKeyframe(int duration) {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    /**
     * Rotate a box in the current keyframe. All the values are relative.
     *
     * @param box the box to rotate
     * @param x   the x rotation
     * @param y   the y rotation
     * @param z   the z rotation
     */
    public void rotate(IBone box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        this.getTransform(box).addRotation(x, y, z);
    }

    /**
     * Move a box in the current keyframe. All the values are relative.
     *
     * @param box the box to move
     * @param x   the x offset
     * @param y   the y offset
     * @param z   the z offset
     */
    public void move(IBone box, float x, float y, float z) {
        if (!this.correctAnimation) {
            return;
        }
        this.getTransform(box).addOffset(x, y, z);
    }

    private Transform getTransform(IBone box) {
        return this.transformMap.computeIfAbsent(box, b -> new Transform());
    }

    /**
     * End the current keyframe. this will reset all box transformations to their original state.
     */
    public void endKeyframe() {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary) {
        if (!this.correctAnimation) {
            return;
        }
        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.prevTempTick && animationTick < this.tempTick) {
            if (stationary) {
                for (IBone box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.setRotationX(+ transform.getRotationX());
                    box.setRotationY(+ transform.getRotationY());
                    box.setRotationZ( + transform.getRotationZ());
                    box.setPositionX(+ transform.getOffsetX());
                    box.setPositionY(+ transform.getOffsetY());
                    box.setPositionZ(+ transform.getOffsetZ());
                }
            } else {
                float tick = (animationTick - this.prevTempTick +  (float) Minecraft.getSystemTime()) / (this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2.0F)), dec = 1.0F - inc;
                for (IBone box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.setRotationX(+ dec * transform.getRotationX());
                    box.setRotationY(+ dec * transform.getRotationY());
                    box.setRotationZ(+ dec * transform.getRotationZ());
                    box.setPositionX(+ dec * transform.getOffsetX());
                    box.setPositionY(+ dec * transform.getOffsetY());
                    box.setPositionZ(+ dec * transform.getOffsetZ());
                }
                for (IBone box : this.transformMap.keySet()) {
                    Transform transform = this.transformMap.get(box);
                    box.setRotationX(+ inc * transform.getRotationX());
                    box.setRotationY(+ inc * transform.getRotationY());
                    box.setRotationZ(+ inc * transform.getRotationZ());
                    box.setPositionX(+ inc * transform.getOffsetX());
                    box.setPositionY(+ inc * transform.getOffsetY());
                    box.setPositionZ(+ inc * transform.getOffsetZ());
                }
            }
        }

        if (!stationary) {
            this.prevTransformMap.clear();
            this.prevTransformMap.putAll(this.transformMap);
            this.transformMap.clear();
        }
    }
}
