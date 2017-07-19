package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public class Movement extends BaseProtocol {
    private final Enum.messageType messageType = Enum.messageType.Movement;
    public int angle;
    public int power;

}
