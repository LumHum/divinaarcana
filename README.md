# ✦ Divina Arcana

> *"I sense you, seeker. Finally. I've been waiting in the spaces between moments."*

A dark, atmospheric tarot reading desktop app built with JavaFX. All card art is fully procedural — no image assets — rendered in pure Java2D at runtime. Guided by **Wisp**, a mysterious oracle who speaks in typewriter text through a cinematic dialogue bar at the bottom of the screen.

---

```
        ╔═══════════════════════════════════════════════════╗
        ║  ✦ · · ·   D I V I N A   A R C A N A   · · · ✦  ║
        ║                                                   ║
        ║         ╭──────────╮   ╭──────────╮              ║
        ║         │  ╲    ╱  │   │  · ✦ ·  │              ║
        ║         │   ╲  ╱   │   │ ╱ ╲ ╱ ╲ │              ║
        ║         │    \/    │   │  ╲ X ╱  │              ║
        ║         │  ╱╲  ╱╲  │   │   ╲╱    │              ║
        ║         │ ╱  \/  ╲ │   │ ─────── │              ║
        ║         │ THE STAR │   │  DEATH  │              ║
        ║         ╰──────────╯   ╰──────────╯              ║
        ║                                                   ║
        ║  ┌─────────────────────────────────────────────┐ ║
        ║  │ 🔮 WISP  ✦  THE DARK ORACLE                │ ║
        ║  │  The cards have been drawn, seeker.         │ ║
        ║  │  Press Reveal to unveil your fate...        │ ║
        ║  │                                  [ Next ▶ ] │ ║
        ║  └─────────────────────────────────────────────┘ ║
        ╚═══════════════════════════════════════════════════╝
```

---

## Features

**Three Reading Types**
- **The Lone Oracle** — one card, one truth
- **The Triad Veil** — past, present, future across three cards
- **The Star of Fate** — five cards arranged in a pentagon: self, shadow, path, gift, destiny

**Procedural Card Art**
Every card is drawn at runtime with no image files. Each of the 78 cards has a unique bespoke symbol — layered aura rings, element-tinted radial gradients, triple gold border frames, scattered micro-sparkles, and ornate corner ornaments. Major Arcana each have a fully hand-coded symbol (The Tower has lightning and falling sparks; The Sun has 16 alternating rays; The World has a double wreath with a dancing figure). Minor suits feature an ornate wand/chalice/blade/pentacle rendered with decorative detail.

**Holographic Hover Effect**
Hovering a card zooms it to 1.13× with an element-coloured drop shadow glow. As the cursor moves across the card, a two-layer sheen renders on an overlay canvas — a soft radial hot-spot following the cursor (like light on a physical card), plus a subtle iridescent colour-play band that shifts angle with cursor direction, replicating the look of a foil trading card.

**Riffle Shuffle Animation**
The deck fan animates through four phases: spread wide → split into left and right halves → riffle (cards alternate from each half, staggered 28 ms apart) → re-fan to original positions.

**Wisp — The Dark Oracle**
A particle-based wisp character orbits a glowing core in real time. She speaks through a full-width cinematic dialogue bar with typewriter text and a mandatory Next button (2-second cooldown). All 78 cards have individual whisper interpretations. Six cards (Death, The Tower, The Devil, The Star, The Moon, Judgement) trigger special extended Wisp monologues.

**Atmospheric UI**
- Parallax star field that responds to mouse movement on every scene
- Scene transitions with fade and sound
- Idle whispers after 20 seconds of inactivity on the Oracle Chamber
- Personalised greeting on return visits after completing a reading

---

## Card Design

Cards are rendered at 130 × 213 px (golden ratio) by default and auto-scaled per spread.

**Back**
Deep space radial gradient (`#110D2A` → `#060610`), 45 seeded micro-sparkles, triple gold border (solid outer / 42% mid / 18% inner), 8-spoke mandala with orbit rings, diamond pip accents at spoke midpoints, glowing golden core with hot-spot gradient, flanked title rule.

**Front**
Element-tinted atmospheric radial glow, same triple border with element-coloured middle ring, value label top-left + element rune top-right (`△ ▽ ✦ ◈`), bespoke symbol per card with aura rings and inner orbit, horizontal separator rule with centre diamond, card name with flanking ornament.

Element accent colours: Fire `#E07828` · Water `#4EA8D9` · Air `#80C4D0` · Earth `#5E9C40` · Spirit `#B560F7` · Aether `#D4AF37`

---

## Tech Stack

| | |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 23.0.2 |
| Build | Maven |
| Art | Pure Java2D / Canvas API — zero image assets |
| Audio | JavaFX MediaPlayer |

---

## Running

```bash
git clone https://github.com/LumHum/divinaarcana.git
cd divinaarcana
./get-fonts.sh   # optional — downloads Cinzel fonts for the premium look
./run.sh
```

Requires Java 21+ and Maven. JavaFX is pulled automatically via the `org.openjfx` Maven plugin.

---

## Project Structure

```
src/main/java/com/divinaarcana/
├── DivinaArcana.java          # Application entry point, scene transitions
├── model/
│   ├── TarotCard.java         # 78-card deck model with whispers & meanings
│   └── ReadingType.java       # Reading types (Lone Oracle, Triad Veil, Star of Fate)
├── scene/
│   ├── SplashScene.java       # Opening — Wisp first appears
│   ├── OnboardingScene.java   # Name entry, Wisp introduction sequence
│   ├── OracleScene.java       # Reading selection chamber
│   ├── ShuffleScene.java      # Shuffle + riffle animation
│   └── ReadingScene.java      # Spread layout, card reveals, interpretations
├── view/
│   ├── CardView.java          # Procedural card art + hover sheen
│   ├── DialogueBar.java       # Full-width Wisp dialogue bar with typewriter + Next
│   ├── WispView.java          # Particle oracle character
│   └── StarField.java         # Parallax star background
└── util/
    ├── MagicDivination.java   # Card draw and shuffle logic
    ├── SoundOracle.java       # Sound effects
    └── WispDialogue.java      # All Wisp dialogue strings
```

---

*The veil opens. The cards are waiting.*

AI Disclosure: This readme file design was made by generative AI to look neat & clean!