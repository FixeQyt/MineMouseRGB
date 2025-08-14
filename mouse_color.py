from fastapi import APIRouter
from openrgb import OpenRGBClient
from openrgb.utils import RGBColor
import time

router = APIRouter()
client = OpenRGBClient()

@router.post("/mouse/color/{ledIndex}/{r}/{g}/{b}")
def set_mouse_led_color(ledIndex: int, r: int, g: int, b: int):
    try:
        mouse = client.devices[0]
        mouse.leds[ledIndex].set_color(RGBColor(r, g, b))
        return {"status": "ok", "ledIndex": ledIndex, "color": [r, g, b]}
    except Exception as e:
        return {"status": "error", "message": str(e)}

@router.get("/mouse/color")
def get_mouse_colors():
    try:
        mouse = client.devices[0]
        # Pobieranie kolorów z device.colors zamiast mouse.leds[x].color
        colors = []
        for idx, color in enumerate(mouse.colors):
            colors.append({
                "ledIndex": idx,
                "r": color.red,
                "g": color.green,
                "b": color.blue
            })
        return {"colors": colors}
    except Exception as e:
        return {"status": "error", "message": str(e)}

@router.post("/mouse/color/blink/{ledIndex}/{amount}/{duration}/{r}/{g}/{b}")
def blink_mouse_led(ledIndex: int, amount: int, duration: int, r: int, g: int, b: int):
    try:
        mouse = client.devices[0]
        # Get current color
        original_color = mouse.colors[ledIndex]
        # Blink loop
        for _ in range(amount):
            mouse.leds[ledIndex].set_color(RGBColor(r, g, b))
            time.sleep(duration / 1000.0)
            mouse.leds[ledIndex].set_color(RGBColor(
                original_color.red, original_color.green, original_color.blue
            ))
            time.sleep(duration / 1000.0)
        return {
            "status": "ok",
            "ledIndex": ledIndex,
            "blinked_color": [r, g, b],
            "original_color": [original_color.red, original_color.green, original_color.blue],
            "amount": amount,
            "duration_ms": duration
        }
    except Exception as e:
        return {"status": "error", "message": str(e)}
