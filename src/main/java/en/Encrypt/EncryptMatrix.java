package Encrypt;
import java.util.Random;

public class EncryptMatrix {



    /**
    * Main function to generate a matrix suich taht det%27 != 0
    *
    * @param size the size of the matrix for the batches  
    * @param seed the sed for random numbers in the matrix
    * @return the generated matrix
    */
    private static float[][] generateMatrix(int size, long seed) {
        Random rand = new Random(seed);
        float[][] matrix = new float[size][size];

        // Fill the matrix with random integer values
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (float)(rand.nextInt(10) + 1); // Adding 1 to avoid zero values
            }
        }

        return matrix;
    }



    /**
    * Function that calculates the determinant of a fmatrix
    *
    * @param matrix  the matrix
    * @return the determinant
    */
    private static float det(float[][] matrix) {
        int size = matrix.length;

        // Base case: if the matrix is 2x2
        if (size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        int det = 0;
        for (int i = 0; i < size; i++) {
            // Calculate the determinant using cofactor expansion
            det += Math.pow(-1, i) * matrix[0][i] * det(getSubMatrix(matrix, 0, i));
        }

        return det;
    }



    /**
    * Main function to get the submatrix for the determinant
    * @param matrix
    * @param rowToRemove
    * @param colToRemove
    * @return the generated matrix
    */
    private static float[][] getSubMatrix(float[][] matrix, int rowToRemove, int colToRemove) {
        int size = matrix.length - 1;
        float[][] subMatrix = new float[size][size];

        for (int i = 0, newRow = 0; i < matrix.length; i++) {
            if (i == rowToRemove) {continue;}
            
            for (int j = 0, newCol = 0; j < matrix.length; j++) {
                if (j == colToRemove) {
                    continue;
                }

                subMatrix[newRow][newCol] = matrix[i][j];
                newCol++;
            }
            newRow++;
        }
        return subMatrix;
    }




    /**
    * Main function to generate a matrix suich taht det%27 != 0
    *
    * @param size the size of the matrix for the batches  
    * @param seed the sed for random numbers in the matrix
    * @return the generated matrix
    */
    public static float[][] createEncryptionMatrix(long seed, int matrixSize) {



        float[][] resultMatrix;
        do {
            float[][] matrix = generateMatrix(matrixSize, seed);
            float det = det(matrix);

            // Check if determinant is not divisible by 27
            if (det != 0) {
                resultMatrix = matrix;
            } else {
                System.out.println("14");
                resultMatrix = null;
            }
        } while (resultMatrix == null);


        return resultMatrix;

    }



    /**
    * Function to calculate the inverse of the encryption mattrix
    *
    * @param matrix the original matrix
    * @return the inverse of the matriz
    */
    public static float[][] inverseMatrix(float[][] matrix) {


        int n = matrix.length;
        float[][] augmentedMatrix = new float[n][2 * n];

        // Augment the matrix with an identity matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrix[i][j];
                augmentedMatrix[i][j + n] = (i == j) ? 1.0f : 0.0f;
            }
        }



        // Apply Gauss-Jordan elimination
        for (int i = 0; i < n; i++) {
            // Make the diagonal element 1
            double pivot = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= pivot;
            }

            // Eliminate other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }



        // Extract the inverse matrix from the augmented matrix
        float[][] inverse = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = augmentedMatrix[i][j + n];
            }
        }

        return inverse;
    }

}
