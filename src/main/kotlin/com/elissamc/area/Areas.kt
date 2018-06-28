package com.elissamc.area

import cn.nukkit.level.Location
import com.elissamc.core.StorageEngine

object Areas {

    fun findArea(location: Location): ProtectedArea? {
        for (area in StorageEngine.getAreas().values)
            if (area.region.intersects(location))
                return area
        return null
    }

    /**
     * Returns area by name.
     *
     * @param name
     * name of area
     * @return protected area by name
     */
    fun getArea(name: String): ProtectedArea? {
        return StorageEngine.getAreas()[name]
    }
}