package com.github.fixeqyt.minemousergb;

public class DeviceInfo {
    public static LEDs getLEDs() {
        return new LEDs();
    }

    public static class LEDs {
        public int getLogoIndex() {
            return 1;
        }
        public int getScrollWheelIndex() {
            return 0;
        }
    }
}
