package com.svxf.legitvisuals.utils.pair;

import java.io.Serializable;
import java.util.Objects;

public abstract class Pair<A, B> implements Serializable {

    public static <A, B> Pair<A, B> of(A a, B b) { return ImmutablePair.of(a, b); }

    public abstract A getFirst();

    public abstract B getSecond();

    @Override
    public int hashCode() {
        return Objects.hash(getFirst(), getSecond());
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if (that instanceof Pair<?, ?> other) {
            return Objects.equals(getFirst(), other.getFirst()) && Objects.equals(getSecond(), other.getSecond());
        }
        return false;
    }
}