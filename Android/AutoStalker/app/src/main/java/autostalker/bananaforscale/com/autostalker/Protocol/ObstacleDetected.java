package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public class ObstacleDetected extends BaseProtocol {
    private final Enum.messageType messageType = Enum.messageType.ObstacleDetected;
    public int side;
}
