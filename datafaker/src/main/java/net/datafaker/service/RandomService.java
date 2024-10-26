package net.datafaker.service;

import java.util.Objects;
import java.util.Random;

public class RandomService {
    private static final char[] HEX_UP = "0123456789ABCDEF".toCharArray();
    private static final char[] HEX_LOWER = "0123456789abcdef".toCharArray();
    private static final Random SHARED_RANDOM = new Random();
    private final Random random;

    /**
     * Uses a default shared random.
     */
    public RandomService() {
        this(SHARED_RANDOM);
    }

    /**
     * @param random If null is passed in, a default Random is assigned
     */
    public RandomService(Random random) {
        this.random = random != null ? random : SHARED_RANDOM;
    }

    @SuppressWarnings("unused")
    public int nextInt() {
        return random.nextInt();
    }

    public int nextInt(int maxExclusive) {
        return random.nextInt(maxExclusive);
    }

    public Integer nextInt(int minInclusive, int maxInclusive) {
        if (minInclusive > maxInclusive)
            throw new IllegalArgumentException("Min (%s) > Max (%s)".formatted(minInclusive, maxInclusive));
        if (maxInclusive + 1 < 0)
            return (int) nextLong(minInclusive, maxInclusive);

        return random.nextInt(minInclusive, maxInclusive + 1);
    }

    @SuppressWarnings("unused")
    public float nextFloat() {
        return random.nextFloat();
    }

    public long nextLong() {
        return random.nextLong();
    }

    public long nextLong(long maxInclusive) {
        if (maxInclusive <= 0) {
            throw new IllegalArgumentException("bound must be positive: " + maxInclusive);
        }

        long bits, val;
        do {
            long randomLong = random.nextLong();
            bits = (randomLong << 1) >>> 1;
            val = bits % maxInclusive;
        } while (bits - val + (maxInclusive - 1) < 0L);
        return val;
    }

    /**
     * A random long value within given range.
     * If {@code min == max} then method always returns {@code min}.
     * Otherwise, {@code max} is exclusive.
     *
     * @param min lower bound (inclusive)
     * @param max upper bound (exclusive in most case)
     * @return a random long value between {@code min} and {@code max}
     */
    public long nextLong(long min, long max) {
        return min == max ?
            min :
            random.nextLong(min, max);
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public double nextDouble(double min, double max) {
        return min + (nextDouble() * (max - min));
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    public byte[] nextRandomBytes(int numberOfBytes) {
        final byte[] randomBytes = new byte[numberOfBytes];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    public String hex() {
        return hex(8);
    }

    public String hex(int length) {
        return hex(length, true);
    }

    public String hex(int length, boolean upper) {
        if (length <= 0) {
            return ""; // Keep the existing behavior instead of throwing an error.
        }
        char[] hexArray = upper ? HEX_UP : HEX_LOWER;
        final char[] hexChars = new char[length];
        final byte[] randomBytes = nextRandomBytes(length);
        for (int i = 0; i < length; i++) {
            hexChars[i] = hexArray[((char) randomBytes[i]) % hexArray.length];
        }
        return new String(hexChars);
    }

    public Random getRandomInternal() {
        return random;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RandomService that)) return false;

        return Objects.equals(random, that.random);
    }

    @Override
    public int hashCode() {
        if (random == SHARED_RANDOM) return 1;
        return random != null ? random.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + random;
    }
}