"""Generates the Scaler icon (original artwork): GUI panels at different sizes with a resize arrow."""
from PIL import Image, ImageDraw, ImageFilter
import os, sys

S = 1024
mask = Image.new("L", (S, S), 0)
ImageDraw.Draw(mask).rounded_rectangle([24, 24, S - 24, S - 24], radius=200, fill=255)

bg = Image.new("RGBA", (S, S))
d = ImageDraw.Draw(bg)
top, bot = (24, 40, 70), (10, 16, 32)
for y in range(S):
    t = y / (S - 1)
    d.line([(0, y), (S, y)], fill=tuple(int(top[i] * (1 - t) + bot[i] * t) for i in range(3)) + (255,))

art = Image.new("RGBA", (S, S), (0, 0, 0, 0))
a = ImageDraw.Draw(art)


def panel(x0, y0, x1, y1, fill, edge):
    """A Minecraft-style GUI panel: light top-left bevel, dark bottom-right bevel."""
    a.rectangle([x0, y0, x1, y1], fill=(20, 20, 20, 255))
    b = max(6, (x1 - x0) // 28)
    a.rectangle([x0 + b, y0 + b, x1 - b, y1 - b], fill=fill)
    a.rectangle([x0 + b, y0 + b, x1 - b, y0 + 2 * b], fill=edge)
    a.rectangle([x0 + b, y0 + b, x0 + 2 * b, y1 - b], fill=edge)


# Small panel (bottom-left) and big panel (top-right): the same GUI at two scales.
panel(150, 560, 470, 860, (198, 198, 198, 255), (255, 255, 255, 255))
for i in range(3):  # tiny hotbar slots
    x = 190 + i * 92
    a.rectangle([x, 760, x + 74, 834], fill=(139, 139, 139, 255))

panel(400, 150, 870, 620, (198, 198, 198, 255), (255, 255, 255, 255))
for r in range(2):
    for c in range(3):
        x, y = 450 + c * 135, 210 + r * 135
        a.rectangle([x, y, x + 110, y + 110], fill=(139, 139, 139, 255))
        a.rectangle([x, y, x + 110, y + 12], fill=(85, 85, 85, 255))
        a.rectangle([x, y, x + 12, y + 110], fill=(85, 85, 85, 255))

# Diagonal resize arrow from the small panel to the big one.
green = (90, 235, 140, 255)
a.line([(330, 700), (700, 330)], fill=green, width=58)
a.polygon([(760, 270), (600, 300), (730, 430)], fill=green)
a.polygon([(270, 760), (430, 730), (300, 600)], fill=green)

glow = art.filter(ImageFilter.GaussianBlur(18))
out = Image.alpha_composite(bg, glow)
out = Image.alpha_composite(out, art)
out = Image.composite(out, Image.new("RGBA", (S, S), (0, 0, 0, 0)), mask)

dest = sys.argv[1]
out.resize((512, 512), Image.LANCZOS).save(os.path.join(dest, "logo-512.png"))
out.resize((128, 128), Image.LANCZOS).save(os.path.join(dest, "icon.png"))
