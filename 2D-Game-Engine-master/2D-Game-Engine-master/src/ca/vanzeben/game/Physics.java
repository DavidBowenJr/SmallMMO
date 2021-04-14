package ca.vanzeben.game;

import ca.vanzeben.mathlib.Vector2;

public class Physics {
    public boolean hasCollision;
    public boolean hasJustTouched;


  //  @Override
    public double SC(Vector2 vA, Vector2 vB, double rA, double rB) {
        double distance = vA.distance(vB);
        double rSum = (rA + rB);
        boolean Test = true;

        if(rSum == distance) {
            hasCollision = false;
            hasJustTouched = true;
            if(Test)
                System.out.println("We are just touching,  but not penetrating. : " + (rSum - distance));
        } else if(rSum > distance) {
            hasCollision = true;
            hasJustTouched = false;
            if(Test) {
                //Audio Fires Off here
                //   File Clap = new File("clap.wav");
           //     PlaySound(Clap);
                System.out.println("We Have Passed Though object, we should step back collision :" + (rSum - distance));
            }
        } else if(rSum < distance) {
            hasCollision = false;
            hasJustTouched = false;
            if(Test)
                System.out.println("We aren't penetrating or touching anything. : " + (rSum - distance));
        }

        if(Test)
            System.out.println("Sum of rA and rB : " + (rA + rB)  + " distance : " + distance);

        return distance;
    }

}
