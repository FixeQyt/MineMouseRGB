package com.github.fixeqyt.minemousergb.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlinkExecutor {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "MineMouseRGBMain-BlinkThread");
        t.setDaemon(true);
        return t;
    });

    public static ExecutorService get() {
        return EXECUTOR;
    }
}

