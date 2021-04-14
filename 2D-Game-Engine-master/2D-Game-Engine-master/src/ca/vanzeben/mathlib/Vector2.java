package ca.vanzeben.mathlib;

import java.awt.*;

/*
Author: David J Bowen
Rework: None
File:  Mathlib
Descr: Some Vector math and a Sphere Collision test.

 */
public class Vector2 {

    public double x, y;
    public Vector2() { x = 0; y = 0; }
    public Vector2(double x, double y) { this.x = x; this.y = y; }
    public Vector2(Vector2 other) { copy(other); }
    public Vector2(Point point) { this.x = point.x; this.y = point.y; }
    public boolean isZero() { return (this.x == 0 && this.y == 0); }
    @Override
    public boolean equals(Object obj) {
        Vector2 v;
        if(!(obj instanceof Vector2)){
            return false;
        }
        v = (Vector2)obj;
        return (this.x == v.x && this.y == v.y);
    }

    /**
     *  is i the index of a component?
     * @return
     */
    public boolean validComponent(int i) {
        return (i >= 0 && i < 2);
    }

    /**
     * i-th element;
     * @return
     */
    public double at(int i) {
        if(i == 0) {
            return this.x;
        }
        return this.y;
    }

    public double x() { return  this.x; }
    public double y() { return  this.y; }

    @Override
    public Object clone() {
        try {
            Object o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Vector2(this);
    }

    public double dotProduct(double ox, double oy) {
        return ((this.x * ox) + (this.y * oy));
    }
    public double dotProduct(Vector2 other) {
        return ((this.x * other.x) + (this.y * other.y));
    }

    /**
     * Magnitude
     * @return
     */
    public double length() { return Math.sqrt((this.x*this.x) + (this.y*this.y)); }

    public double distance(Vector2 vother) {
        double vx = vother.x() - this.x();
        double vy = vother.y() - this.y();
        return Math.sqrt(vx*vx + vy*vy);
    }

    /**
     * Assign value to all elements
     * @return
     */
    public void fill(double value) { this.x = this.y = value; }

    /**
     * Explicitly set the i-th component to value
     * @return
     */

    public void set(int i , double value) {
        if (i == 0)
            this.x = value;
        else
            this.y = value;
    }
    public void set(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    public void sub(Vector2 other)
    {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void mult(Vector2 other)
    {
        this.x *= other.x;
        this.y *= other.y;
    }

    public void scalarAdd(double value) {
        this.x += value;
        this.y += value;
    }

    public void scalarSub(double value) {
        this.x -= value;
        this.y -= value;
    }

    public void scalarMult(double value) {
        this.x *= value;
        this.y *= value;
    }

    public void scalarDiv(double value) {
        if(value != 0) {
            double rsp = 1.0d / value;
            this.x *= rsp;
            this.y *= rsp;
        } else
        {
            this.x = this.y = 0; // no solution?
        }
    }

    public double SphereCollision(Vector2 vA, Vector2 vB, double rA, double rB) {
        double distance = vA.distance(vB);
        double rSum = (rA + rB);
        boolean Test = true;
        if(rSum == distance) {
            if(Test)
                System.out.println("We are just touching, but not penetrating. : " + (rSum - distance));
        } else if(rSum > distance) {
            if (Test)
                System.out.println("We have passed though object, we should step back collision: " + (rSum - distance));
        } else if (rSum < distance) {
            System.out.println("We aren't penetrating or touching anything: " + (rSum - distance));
        }

        if(Test)
            System.out.println("Sum of rA and rB: " + (rA + rB) + " distance : " + distance);
        return  distance;
    }


    public double normalize() {
        double len = length();
        if(len != 0) {
            this.x /= len;
            this.y /= len;
        }
        return len;
    }
    public void copy(Vector2 other) {
         this.x = other.x;
         this.y = other.y;
    }

    @Override
    public String toString() {
        return "[" + this.x + '|' + this.y + '|';
    }

    /*
    @Override
    public void show() {
    }
*/
}
