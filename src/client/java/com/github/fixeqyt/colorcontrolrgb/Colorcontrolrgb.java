package com.github.fixeqyt.colorcontrolrgb;

import com.github.fixeqyt.colorcontrolrgb.logic.BlinkExecutor;
import com.github.fixeqyt.colorcontrolrgb.logic.ColorJsonParser;
import com.github.fixeqyt.colorcontrolrgb.logic.ShutdownColorRestorer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Colorcontrolrgb implements ModInitializer {

    private static final Logger LOGGER = LogManager.getLogger(Colorcontrolrgb.class);

    private String initialColor;
    private String initialScrollColor;
    private static RegistryKey<World> lastWorldKey = null;
    private int lastHurtTime = 0;

    @Override
    public void onInitialize() {
        try {
            String colorJson = MouseColorAPI.getCurrentColor();
            int[] rgb = ColorJsonParser.parseLogoColor(colorJson);
            initialColor = rgb[0] + "," + rgb[1] + "," + rgb[2];
            int[] swRgb = ColorJsonParser.parseScrollWheelColor(colorJson);
            initialScrollColor = swRgb[0] + "," + swRgb[1] + "," + swRgb[2];

            MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.LOGO_INIT.r, Colors.LOGO_INIT.g, Colors.LOGO_INIT.b);
            MouseColorAPI.changeColor(DeviceInfo.getLEDs().getScrollWheelIndex(), Colors.SCROLL_INIT.r, Colors.SCROLL_INIT.g, Colors.SCROLL_INIT.b);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize mouse colors", e);
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.MENU.r, Colors.MENU.g, Colors.MENU.b);
            } catch (Exception e) {
                LOGGER.error("Error in delayed color change", e);
            }
        }).start();

        // Change color once when entering overworld, nether, or end
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null && !mc.player.isDead() && mc.player.hurtTime > 0 && mc.player.age > 10) {
                if (mc.player.hurtTime > lastHurtTime) {
                    lastHurtTime = mc.player.hurtTime;
                    BlinkExecutor.get().submit(() -> {
                        try {
                            MouseColorAPI.blinkColor(DeviceInfo.getLEDs().getLogoIndex(), 1, 250, Colors.DAMAGE.r, Colors.DAMAGE.g, Colors.DAMAGE.b);
                        } catch (IOException e) {
                            LOGGER.error("Error blinking logo on damage", e);
                        }
                    });
                }
            } else {
                lastHurtTime = 0;
            }

            if (mc.world == null) {
                // If we exited to the menu, restore the menu color
                if (Colorcontrolrgb.lastWorldKey != null) {
                    try {
                        MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.MENU.r, Colors.MENU.g, Colors.MENU.b);
                    } catch (Exception e) {
                        LOGGER.error("Error restoring menu color", e);
                    }
                    Colorcontrolrgb.lastWorldKey = null;
                }
                return;
            }
            var currentKey = mc.world.getRegistryKey();
            if (Colorcontrolrgb.lastWorldKey == null || !Colorcontrolrgb.lastWorldKey.equals(currentKey)) {
                Colorcontrolrgb.lastWorldKey = currentKey;
                try {
                    if (Colorcontrolrgb.lastWorldKey.equals(World.OVERWORLD)) {
                        MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.OVERWORLD.r, Colors.OVERWORLD.g, Colors.OVERWORLD.b);
                    } else if (Colorcontrolrgb.lastWorldKey.equals(World.NETHER)) {
                        MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.NETHER.r, Colors.NETHER.g, Colors.NETHER.b);
                    } else if (Colorcontrolrgb.lastWorldKey.equals(World.END)) {
                        MouseColorAPI.changeColor(DeviceInfo.getLEDs().getLogoIndex(), Colors.END.r, Colors.END.g, Colors.END.b);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error changing color for dimension", e);
                }
            }
        });

        // Restore the initial color when the game window is closed
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownColorRestorer(initialColor, initialScrollColor)));
    }
}
