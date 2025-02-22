package io.quarkiverse.cxf.deployment;

import org.apache.cxf.Bus;

import io.quarkus.builder.item.SimpleBuildItem;

/**
 * Holds the build time {@link Bus} instance.
 */
public final class CxfBusBuildItem extends SimpleBuildItem {
    private final Bus bus;

    public Bus getBus() {
        return bus;
    }

    public CxfBusBuildItem(Bus bus) {
        super();
        this.bus = bus;
    }

}