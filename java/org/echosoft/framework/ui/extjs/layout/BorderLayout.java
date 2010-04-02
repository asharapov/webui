package org.echosoft.framework.ui.extjs.layout;

import java.util.EnumMap;

import org.echosoft.framework.ui.core.UIComponent;

/**
 * Описывает характеристики менеджера раскладки <code>Ext.layout.BorderLayout</code>.
 * <p><strong>Важно!</strong> Данный тип менеджера компоновки работает только с экземплярами {@link BorderLayoutRegion} и его потомками.</p>
 * @author Anton Sharapov
 */
public class BorderLayout extends Layout {

    private final EnumMap<BorderLayoutRegion.Region, BorderLayoutRegion> regions;

    public BorderLayout() {
        super();
        this.regions = new EnumMap<BorderLayoutRegion.Region,BorderLayoutRegion>(BorderLayoutRegion.Region.class);
    }

    @Override
    public int getItemsCount() {
        return regions.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<UIComponent> getItems() {
        return (Iterable)regions.values();
    }

    @Override
    public <T extends UIComponent> T append(final T item) {
        if (item==null)
            throw new IllegalArgumentException("Component must be specified");
        if (!(item instanceof BorderLayoutRegion))
            throw new IllegalArgumentException("Layout manager can manage only BorderLayoutRegion instances");
        final BorderLayoutRegion region = (BorderLayoutRegion)item;
        if (regions.containsKey(region.getRegion()))
            throw new IllegalStateException("Layout manager already contains given region");
        regions.put(region.getRegion(), region);
        return item;
    }

    public BorderLayoutRegion getRegion(final BorderLayoutRegion.Region type, final boolean createIfNotExists) {
        BorderLayoutRegion region = regions.get(type);
        if (region==null && createIfNotExists) {
            region = new BorderLayoutRegion(type);
            regions.put(type, region);
        }
        return region;
    }

    public BorderLayoutRegion getNorth(final boolean createIfNotExists) {
        return getRegion(BorderLayoutRegion.Region.NORTH, createIfNotExists);
    }
    public BorderLayoutRegion getEast(final boolean createIfNotExists) {
        return getRegion(BorderLayoutRegion.Region.EAST, createIfNotExists);
    }
    public BorderLayoutRegion getSouth(final boolean createIfNotExists) {
        return getRegion(BorderLayoutRegion.Region.SOUTH, createIfNotExists);
    }
    public BorderLayoutRegion getWest(final boolean createIfNotExists) {
        return getRegion(BorderLayoutRegion.Region.WEST, createIfNotExists);
    }
    public BorderLayoutRegion getCenter() {
        return getRegion(BorderLayoutRegion.Region.CENTER, true);
    }

    @Override
    protected String getLayout() {
        return "border";
    }

}