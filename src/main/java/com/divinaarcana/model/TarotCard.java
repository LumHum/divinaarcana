package com.divinaarcana.model;

public class TarotCard {

    public enum Arcana { MAJOR, MINOR }
    public enum Suit { WANDS, CUPS, SWORDS, PENTACLES }
    public enum Element { FIRE, WATER, AIR, EARTH, SPIRIT, AETHER }

    private final String name;
    private final Arcana arcana;
    private final Suit suit;
    private final int value;
    private final String uprightMeaning;
    private final String reversedMeaning;
    private final Element element;
    private final String keywords;
    private final String wispWhisper;
    private boolean isReversed;

    public TarotCard(String name, Arcana arcana, Suit suit, int value,
                     String uprightMeaning, String reversedMeaning,
                     Element element, String keywords, String wispWhisper) {
        this.name = name;
        this.arcana = arcana;
        this.suit = suit;
        this.value = value;
        this.uprightMeaning = uprightMeaning;
        this.reversedMeaning = reversedMeaning;
        this.element = element;
        this.keywords = keywords;
        this.wispWhisper = wispWhisper;
    }

    public String getDisplayMeaning() {
        return isReversed ? reversedMeaning : uprightMeaning;
    }

    public String getOrientationLabel() {
        return isReversed ? "✦ Reversed ✦" : "✦ Upright ✦";
    }

    public String getSuitSymbol() {
        if (arcana == Arcana.MAJOR) return "✶";
        return switch (suit) {
            case WANDS -> "𝌆";
            case CUPS -> "◉";
            case SWORDS -> "✦";
            case PENTACLES -> "⬡";
        };
    }

    public String getValueLabel() {
        if (arcana == Arcana.MAJOR) return toRoman(value);
        return switch (value) {
            case 1 -> "ACE";
            case 11 -> "PAGE";
            case 12 -> "KNIGHT";
            case 13 -> "QUEEN";
            case 14 -> "KING";
            default -> toRoman(value);
        };
    }

    private String toRoman(int n) {
        String[] r = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                       "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI"};
        return n >= 0 && n <= 21 ? r[n] : String.valueOf(n);
    }

    // getters
    public String getName() { return name; }
    public Arcana getArcana() { return arcana; }
    public Suit getSuit() { return suit; }
    public int getValue() { return value; }
    public String getUprightMeaning() { return uprightMeaning; }
    public String getReversedMeaning() { return reversedMeaning; }
    public Element getElement() { return element; }
    public String getKeywords() { return keywords; }
    public String getWispWhisper() { return wispWhisper; }
    public boolean isReversed() { return isReversed; }
    public void setReversed(boolean reversed) { isReversed = reversed; }
}
