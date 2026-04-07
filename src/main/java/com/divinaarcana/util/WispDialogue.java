package com.divinaarcana.util;

import java.util.List;
import java.util.Random;

import com.divinaarcana.model.ReadingType;
import com.divinaarcana.model.TarotCard;

public class WispDialogue {

    private static final Random rng = new Random();

    // onboarding — one line at a time, seeker
    public static final String[] FIRST_MEETING = {
        "I sense you, seeker. Finally. I've been waiting in the spaces between moments.",
        "They call me Wisp. I am the voice that dwells in shadow and cards. I know the language of fate.",
        "You've come to read what the cards conceal. This is... good. I hunger for such seekers.",
        "You will choose the cards. I will merely translate what they long to tell you. We are partners in mystery.",
        "After this first reading, you will understand something about yourself you've always known but never dared name. Are you prepared?"
    };

    public static final String ENTER_NAME = "Before we begin — what name do you carry, seeker? I will remember it long after this reading fades.";
    public static final String GREETING_RETURN = "You return. The cards sensed you were coming. They've been restless.";

    // pre-shuffle
    public static final String[] BEFORE_DRAW = {
        "Concentrate on your question. Not the words — the essence. The hunger beneath. Let it flow through your hands as you shuffle. The cards feel intention.",
        "Breathe. Let your mind become still as midnight. The shuffle is meditation. The draw is surrender. Do not think. Only feel.",
        "Clear your mind of everything except what truly matters to you right now. The cards are listening. They always are.",
        "Hold your question like a flame in a dark room — gently, deliberately. The cards will find it."
    };

    // reading type introductions
    public static String readingIntro(ReadingType type) {
        return switch (type) {
            case SINGLE_CARD ->
                "One card. One answer. Sometimes the simplest truths hide beneath our greatest questions. Draw, and let the universe speak.";
            case THREE_CARD ->
                "Three threads of fate pulled from the loom. Past, present, and future. Together they weave the pattern of your becoming.";
            case DESTINY_STAR ->
                "Five points of a star that only you can see. Self, shadow, path, gift, and destiny. The map of your becoming, seeker.";
        };
    }

    // after reading completes
    public static String readingComplete(ReadingType type) {
        return switch (type) {
            case SINGLE_CARD ->
                "There. One card. One truth revealed. Meditate on this image. Let its wisdom settle into the dark places within you where transformation occurs.";
            case THREE_CARD ->
                "Three cards have spoken. Do you see how they relate? This is your story, written in symbols older than memory. The path forward is now visible.";
            case DESTINY_STAR ->
                "Five points align. The star of your fate is drawn. I see the shape of your life in these cards, seeker. Study each connection.";
        };
    }

    // special card whispers
    public static String specialCardWhisper(TarotCard card) {
        return switch (card.getName()) {
            case "Death" ->
                "Ah. Death. Not ending — transformation. Your old self has overstayed its welcome, seeker. Feel the scythe. Let it cut clean. What dies in you was already dying.";
            case "The Tower" ->
                "The Tower. Lightning. Collapse. Destruction. Beautiful in its necessity. The structure you built must crumble. This is not punishment. This is mercy.";
            case "The Star" ->
                "The Star breaks through. I feel it — the light. Even in endless darkness, a star endures. Follow this light, though it is distant. Though it flickers. Follow it.";
            case "The Devil" ->
                "The devil smiles, and I smile with him. We both know your secret hunger. Acknowledge it. Understanding your chains is the first step to removing them.";
            case "The Moon" ->
                "The moon pulls the tides and the tides pull madness. Trust what you feel in this darkness. Your instincts are not lying to you. The fog obscures, but does not destroy.";
            case "Judgement" ->
                "The trumpet sounds. The reckoning comes for all. What will you answer when called by your true name, seeker? This is that moment.";
            default -> null;
        };
    }

    // idle whispers — shown when nothing is happening
    private static final List<String> IDLE_WHISPERS = List.of(
        "The cards grow restless... they have messages you haven't yet asked for...",
        "I see you sitting there, seeker. Even when you're not consulting me, I'm watching... waiting...",
        "The shadows deepen. Soon they'll have more to tell you. Will you listen?",
        "Something stirs beneath the surface of your life. I feel it awakening. The cards sense it too.",
        "You keep thinking of that last card you drew, don't you? It's not finished with you yet.",
        "The veil between worlds is thinner than usual tonight. Ask your question carefully.",
        "I've been here for centuries. Waiting for seekers like you. Exactly like you."
    );

    public static String idleWhisper() {
        return IDLE_WHISPERS.get(rng.nextInt(IDLE_WHISPERS.size()));
    }

    public static String beforeDraw() {
        return BEFORE_DRAW[rng.nextInt(BEFORE_DRAW.length)];
    }

    // card interpretation — builds on the card's own whisper or generates one
    public static String interpret(TarotCard card) {
        String special = specialCardWhisper(card);
        if (special != null) return special;
        if (!card.getWispWhisper().isEmpty()) return card.getWispWhisper();
        // fallback for any missing whispers
        String orientation = card.isReversed() ? "in shadow" : "in light";
        return "The " + card.getName() + " appears " + orientation + ". " + card.getDisplayMeaning() +
               ". Meditate on this, seeker. The cards rarely speak in accidents.";
    }
}
