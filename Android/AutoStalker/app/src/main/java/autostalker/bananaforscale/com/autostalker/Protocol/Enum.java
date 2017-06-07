package autostalker.bananaforscale.com.autostalker.Protocol;

/**
 * Created by Emiliano on 03/06/2017.
 */

public  class Enum {
    public static enum tipoMensaje{
        Movement(1),
        Settings(2),
        ReturnCommand(3),
        ShutDownCommand(4),
        CameraShutDownCommand(5),
        ObstacleDetected(6),
        BatteryLevell(7),
        Position(8),
        RaspberryError(9),
        Ping(10),
        PingAnswer(11)
        ;

        public final int value;
        private tipoMensaje(int val){
            this.value = val;
        }

        @Override
        public String toString(){
            return String.valueOf(value);
        }

    }
}
