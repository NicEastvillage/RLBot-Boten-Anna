package botenanna.math.zone;


import botenanna.game.Situation;
import botenanna.math.Vector3;


public class Box{

    private Vector3 FIRSTCORDINAT;
    private Vector3 SEKUNDDINAT;
    private Vector3 Position;

    public Box(Vector3 Position, Vector3 FIRSTCORDINAT, Vector3 SEKUNDDINAT) {

    this.FIRSTCORDINAT =FIRSTCORDINAT;
    this.SEKUNDDINAT = SEKUNDDINAT;
    this.Position = Position;

    }

    public boolean ballBox() {

        return (Position.x >= FIRSTCORDINAT.x && Position.x <= SEKUNDDINAT.x && Position.y >= FIRSTCORDINAT.y && Position.y <= SEKUNDDINAT.y && Position.z >= FIRSTCORDINAT.z && Position.z <= SEKUNDDINAT.z);


    }
}
