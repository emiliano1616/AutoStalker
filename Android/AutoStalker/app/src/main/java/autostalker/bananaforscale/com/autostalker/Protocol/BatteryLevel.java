package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public class BatteryLevel extends BaseProtocol {
    private final Enum.messageType messageType = Enum.messageType.BatteryLevel;
    public int batteryLevel;
}
