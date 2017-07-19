package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public class Position extends BaseProtocol {
    private final Enum.messageType messageType = Enum.messageType.Position;
    public double x;
    public double y;
    public float radio;
}
