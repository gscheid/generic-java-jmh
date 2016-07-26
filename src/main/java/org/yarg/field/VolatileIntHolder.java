package org.yarg.field;

public class VolatileIntHolder {
    public volatile int value;

    public VolatileIntHolder(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }
}
