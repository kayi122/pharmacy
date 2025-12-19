package com.example.pharmacymanagementsystem.util;

import com.example.pharmacymanagementsystem.model.Location;

public class LocationHelper {

    public static String getProvince(Location location) {
        Location current = location;
        while (current != null) {
            if (current.getType() == Location.LocationType.PROVINCE) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    public static String getDistrict(Location location) {
        Location current = location;
        while (current != null) {
            if (current.getType() == Location.LocationType.DISTRICT) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    public static String getSector(Location location) {
        Location current = location;
        while (current != null) {
            if (current.getType() == Location.LocationType.SECTOR) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    public static String getCell(Location location) {
        Location current = location;
        while (current != null) {
            if (current.getType() == Location.LocationType.CELL) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    public static String getVillage(Location location) {
        Location current = location;
        while (current != null) {
            if (current.getType() == Location.LocationType.VILLAGE) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }
}
