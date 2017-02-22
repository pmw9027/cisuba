package com.eastblue.cisuba.String;

import java.util.HashMap;

/**
 * Created by PJC on 2017-02-22.
 */

public class LocationCode {

    public static LocationCode locationCode = null;
    HashMap<Integer, String> locationMap;

    public static LocationCode getInstance() {
        if(locationCode == null) {
            locationCode = new LocationCode();
        }

        return locationCode;
    }

    private LocationCode() {

        locationMap = new HashMap<Integer, String>();
        locationMap.put(1, "서울");
        locationMap.put(2, "부산");
        locationMap.put(3, "경기도");
        locationMap.put(4, "강원도");
        locationMap.put(5, "전라북도");
        locationMap.put(6, "전라남도");
        locationMap.put(7, "충청북도");
        locationMap.put(8, "충청남도");
        locationMap.put(9, "경상남도");
        locationMap.put(10, "경상북도");
    }

    public String getLocation(String id) {
        return locationMap.get(Integer.parseInt(id));
    }
}
