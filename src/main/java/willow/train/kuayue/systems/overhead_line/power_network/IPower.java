package willow.train.kuayue.systems.overhead_line.power_network;

public interface IPower {

    float getMaxVoltage();
    float getMaxCurrent();
    float getMaxPower();
    boolean isOverloaded(float voltage, float current);
}
