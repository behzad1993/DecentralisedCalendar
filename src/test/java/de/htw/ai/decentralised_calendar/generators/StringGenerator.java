//package de.htw.ai.decentralised_calendar.generators;
//
//
//import com.pholser.junit.quickcheck.generator.GenerationStatus;
//import com.pholser.junit.quickcheck.generator.Generator;
//import com.pholser.junit.quickcheck.random.SourceOfRandomness;
//
//import java.util.concurrent.ThreadLocalRandom;
//
//
///**
// * @author kenny on 8-5-17.
// */
//
//public class StringGenerator extends Generator<String> {
//
//    public static final int CAPACITY_MIN = 10;
//    public static final int CAPACITY_MAX = 50;
//    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
//    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//    private static final String NUMBERS = "0123456789";
//    private static final String SPECIAL_CHARS = ".-\\;:_@[]^/|}{";
//    private static final String SPACE = "                 ";
//    private static final String NEWLINE = "\n\n\n\n";
//    private static final String ALL_MY_CHARS = LOWERCASE_CHARS + UPPERCASE_CHARS + NUMBERS + SPACE + SPECIAL_CHARS + NEWLINE;
//
//
//    public StringGenerator() {
//        super(String.class);
//    }
//
//
//    public String generate(final SourceOfRandomness random, final GenerationStatus status) {
//        final int lengthOfString = ThreadLocalRandom.current().nextInt(CAPACITY_MIN, CAPACITY_MAX);
//        final StringBuilder sb = new StringBuilder(lengthOfString);
//        for (int i = 0; i < lengthOfString; i++) {
//            final int randomIndex = random.nextInt(ALL_MY_CHARS.length());
//            sb.append(ALL_MY_CHARS.charAt(randomIndex));
//        }
//        return sb.toString();
//    }
//}
//
