package com.elissamc.area;

import cn.nukkit.level.Location;
import com.elissamc.core.StorageEngine;

public class Areas {

    public static final ProtectedArea findArea(final Location location) {
        for (ProtectedArea area : StorageEngine.getAreas().values())
            if (area.getRegion().intersects(location))
                return area;
        return null;
    }

    /**
     * Returns area by name.
     *
     * @param name
     *            name of area
     * @return protected area by name
     */
    public static final ProtectedArea getArea(final String name) {
        return StorageEngine.getAreas().get(name);
    }
}