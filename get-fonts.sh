#!/bin/bash
# downloads the Cinzel fonts from Google Fonts for the premium look
# run this once before launching the app — totally optional but very pretty

FONT_DIR="src/main/resources/fonts"
mkdir -p "$FONT_DIR"

echo "✦ Fetching Cinzel Regular..."
curl -L "https://fonts.gstatic.com/s/cinzel/v23/8vIU7ww63mVu7gt79mT7.woff2" -o "$FONT_DIR/Cinzel-Regular.ttf" 2>/dev/null \
  || echo "  (couldn't fetch — app works fine with system fonts)"

echo "✦ Fetching Cinzel Decorative Regular..."
curl -L "https://fonts.gstatic.com/s/cinzeldecorative/v16/daaHSScvJGqLYhG8nNt8KPadrOzf5fQx.woff2" -o "$FONT_DIR/CinzelDecorative-Regular.ttf" 2>/dev/null \
  || echo "  (couldn't fetch — app works fine with system fonts)"

echo ""
echo "✦ Done. Launch with: ./run.sh"
