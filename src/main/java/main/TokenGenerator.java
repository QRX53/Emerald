package main;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class TokenGenerator {
    
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public TokenGenerator(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public TokenGenerator(int length, Random random) {
        this(length, random, alphanum);
    }

    public TokenGenerator(int length) {
        this(length, new SecureRandom());
    }

    public TokenGenerator() {
        this(21);
    }

    @Override
    public String toString() {
        return "TokenGenerator { " +
                "random = " + random +
                ", symbols = " + Arrays.toString(symbols) +
                ", buf = " + Arrays.toString(buf) +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenGenerator)) return false;
        TokenGenerator that = (TokenGenerator) o;
        return random.equals(that.random) && Arrays.equals(symbols, that.symbols) && Arrays.equals(buf, that.buf);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(random);
        result = 31 * result + Arrays.hashCode(symbols);
        result = 31 * result + Arrays.hashCode(buf);
        return result;
    }
}