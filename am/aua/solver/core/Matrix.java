package am.aua.solver.core;
import java.util.Arrays;

public class Matrix {

    private float[][] matrix;

    // no-arg constructor
    public Matrix(int size) {
        this.matrix = new float[size][size];
        for (float[] floats : matrix)
            Arrays.fill(floats, 0);
    }

    // constructor
    public Matrix(float[][] arr) {
        setMatrix(arr);
    }

    // copy constructor
    public Matrix(Matrix matrix) {
        setMatrix(matrix.getMatrix());
    }

    // accessor
    public float[][] getMatrix() {
        return matrix;
    }

    // mutator
    public void setMatrix(float[][] matrix) {
        this.matrix = matrix;
    }

    public void add(int i, int j, float value) {
        this.matrix[i][j] += value;
    }

    // equals method
    public boolean equals(Object object) {
        if (!(object instanceof Matrix))
            return false;
        else
            return this.getMatrix() == ((Matrix) object).getMatrix();
    }

    // toString method
    public String toString() {
        String output = "";

        for (int i = 0; i < this.getMatrix().length; i++) {
            for (int j = 0; j < this.getMatrix().length; j++) {
                output += getMatrix()[i][j] + " ";
            }
            output += "\n";
        }

        return output;
    }

}
