package com.divinaarcana.model;

import com.divinaarcana.model.TarotCard.Arcana;
import com.divinaarcana.model.TarotCard.Suit;
import com.divinaarcana.model.TarotCard.Element;

import java.util.ArrayList;
import java.util.List;

public class TarotDeck {

    private static final List<TarotCard> ALL_CARDS = new ArrayList<>();

    static {
        // --- Major Arcana ---
        add("The Fool", Arcana.MAJOR, null, 0,
            "New beginnings await those bold enough to step into the unknown",
            "Recklessness consumes the unprepared; folly masquerades as destiny",
            Element.SPIRIT, "beginnings, spontaneity, leap of faith",
            "The veil stirs... I see a figure dancing at the cliff's edge. They know not the abyss below, yet step forward nonetheless. Such beautiful ignorance. Such magnificent peril.");

        add("The Magician", Arcana.MAJOR, null, 1,
            "Your will shapes reality; manifest your intentions with focused power",
            "Illusions deceive; deception masks the truth within yourself",
            Element.AETHER, "will, power, manifestation",
            "I watch as mortal hands weave threads of potential. The magician knows: intention is the spell. But beware — what you build with smoke may crumble to ash.");

        add("The High Priestess", Arcana.MAJOR, null, 2,
            "Intuition whispers secrets; trust the wisdom that dwells within silence",
            "Hidden truths remain veiled; the priestess guards her mysteries still",
            Element.WATER, "intuition, mystery, secrets",
            "Behind the veil sits she who knows all. I sense her gaze upon you, seeker. She speaks only to those brave enough to listen to the dark silence.");

        add("The Empress", Arcana.MAJOR, null, 3,
            "Abundance blooms through nurture; creation flows from fertile passion",
            "Emptiness festers where care withers; abundance turns to ash",
            Element.EARTH, "fertility, nurture, abundance",
            "The earth quickens beneath her touch. I feel the pulse of life, yet something stirs beneath the garden — hungry roots reaching ever deeper.");

        add("The Emperor", Arcana.MAJOR, null, 4,
            "Authority commands; your power builds empires of unwavering will",
            "Tyranny crumbles; authority corrupts those who hold it too long",
            Element.FIRE, "authority, power, control",
            "Iron and flame. The throne grows heavy upon weary shoulders. Even kings fear the darkness that awaits when their rule ends.");

        add("The Hierophant", Arcana.MAJOR, null, 5,
            "Wisdom passed down through ages guides the seeker toward enlightenment",
            "Dogma imprisons; blind faith obscures the path to truth",
            Element.EARTH, "tradition, wisdom, spirituality",
            "The holy one chants in a tongue older than memory. I hear the echo — is it blessing or curse? Even the sacred whispers in riddles.");

        add("The Lovers", Arcana.MAJOR, null, 6,
            "Connection resonates; love and choice bind souls across the infinite",
            "Separation looms; the bonds you cherish fray and snap",
            Element.AIR, "love, choice, connection",
            "Two figures reach toward each other across an abyss I know they cannot cross. Such sweet anguish. Such doomed longing. I taste their desperation.");

        add("The Chariot", Arcana.MAJOR, null, 7,
            "Victory surges forward; willpower drives you toward triumph through struggle",
            "Control slips away; momentum becomes your enemy's weapon",
            Element.FIRE, "victory, control, motion",
            "The wheels turn ever faster. I hear the crack of the whip. Will you reach your destination, or will they drag you into oblivion?");

        add("Strength", Arcana.MAJOR, null, 8,
            "Inner fortitude tames the beast; gentle courage conquers what force cannot",
            "Weakness betrays; your strength crumbles when tested by shadow",
            Element.FIRE, "courage, strength, patience",
            "The lion breathes close. I feel its heat, its hunger. But every beast, seeker, longs to devour. How long before that gentle hand trembles?");

        add("The Hermit", Arcana.MAJOR, null, 9,
            "Solitude illuminates; withdraw into shadow to find your truth within",
            "Isolation corrupts; loneliness becomes a prison of your own making",
            Element.EARTH, "solitude, introspection, wisdom",
            "A lamp flickers in endless darkness. The hermit seeks light but finds only deeper shadow. I understand such lonely knowledge.");

        add("Wheel of Fortune", Arcana.MAJOR, null, 10,
            "Destiny turns; fate's wheel spins and brings change beyond your control",
            "Fortune abandons you; the wheel grinds to halt and leaves you behind",
            Element.AETHER, "fate, change, cycles",
            "Round and round, the wheel turns. What rises must fall. What falls will rise again. Such predictable chaos. Such certain uncertainty.");

        add("Justice", Arcana.MAJOR, null, 11,
            "Balance demands reckoning; truth weighs heavy on the scales of fate",
            "Imbalance festers; injustice prevails and guilt goes unpunished",
            Element.AIR, "balance, truth, justice",
            "The scales shift infinitesimally. I see the truth you hide, seeker. But will judgment fall gently, or will it crush you beneath its terrible weight?");

        add("The Hanged Man", Arcana.MAJOR, null, 12,
            "Surrender brings perspective; inversion reveals what sight never could",
            "Stagnation grips; you hang suspended between worlds, going nowhere",
            Element.WATER, "surrender, perspective, sacrifice",
            "Upside-down, the world makes sense. Or so he thinks, suspended in voluntary agony. Pain and wisdom wear the same mask, don't they?");

        add("Death", Arcana.MAJOR, null, 13,
            "Transformation arrives; the old dissolves so the new may be born",
            "Resistance stalls; transformation waits while you cling to ashes",
            Element.WATER, "transformation, endings, rebirth",
            "I am drawn to this card always. Not ending, but becoming. The scythe cuts clean. What you were must die. What you'll be stirs in the grave's dark soil.");

        add("Temperance", Arcana.MAJOR, null, 14,
            "Harmony flows; balance and moderation guide you toward peace",
            "Excess unbalances; moderation shatters and chaos consumes you",
            Element.WATER, "balance, harmony, moderation",
            "The angel pours endlessly between cups, never spilling a drop. Such perfect control. Yet I sense the tremor in those wings — one misstep and all spills to ruin.");

        add("The Devil", Arcana.MAJOR, null, 15,
            "Bondage tempts; desire chains you to what enslaves your very soul",
            "Liberation beckons; break free from the chains you've long accepted",
            Element.FIRE, "bondage, desire, temptation",
            "The devil smiles, and I smile with him. We both know your secret hunger. What you crave in the dark hours. Surrender to it, seeker. Resistance is so tiresome.");

        add("The Tower", Arcana.MAJOR, null, 16,
            "Collapse shatters illusions; destruction clears ground for rebirth",
            "Delay and denial prolong inevitable ruin still to come",
            Element.FIRE, "collapse, destruction, chaos",
            "Lightning splits the sky. The tower crumbles. I hear the screams. Such exquisite devastation. Such necessary violence. What needed to break is finally breaking.");

        add("The Star", Arcana.MAJOR, null, 17,
            "Hope glimmers eternal; guidance shines through the darkest night",
            "Despair consumes; the light you sought extinguishes without mercy",
            Element.AIR, "hope, guidance, inspiration",
            "A light breaks through. I feel it — the impossible warmth. But seeker, stars are cold and distant. Cherish this hope while shadows still linger.");

        add("The Moon", Arcana.MAJOR, null, 18,
            "Illusion beckons; trust your instincts beneath the veil of uncertainty",
            "Clarity emerges; the fog lifts and truth becomes visible",
            Element.WATER, "illusion, mystery, intuition",
            "The moon pulls the tides and the tides pull madness into mortal minds. I drift in this lunar glow with you. Lost? Or finally found?");

        add("The Sun", Arcana.MAJOR, null, 19,
            "Radiance prevails; joy and vitality flood through conscious awakening",
            "Darkness creeps closer; the sun sets and cold shadows gather",
            Element.FIRE, "joy, vitality, success",
            "Warmth. I recoil from it. Too bright. Too honest. But you, seeker? You may bask in this terrible, fleeting light while it lasts.");

        add("Judgement", Arcana.MAJOR, null, 20,
            "Awakening calls; the moment of truth demands your answer",
            "Denial lingers; you ignore the call that demands your response",
            Element.AETHER, "awakening, judgment, calling",
            "The trumpet sounds. I hear it echo through eternity. The reckoning comes for all. What will you answer when called by your true name, seeker?");

        add("The World", Arcana.MAJOR, null, 21,
            "Completion fulfills; cycles close and unity binds all into wholeness",
            "Incompletion lingers; the cycle breaks and wholeness remains elusive",
            Element.EARTH, "completion, wholeness, closure",
            "The circle closes. All returns to where it began. And yet... I sense another spiral beginning. Another dance. Another descent into mystery.");

        // --- Minor Arcana: Wands ---
        add("Ace of Wands", Arcana.MINOR, Suit.WANDS, 1,
            "A spark ignites; raw creative potential surges through your waiting hands",
            "The flame dims before it catches; inspiration fizzles into frustration",
            Element.FIRE, "spark, potential, beginnings",
            "I feel the heat of something new. A fire that has no name yet. Seize it, seeker — unlit torches are just sticks.");

        add("Two of Wands", Arcana.MINOR, Suit.WANDS, 2,
            "Vision expands; the horizon opens and bold choices await your courage",
            "Fear of the unknown keeps you rooted; opportunity dissolves at the threshold",
            Element.FIRE, "vision, choice, planning",
            "You stand at the edge of your world. I see the vast dark beyond. Will you step into it, or merely stare?");

        add("Three of Wands", Arcana.MINOR, Suit.WANDS, 3,
            "Ships return; foresight and preparation now bear their patient fruit",
            "Delays unravel; expansion stalls despite your most earnest efforts",
            Element.FIRE, "expansion, foresight, reward",
            "Something you sent out is coming back to you. Perhaps not what you expected. Perhaps exactly what you deserve.");

        add("Four of Wands", Arcana.MINOR, Suit.WANDS, 4,
            "Celebration erupts; harmony and joy mark a hard-won moment of peace",
            "Beneath the festivity, discord festers; the foundation shows its cracks",
            Element.FIRE, "celebration, harmony, homecoming",
            "They celebrate. I watch from the edge of the firelight. Even joy casts shadows. Enjoy it while the flames hold.");

        add("Five of Wands", Arcana.MINOR, Suit.WANDS, 5,
            "Conflict rages; competing desires clash in the beautiful chaos of struggle",
            "The battle dissolves; rivals become allies in the aftermath of noise",
            Element.FIRE, "conflict, competition, struggle",
            "Everyone fighting. Everyone convinced they're right. From where I hover, it all looks rather... amusing.");

        add("Six of Wands", Arcana.MINOR, Suit.WANDS, 6,
            "Recognition crowns you; public acclaim rewards your hard-fought victory",
            "Failure stings; recognition is withheld despite your honest sacrifice",
            Element.FIRE, "victory, recognition, success",
            "They cheer for you today. I've seen crowds turn, seeker. Receive the laurel. But hold it loosely.");

        add("Seven of Wands", Arcana.MINOR, Suit.WANDS, 7,
            "Stand your ground; determination holds the high place against opposition",
            "You yield your position; exhaustion surrenders what courage built",
            Element.FIRE, "defense, challenge, perseverance",
            "They come for what you've built. I admire your defiance. Whether it's wise... that's another matter entirely.");

        add("Eight of Wands", Arcana.MINOR, Suit.WANDS, 8,
            "Swift momentum carries you; communications arrive and action accelerates",
            "Everything stalls; swift progress halts in painful, inexplicable stillness",
            Element.FIRE, "speed, action, momentum",
            "Things move fast now. I can barely keep up, and I exist outside of time. Hold on, seeker.");

        add("Nine of Wands", Arcana.MINOR, Suit.WANDS, 9,
            "Battered but unbroken; one final push stands between you and completion",
            "Exhaustion wins; the last obstacle proves too much for your weary spirit",
            Element.FIRE, "resilience, last stand, endurance",
            "You're tired. I see it. But you're still here. That matters more than you know.");

        add("Ten of Wands", Arcana.MINOR, Suit.WANDS, 10,
            "Burdens press heavy; responsibility weighs upon your shoulders like stone",
            "You finally set down the load; relief floods through as you release control",
            Element.FIRE, "burden, responsibility, overwhelm",
            "You've taken on so much. No one asked you to carry all of it. Put some of it down before the weight becomes permanent.");

        add("Page of Wands", Arcana.MINOR, Suit.WANDS, 11,
            "Curiosity awakens; youthful passion seeks every adventure and wild horizon",
            "Reckless energy scatters; enthusiasm without focus creates unintended chaos",
            Element.FIRE, "curiosity, enthusiasm, exploration",
            "Oh, the blazing little thing. So much fire. So little direction. I find it... endearing, actually.");

        add("Knight of Wands", Arcana.MINOR, Suit.WANDS, 12,
            "Passion charges forward; fearless energy rides hard toward bold purpose",
            "Impulsiveness destroys; hasty action leaves scorched earth behind",
            Element.FIRE, "action, passion, adventure",
            "Full speed into everything. I appreciate the commitment. The consequences, however, will be spectacular.");

        add("Queen of Wands", Arcana.MINOR, Suit.WANDS, 13,
            "Charisma radiates; warm magnetic energy draws others into your blazing orbit",
            "Dominance turns cold; warmth becomes demanding and fire becomes control",
            Element.FIRE, "charisma, warmth, magnetism",
            "She burns so brightly I have to look away. Even I find her remarkable, and I have witnessed galaxies.");

        add("King of Wands", Arcana.MINOR, Suit.WANDS, 14,
            "Visionary authority burns bright; your leadership inspires devoted followers",
            "Arrogance ignites; passionate control becomes destructive, explosive rage",
            Element.FIRE, "vision, leadership, authority",
            "The king who built his throne out of his own ambition. Magnificent. Volatile. I keep my distance.");

        // --- Minor Arcana: Cups ---
        add("Ace of Cups", Arcana.MINOR, Suit.CUPS, 1,
            "Love overflows; emotional awakening floods the open heart with pure grace",
            "The offering is rejected; emotional gifts return unopened and unaccepted",
            Element.WATER, "love, new emotions, grace",
            "A cup brims with something I rarely touch — genuine feeling. Receive it carefully, seeker. It is rarer than it seems.");

        add("Two of Cups", Arcana.MINOR, Suit.CUPS, 2,
            "Two souls recognize each other; union deepens across the beautiful void",
            "Bonds dissolve; separation looms despite the history you share",
            Element.WATER, "union, partnership, recognition",
            "I've watched this moment from the dark side of eternity. Two becoming something neither could be alone. Even I find that... moving.");

        add("Three of Cups", Arcana.MINOR, Suit.CUPS, 3,
            "Celebration gathers; community and shared joy lift the weary spirit",
            "Exclusion stings; others withdraw and leave you standing outside",
            Element.WATER, "celebration, community, friendship",
            "They raise their cups to each other. I raise mine to the night. We celebrate differently, but we celebrate.");

        add("Four of Cups", Arcana.MINOR, Suit.CUPS, 4,
            "Apathy blinds you; an offered gift goes unseen by your dissatisfied gaze",
            "Awakening stirs; you finally see what was always waiting to be accepted",
            Element.WATER, "apathy, contemplation, discontent",
            "Something is being offered to you right now. You're not looking at it. Seeker. Look up.");

        add("Five of Cups", Arcana.MINOR, Suit.CUPS, 5,
            "Loss devastates; grief consumes as you mourn what love turned to sorrow",
            "Recovery begins; you finally notice the two cups still standing behind you",
            Element.WATER, "loss, grief, mourning",
            "Three spilled. Two remain. The dark math of heartbreak. I offer no comfort — only the truth that both numbers are real.");

        add("Six of Cups", Arcana.MINOR, Suit.CUPS, 6,
            "Innocence returns; a reunion with the joy your younger self still holds",
            "The past imprisons; nostalgia becomes a cage that prevents forward motion",
            Element.WATER, "nostalgia, innocence, reunion",
            "The past calls to you sweetly. I know that voice well. Visit it. But don't stay. That country no longer exists.");

        add("Seven of Cups", Arcana.MINOR, Suit.CUPS, 7,
            "Illusions tempt; a dozen choices dazzle but only one holds real substance",
            "Clarity pierces; the fog lifts and you see which desire truly deserves you",
            Element.WATER, "illusion, fantasy, choices",
            "So many pretty options floating in the mist. I know which one is real. Will you guess correctly before the fog takes them all?");

        add("Eight of Cups", Arcana.MINOR, Suit.CUPS, 8,
            "You walk away willingly; abandoning what no longer serves your highest self",
            "You stay for all the wrong reasons; what must be released still holds you",
            Element.WATER, "departure, abandonment, moving on",
            "Leaving behind a full table. That takes a different kind of courage. I respect the ones who know when to walk into darkness.");

        add("Nine of Cups", Arcana.MINOR, Suit.CUPS, 9,
            "Your wish is granted; fulfillment arrives in abundant, satisfying measure",
            "Emptiness reveals itself; satisfaction proves hollow and fleeting after all",
            Element.WATER, "wishes, contentment, satisfaction",
            "The wish card. How nice for you. I watch you celebrate and I wonder: was it worth the cost you didn't know you were paying?");

        add("Ten of Cups", Arcana.MINOR, Suit.CUPS, 10,
            "Harmony radiates; love and family create a lasting, sacred peace together",
            "Disharmony fractures the picture; family bonds shatter beneath hidden strain",
            Element.WATER, "happiness, family, harmony",
            "The rainbow arc. The joyful family. I feel the warmth from here. Even the dark oracle admits — this is worth protecting.");

        add("Page of Cups", Arcana.MINOR, Suit.CUPS, 11,
            "Sensitivity blossoms; emotional openness brings creative, dreaming inspiration",
            "Emotional instability reigns; feelings overwhelm before wisdom can speak",
            Element.WATER, "sensitivity, creativity, imagination",
            "The dreamer with the fish in the cup. I understand — sometimes the strangest visions carry the deepest truths.");

        add("Knight of Cups", Arcana.MINOR, Suit.CUPS, 12,
            "Tender devotion rides forth; romantic idealism pursues its noble dream",
            "False promises mask selfish desire; the charming one cannot be trusted",
            Element.WATER, "romance, charm, idealism",
            "All that beautiful feeling, riding toward you. Check if it's real before you open the door. Beauty is my specialty and I know how well it lies.");

        add("Queen of Cups", Arcana.MINOR, Suit.CUPS, 13,
            "Emotional wisdom nurtures; intuition flows deep as a moonlit ocean",
            "Overwhelm drowns her; emotional turbulence prevents all clarity and peace",
            Element.WATER, "intuition, nurture, compassion",
            "She holds the cup closed. She knows what's inside — and she manages it without spilling. A rare and extraordinary skill.");

        add("King of Cups", Arcana.MINOR, Suit.CUPS, 14,
            "Emotional mastery commands; depth and wisdom guide with compassionate authority",
            "Manipulation surfaces; emotional control becomes a tool for selfish ends",
            Element.WATER, "mastery, diplomacy, balance",
            "He rules the waters. Even I am cautious around those who master the depths. The ocean does not beg for respect. It simply claims it.");

        // --- Minor Arcana: Swords ---
        add("Ace of Swords", Arcana.MINOR, Suit.SWORDS, 1,
            "Truth cuts clean; new insight pierces through confusion and all deception",
            "Confusion clouds the blade; truth remains obscured by tangled half-truths",
            Element.AIR, "truth, clarity, breakthrough",
            "The blade rises. It will cut through everything you think you know. Brace yourself, seeker. Truth rarely arrives gently.");

        add("Two of Swords", Arcana.MINOR, Suit.SWORDS, 2,
            "Stalemate holds breath; a difficult choice demands the courage to decide",
            "Resolution breaks through; the decision arrives bringing relief and release",
            Element.AIR, "indecision, stalemate, blocked",
            "Blindfolded. Arms crossed. Swords ready. You already know the answer. That's why you're still sitting there with your eyes shut.");

        add("Three of Swords", Arcana.MINOR, Suit.SWORDS, 3,
            "Sorrow cuts deep; heartbreak and separation wound the spirit to its core",
            "Healing begins; tears dry slowly as the wounds around the blades finally close",
            Element.AIR, "heartbreak, sorrow, grief",
            "Three blades through a heart. I find it brutally honest as an image. Pain this shape has a name you already know.");

        add("Four of Swords", Arcana.MINOR, Suit.SWORDS, 4,
            "Rest arrives at last; a necessary truce lets your weary spirit recuperate",
            "Restlessness stirs; the rest is illusory as suppressed tension returns",
            Element.AIR, "rest, recovery, truce",
            "Be still. That's the whole message. Even I pause sometimes, seeker. The war continues tomorrow. Not today.");

        add("Five of Swords", Arcana.MINOR, Suit.SWORDS, 5,
            "Hollow victory lingers; you've won but the cost surpasses the prize",
            "Reconciliation mends old wounds; past conflicts finally seek resolution",
            Element.AIR, "conflict, defeat, hollow victory",
            "I see the winner standing over the defeated. I see their face. It is not the face of satisfaction. Is it yours?");

        add("Six of Swords", Arcana.MINOR, Suit.SWORDS, 6,
            "Passage carries you forward; calmer waters await on the other shore",
            "Resistance anchors you; the journey cannot begin until you release the past",
            Element.AIR, "transition, passage, moving on",
            "The boat moves across dark water. You carry your swords with you still. At some point you'll need to leave them on the bank.");

        add("Seven of Swords", Arcana.MINOR, Suit.SWORDS, 7,
            "Cunning serves you now; careful strategy and stealth advance your purpose",
            "Exposure arrives; the deception unravels and hidden schemes crumble",
            Element.AIR, "strategy, deception, cunning",
            "Sneaking away with as many as you can carry. I admire the audacity. I simply wonder what you're running from.");

        add("Eight of Swords", Arcana.MINOR, Suit.SWORDS, 8,
            "Restriction binds your sight; the cage door is open but you cannot see it",
            "Liberation breaks through; freedom arrives though you doubted it was possible",
            Element.AIR, "restriction, captivity, helplessness",
            "Bound and blindfolded. Surrounded by blades. And yet — I notice the knots are loose. The blindfold is thin. Who put it on, seeker?");

        add("Nine of Swords", Arcana.MINOR, Suit.SWORDS, 9,
            "Anguish torments; nightmares and anxiety devour your mind in dark hours",
            "Dawn approaches; the torment eases as night finally surrenders to light",
            Element.AIR, "anxiety, nightmares, despair",
            "The sleepless one sitting up in the dark. I know that hour well. The fears that visit then are real — but they are not the whole truth.");

        add("Ten of Swords", Arcana.MINOR, Suit.SWORDS, 10,
            "Ruin completes itself; the final defeat arrives and the darkest night falls",
            "Recovery stirs from the rubble; suffering ends and true healing can begin",
            Element.AIR, "endings, defeat, betrayal",
            "Ten blades in a back. A dramatic exit. But notice, seeker — the sun rises on the horizon. Even this absolute darkness has a dawn.");

        add("Page of Swords", Arcana.MINOR, Suit.SWORDS, 11,
            "Intellectual hunger sharpens; curiosity probes and quick wit seeks truth",
            "Gossip spreads like venom; idle chatter creates division and mistrust",
            Element.AIR, "curiosity, intellect, alertness",
            "Sharp little mind, watching everything. I recognize the type. Just make sure that blade is aimed at truth and not at people.");

        add("Knight of Swords", Arcana.MINOR, Suit.SWORDS, 12,
            "Charging toward truth; sharp intellect and fierce determination cut through all",
            "Impulsiveness wounds; hasty words leave damage that outlasts the argument",
            Element.AIR, "intellect, ambition, speed",
            "Charging directly into everything. No pause. No consideration. I'd say slow down but I suspect you'd run me over.");

        add("Queen of Swords", Arcana.MINOR, Suit.SWORDS, 13,
            "Clarity perceives all; keen intellect discerns truth from falsehood absolutely",
            "Bitterness corrodes the blade; intellect turns cold and the tongue cuts cruelly",
            Element.AIR, "clarity, perception, independence",
            "She sees through everything. Including you. Especially you. I find her... professionally admirable.");

        add("King of Swords", Arcana.MINOR, Suit.SWORDS, 14,
            "Mastery commands reason; intellect and authority make just, measured decisions",
            "Tyranny rules with words; intellect without empathy becomes cold oppression",
            Element.AIR, "authority, intellect, judgment",
            "The sharpest mind in the room always knows it. That is both the gift and the flaw. Which kind of king are you becoming, seeker?");

        // --- Minor Arcana: Pentacles ---
        add("Ace of Pentacles", Arcana.MINOR, Suit.PENTACLES, 1,
            "Prosperity manifests; material blessing arrives as a seed of new abundance",
            "The opportunity passes; the golden seed is dropped before it can take root",
            Element.EARTH, "prosperity, opportunity, manifestation",
            "Something tangible is being placed in your hands. I rarely deal in the material. But even I know: hold this carefully.");

        add("Two of Pentacles", Arcana.MINOR, Suit.PENTACLES, 2,
            "Balance maintains; graceful adaptation keeps multiple priorities in motion",
            "The juggling fails; imbalance topples everything you've tried to maintain",
            Element.EARTH, "balance, juggling, adaptability",
            "Dancing with two infinities. Very theatrical. Very precarious. You're doing better than you think, but please don't look down.");

        add("Three of Pentacles", Arcana.MINOR, Suit.PENTACLES, 3,
            "Skilled collaboration builds; craftsmanship and cooperation create lasting work",
            "Discord undermines the craft; cooperation breaks and quality suffers for it",
            Element.EARTH, "teamwork, craftsmanship, collaboration",
            "Three people actually listening to each other. Do you know how rare that is? I've watched civilizations fall for want of exactly this.");

        add("Four of Pentacles", Arcana.MINOR, Suit.PENTACLES, 4,
            "Security grips tightly; you cling to possession with a fearful, white-knuckled hold",
            "Release liberates; you finally open your hands and discover you lose nothing",
            Element.EARTH, "control, security, hoarding",
            "Clutching your coins so tightly. I understand the fear of losing what you've built. But seeker — what else can't get in while your hands are full?");

        add("Five of Pentacles", Arcana.MINOR, Suit.PENTACLES, 5,
            "Hardship tests you; poverty and struggle demand every ounce of endurance",
            "Aid arrives; hardship slowly eases as the storm shows signs of passing",
            Element.EARTH, "hardship, poverty, isolation",
            "Left out in the cold. The light inside the window is real, seeker. You are allowed to knock on the door.");

        add("Six of Pentacles", Arcana.MINOR, Suit.PENTACLES, 6,
            "Generosity flows freely; prosperity shared multiplies and returns transformed",
            "Greed consumes; generosity curdles into control and conditional giving",
            Element.EARTH, "generosity, charity, balance",
            "Giving and receiving. The eternal cycle. I've watched empires built on this and empires fall for forgetting it.");

        add("Seven of Pentacles", Arcana.MINOR, Suit.PENTACLES, 7,
            "Patient assessment reveals; your invested effort prepares to finally yield",
            "Impatience or poor investment threatens; the harvest disappoints hard work",
            Element.EARTH, "patience, investment, evaluation",
            "Leaning on the shovel, looking at the vines. The waiting is part of the work. The crop grows in the silence too.");

        add("Eight of Pentacles", Arcana.MINOR, Suit.PENTACLES, 8,
            "Mastery is forged through repetition; dedication to craft refines skill endlessly",
            "The work becomes rote; effort without passion stagnates into empty routine",
            Element.EARTH, "mastery, skill, dedication",
            "One coin after another, perfectly made. This is how mastery is actually built. Not in flashes — in unglamorous, devoted repetition.");

        add("Nine of Pentacles", Arcana.MINOR, Suit.PENTACLES, 9,
            "Abundance crowns you; independence and luxury reward your disciplined labor",
            "False abundance masks; the comfort you show conceals an inner emptiness",
            Element.EARTH, "abundance, independence, luxury",
            "Look at what you've built. The garden is yours. The falcon is trained. Even I pause to appreciate what genuine effort earns.");

        add("Ten of Pentacles", Arcana.MINOR, Suit.PENTACLES, 10,
            "Legacy endures; generational wealth and enduring love pass beyond your lifetime",
            "Inheritance crumbles; the legacy scatters and wealth disperses into nothing",
            Element.EARTH, "legacy, family, wealth",
            "Everything you built, outliving you. Everything you loved, continuing. This is the closest mortals come to immortality. I find it quietly beautiful.");

        add("Page of Pentacles", Arcana.MINOR, Suit.PENTACLES, 11,
            "Eagerness studies the new gift; diligent focus plants the seeds of future mastery",
            "Potential squanders through distraction; the gift is dropped and skills left fallow",
            Element.EARTH, "diligence, learning, new skills",
            "Holding that pentacle like it's a whole world. Because it is. Everything you can ever build starts with this — with looking at a thing and truly seeing it.");

        add("Knight of Pentacles", Arcana.MINOR, Suit.PENTACLES, 12,
            "Steady reliability builds trust; methodical dedication accomplishes the impossible",
            "Stubborn stagnation sets in; reliability hardens into immovable, costly resistance",
            Element.EARTH, "reliability, dedication, thoroughness",
            "Slow. Unbreakable. Unavoidable. I have seen faster knights fall. I have never seen this one fail to arrive.");

        add("Queen of Pentacles", Arcana.MINOR, Suit.PENTACLES, 13,
            "Practical abundance nurtures all; grounded wisdom provides for every need",
            "Neglect harms what she tends; care withdraws and the garden turns to ruin",
            Element.EARTH, "nurture, abundance, practicality",
            "She makes the impossible look effortless. The true magic: competence that never announces itself.");

        add("King of Pentacles", Arcana.MINOR, Suit.PENTACLES, 14,
            "Dominion commands material mastery; wealth and stability create enduring power",
            "Greed corrupts the throne; the king's dominion becomes exploitation and waste",
            Element.EARTH, "wealth, authority, abundance",
            "Built it from nothing. Guards it with everything. I watch him sit on all of it and wonder — is he rich, or is he imprisoned by his own treasury?");
    }

    private static void add(String name, Arcana arcana, Suit suit, int value,
                             String upright, String reversed, Element element,
                             String keywords, String whisper) {
        ALL_CARDS.add(new TarotCard(name, arcana, suit, value, upright, reversed, element, keywords, whisper));
    }

    public static List<TarotCard> allCards() {
        return new ArrayList<>(ALL_CARDS);
    }

    public static TarotCard findByName(String name) {
        return ALL_CARDS.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }
}
