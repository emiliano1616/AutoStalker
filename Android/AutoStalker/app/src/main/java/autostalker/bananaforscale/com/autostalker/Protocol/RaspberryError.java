package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public class RaspberryError extends BaseProtocol {
    private final Enum.messageType messageType = Enum.messageType.RaspberryError;
    public String message;
    public String stackTrace;
}
