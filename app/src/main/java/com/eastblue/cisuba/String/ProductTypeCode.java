package com.eastblue.cisuba.String;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by PJC on 2017-02-24.
 */

public class ProductTypeCode {

    public static ProductTypeCode productCode = null;
    HashMap<String, Integer> productMap;

    public static ProductTypeCode getInstance() {
        if(productCode == null) {
            productCode = new ProductTypeCode();
        }

        return productCode;
    }

    private ProductTypeCode() {
        productMap = new LinkedHashMap<>();
        productMap.put("목욕탕", 0);
        productMap.put("사우나", 1);
        productMap.put("찜질방", 2);
        productMap.put("워터파크", 3);
        productMap.put("온천", 4);
        productMap.put("남성전용", 5);
        productMap.put("여성전용", 6);
    }

    public String getProduct(String id) {
        for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
            if(entry.getValue() == Integer.parseInt(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public HashMap<String, Integer> getMap() {
        return productMap;
    }
}
