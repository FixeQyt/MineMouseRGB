package com.github.fixeqyt.colorcontrolrgb.logic;

import com.github.fixeqyt.colorcontrolrgb.DeviceInfo;
import com.github.fixeqyt.colorcontrolrgb.MouseColorAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShutdownColorRestorer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ShutdownColorRestorer.class);
    private final String initialColor;
    private final String initialScrollColor;

    public ShutdownColorRestorer(String initialColor, String initialScrollColor) {
        this.initialColor = initialColor;
        this.initialScrollColor = initialScrollColor;
    }

    @Override
    public void run() {
        try {
            if (initialColor != null && initialScrollColor != null) {
                String[] rgb = initialColor.split(",");
                int rLogo = Integer.parseInt(rgb[0].trim());
                int gLogo = Integer.parseInt(rgb[1].trim());
                int bLogo = Integer.parseInt(rgb[2].trim());
                String[] swRgb = initialScrollColor.split(",");
                int rSW = Integer.parseInt(swRgb[0].trim());
                int gSW = Integer.parseInt(swRgb[1].trim());
                int bSW = Integer.parseInt(swRgb[2].trim());
                MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), rLogo, gLogo, bLogo);
                MouseColorAPI.changeColor(DeviceInfo.getLEDs().getScrollWheelIndex(), rSW, gSW, bSW);
            }
        } catch (Exception e) {
            LOGGER.error("Error restoring initial color", e);
        }
    }
}

