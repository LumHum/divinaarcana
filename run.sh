#!/bin/bash
# run Divina Arcana — one command does it all

set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$DIR"

# check Java 21+
JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ "$JAVA_VER" -lt 21 ] 2>/dev/null; then
  echo ""
  echo "  ✦ Divina Arcana requires Java 21 or later."
  echo "  Install it from: https://adoptium.net"
  echo ""
  exit 1
fi

# check mvn
if ! command -v mvn &> /dev/null; then
  echo ""
  echo "  ✦ Maven is required. Install it with:"
  echo "    brew install maven     (Mac)"
  echo "    or from: https://maven.apache.org"
  echo ""
  exit 1
fi

echo ""
echo "  ✦  The veil opens...  ✦"
echo ""

mvn -q javafx:run
