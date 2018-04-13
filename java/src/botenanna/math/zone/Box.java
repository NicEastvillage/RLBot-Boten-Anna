package botenanna.math.zone;

import botenanna.math.Vector3;


public class Box {
    private Vector3 boxCoordinates1;
    private Vector3 boxCoordinates2;
    private double Lowerpoint_x;
    private double Lowerpoint_y;
    private double Lowerpoint_z;
    private double Maxpoint_x;
    private double Maxpoint_y;
    private double Maxpoint_z;


    public Box(Vector3 boxCoordinates1, Vector3 boxCoordinates2) {
        this.boxCoordinates1 = boxCoordinates1;
        this.boxCoordinates2 = boxCoordinates2;

        this.Lowerpoint_x = Math.min(boxCoordinates1.x, boxCoordinates2.x);
        this.Lowerpoint_y = Math.min(boxCoordinates1.y, boxCoordinates2.y);
        this.Lowerpoint_z = Math.min(boxCoordinates1.z, boxCoordinates2.z);
        this.Maxpoint_x = Math.max(boxCoordinates1.x, boxCoordinates2.x);
        this.Maxpoint_y = Math.max(boxCoordinates1.y, boxCoordinates2.y);
        this.Maxpoint_z = Math.max(boxCoordinates1.z, boxCoordinates2.z);

    }


    public boolean isPointInBoxArea(Vector3 givenPoint) {

        if (Lowerpoint_x < givenPoint.x && givenPoint.x < Maxpoint_x){
            if (Lowerpoint_y < givenPoint.y && givenPoint.y < Maxpoint_y){
                return Lowerpoint_z < givenPoint.z && givenPoint.z < Maxpoint_z;
            }
        }
        return false;
    }
}