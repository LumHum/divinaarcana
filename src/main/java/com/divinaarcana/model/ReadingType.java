package com.divinaarcana.model;

public enum ReadingType {
    SINGLE_CARD("The Lone Oracle",  "One card. One truth.",               1),
    THREE_CARD( "The Triad Veil",   "Past · Present · Future",            3),
    DESTINY_STAR("The Star of Fate","Five points of your becoming.",       5);

    private final String displayName;
    private final String description;
    private final int cardCount;

    ReadingType(String displayName, String description, int cardCount) {
        this.displayName = displayName;
        this.description = description;
        this.cardCount   = cardCount;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int    getCardCount()   { return cardCount; }

    public String[] getPositionNames() {
        return switch (this) {
            case SINGLE_CARD  -> new String[]{"The Truth"};
            case THREE_CARD   -> new String[]{"The Past", "The Present", "The Future"};
            case DESTINY_STAR -> new String[]{"The Self", "The Shadow", "The Path", "The Gift", "The Destiny"};
        };
    }
}
