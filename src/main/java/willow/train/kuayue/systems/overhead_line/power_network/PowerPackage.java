package willow.train.kuayue.systems.overhead_line.power_network;

public class PowerPackage {
    private boolean overload;
    private float voltage, current;
    public static final PowerPackage EMPTY = new PowerPackage(0, 0);

    public PowerPackage(float voltage, float current) {
        this.voltage = voltage;
        this.current = current;
        overload = false;
    }

    public float getPower() {
        return current * voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getVoltage() {
        return voltage;
    }

    public float getCurrent() {
        return current;
    }

    public boolean isOverload() {
        if (overload) return true;
        overload = current < 0 || voltage < 0;
        return overload;
    }
}
