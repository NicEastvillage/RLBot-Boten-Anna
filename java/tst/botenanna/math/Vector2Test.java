package botenanna.math;


import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2Test {

    @Test
    public void Vector2TurnTest1() {
    double inputRadian = Math.PI/30;
    Vector2 direction = new Vector2(-123,450);
    Vector2 test = new Vector2(-169.36,434.6778519);

    Vector2 turned = direction.turn(inputRadian);
    assertEquals(0, turned.minus(test).getMagnitude(), 1E-2);
    }
    @Test
    public void Vector2TurnTest2() {
    Vector2 direction = new Vector2(8,25);
    Vector2 test = new Vector2(-8,-25);
    double inputRadian = Math.PI;

    assertEquals(0, direction.turn(inputRadian).minus(test).getMagnitude(), 1E-8);
    }


}
