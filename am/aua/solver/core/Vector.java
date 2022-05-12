package am.aua.solver.core;

import java.util.Arrays;

public class Vector {

    private float[] vector;

    // no-arg constructor
    public Vector(int size) {
        this.vector = new float[size];
        Arrays.fill(vector, 0);
    }

    // constructor
    public Vector(float[] arr) {
        setVector(arr);
    }

    // copy constructor
    public Vector(Vector vector) {
        setVector(vector.getVector());
    }

    // accessor
    public float[] getVector() {
        return vector;
    }

    // mutator
    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public void add(int i, float value) {
        this.vector[i] += value;
    }

    // equals method
    public boolean equals(Object object) {
        if (!(object instanceof Vector))
            return false;
        else
            return this.getVector() == ((Vector) object).getVector();
    }

    // toString method
    public String toString(Vector vector) {
        String output = "";

        for (int i = 0; i < vector.getVector().length; i++) {
            output += getVector()[i] + " ";
        }

        return output;
    }

}
