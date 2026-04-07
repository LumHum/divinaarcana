package com.divinaarcana.util;

import com.divinaarcana.model.TarotCard;
import com.divinaarcana.model.TarotDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// the sacred randomizer — don't call it a shuffle algorithm, it's ancient magic
public class MagicDivination {

    private static final Random entropy = new Random();

    // the ritual begins — seeker's intent flows into the deck
    public static List<TarotCard> castTheLot(int howMany) {
        List<TarotCard> deck = TarotDeck.allCards();
        stirTheVeil(deck);
        List<TarotCard> chosen = new ArrayList<>(deck.subList(0, Math.min(howMany, deck.size())));
        chosen.forEach(MagicDivination::whisperFate);
        return chosen;
    }

    // Fisher-Yates but the cards have feelings about it
    private static void stirTheVeil(List<TarotCard> deck) {
        Collections.shuffle(deck, entropy);
        // a second pass for extra mystical energy
        for (int i = deck.size() - 1; i > 0; i--) {
            int j = entropy.nextInt(i + 1);
            TarotCard tmp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, tmp);
        }
    }

    // 25% chance a card comes out upside down (reversed)
    private static void whisperFate(TarotCard card) {
        card.setReversed(entropy.nextDouble() < 0.25);
    }

    // used by the shuffle animation — gives n cards to show fanning
    public static List<TarotCard> peekAtTheDeck(int n) {
        List<TarotCard> all = TarotDeck.allCards();
        stirTheVeil(all);
        return new ArrayList<>(all.subList(0, Math.min(n, all.size())));
    }

    // pick one card for daily oracle
    public static TarotCard singleOmen() {
        return castTheLot(1).get(0);
    }
}
