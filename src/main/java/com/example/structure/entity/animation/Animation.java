package com.example.structure.entity.animation;

public class Animation {
    @Deprecated
    private int id;
    private int Duration;


    private Animation(int duration) {
        this.Duration = duration;
    }


    public static Animation create(int id, int duration) {
        Animation animation = Animation.create(duration);
        animation.id = id;
        return animation;
    }

    public static Animation create(int duration) {
        return new Animation(duration);
    }

    @Deprecated
    public int getID() {
        return this.id;
    }

    /**
     * @return the duration of this model
     */
    public int getDuration() {
        return this.Duration;
    }
}
