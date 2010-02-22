package com.steeplesoft.meetspace.plugins;

public interface EventPlugin {
    void beforeAdd();
    void afterAdd();

    void beforeUpdate();
    void afterUpdate();

    void beforeDelete();
    void afterDelete();
}
