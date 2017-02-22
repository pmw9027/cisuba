package com.eastblue.cisuba.String;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by PJC on 2017-02-22.
 */

public class LocationCode {

    public static LocationCode locationCode = null;
    HashMap<String, Integer> locationMap;

    public static LocationCode getInstance() {
        if(locationCode == null) {
            locationCode = new LocationCode();
        }

        return locationCode;
    }

    private LocationCode() {

        locationMap = new HashMap<String, Integer>();
        locationMap.put("전체", 0);
        locationMap.put("서울", 1);
        locationMap.put("부산", 2);
        locationMap.put("경기도", 3);
        locationMap.put("강원도", 4);
        locationMap.put("전라북도", 5);
        locationMap.put("전라남도", 6);
        locationMap.put("충청북도", 7);
        locationMap.put("충청남도", 8);
        locationMap.put("경상남도", 9);
        locationMap.put("경상북도", 10);
    }

    public String getLocation(String id) {
        for (Map.Entry<String, Integer> entry : locationMap.entrySet()) {
            if(entry.getValue() == Integer.parseInt(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public HashMap<String, Integer> getMap() {
        return locationMap;
    }

}
