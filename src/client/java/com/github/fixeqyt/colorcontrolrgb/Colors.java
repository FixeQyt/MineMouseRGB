package com.github.fixeqyt.colorcontrolrgb;

public enum Colors {
    LOGO_INIT(239, 50, 61),
    SCROLL_INIT(191, 255, 0),
    MENU(189, 178, 173),
    OVERWORLD(127, 178, 56),
    NETHER(255, 85, 85),
    END(112, 0, 255),
    DAMAGE(255, 0, 0);

    public final int r;
    public final int g;
    public final int b;

    Colors(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int[] rgb() {
        return new int[] {r, g, b};
    }
}
