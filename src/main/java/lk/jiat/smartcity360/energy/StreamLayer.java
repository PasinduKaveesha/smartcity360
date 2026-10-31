package lk.jiat.smartcity360.energy;

/**
 * Abstract decorator wrapping a MeterStream instance.
 */
public abstract class StreamLayer implements MeterStream {

    protected final MeterStream inner;

    public StreamLayer(MeterStream inner) {
        if (inner == null) {
            throw new IllegalArgumentException("Inner stream cannot be null");
        }
        this.inner = inner;
    }

    public MeterStream getInner() {
        return inner;
    }
}
