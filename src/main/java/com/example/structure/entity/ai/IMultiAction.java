package com.example.structure.entity.ai;


public interface IMultiAction {

        void doAction();
        default void update() {
        }
        default boolean shouldExplodeUponImpact() {
            return false;
        }
        default boolean isImmuneToDamage() {
            return false;
        }
        default int attackLength() {
            return 100;
        }
        default int attackCooldown() {
            return 50;
        }

}
