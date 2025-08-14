package com.github.fixeqyt.minemousergb.logic;

import com.github.fixeqyt.minemousergb.DeviceInfo;
import org.json.JSONObject;

public class ColorJsonParser {
    public static int[] parseLogoColor(String json) {
        JSONObject obj = new JSONObject(json);
        for (Object o : obj.getJSONArray("colors")) {
            JSONObject led = (JSONObject) o;
            if (led.getInt("ledIndex") == DeviceInfo.getLEDs().getLogoIndex()) {
                return new int[] { led.getInt("r"), led.getInt("g"), led.getInt("b") };
            }
        }
        throw new RuntimeException("Logo LED not found in JSON");
    }

    public static int[] parseScrollWheelColor(String json) {
        JSONObject obj = new JSONObject(json);
        for (Object o : obj.getJSONArray("colors")) {
            JSONObject led = (JSONObject) o;
            if (led.getInt("ledIndex") == DeviceInfo.getLEDs().getScrollWheelIndex()) {
                return new int[] { led.getInt("r"), led.getInt("g"), led.getInt("b") };
            }
        }
        throw new RuntimeException("Scroll wheel LED not found in JSON");
    }
}

