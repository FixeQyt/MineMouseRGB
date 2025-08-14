package com.github.fixeqyt.colorcontrolrgb;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class MouseColorAPI {
    private static @NotNull String sendRequest(String urlStr, String method) throws IOException {
        var conn = (HttpURLConnection) URI.create(urlStr).toURL().openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.getResponseCode();
        var sb = new StringBuilder();
        try (var is = conn.getInputStream();
             var reader = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        } catch (IOException ignored) {}
        conn.disconnect();
        return sb.toString();
    }

    /**
     * Changes the color of the mouse logo to the specified RGB values.
     *
     * @param r the red component of the color (0-255)
     * @param g the green component of the color (0-255)
     * @param b the blue component of the color (0-255)
     * @throws IOException if an I/O error occurs during the request
     */
    public static void changeColor(int ledIndex, int r, int g, int b) throws IOException {
        String urlStr = String.format("http://localhost:6969/mouse/color/%d/%d/%d/%d", ledIndex, r, g, b);
        sendRequest(urlStr, "POST");
    }

    /**
     * Retrieves the current color of the mouse.
     *
     * @return a string representing the current color of the mouse
     * @throws IOException if an I/O error occurs during the request
     */
    public static String getCurrentColor() throws IOException {
        String urlStr = "http://localhost:6969/mouse/color";
        return sendRequest(urlStr, "GET"); // Returns e.g. `{"scroll_wheel":{"r":0,"g":0,"b":0},"logo":{"r":0,"g":0,"b":0}}`
    }

        /**
         * Powoduje, że myszka miga wybranym kolorem przez określoną liczbę razy i czas trwania.
         *
         * @param amount   liczba mignięć
         * @param duration czas trwania jednego mignięcia (w milisekundach)
         * @param r        składowa czerwona koloru (0-255)
         * @param g        składowa zielona koloru (0-255)
         * @param b        składowa niebieska koloru (0-255)
         * @throws IOException jeśli wystąpi błąd wejścia/wyjścia podczas żądania
         */
        public static void blinkColor(int ledIndex, int amount, int duration, int r, int g, int b) throws IOException {
            String urlStr = String.format("http://localhost:6969/mouse/color/blink/%d/%d/%d/%d/%d/%d", ledIndex, amount, duration, r, g, b);
            sendRequest(urlStr, "POST");
        }
}