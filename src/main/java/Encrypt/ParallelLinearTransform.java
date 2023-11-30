package Encrypt;

class ParallelLinearTransform extends Thread {
    
    
    private final float[][] matrixEncoder; //The matrix to decode or encode
    private float[][] ASCIIValues; //Array with the asccii 
    private float[] resultVector; // The result of matrix multiplication
    private String result;  // The resutant string
    private String toDecode; 

    
    //Constructor of the class
    public ParallelLinearTransform(float[][] Decoder,  String toDecode) {
        this.matrixEncoder = Decoder;
        this.toDecode = toDecode; 
        this.result = "";
    }




    /**
    * Main function to ccall the other process
    */
    @Override
    public void run() {
        if(this.toDecode == null)return;
        convertStringToAsciiArray();
        matrixProduct();
        this.result = asciiToString(resultVector);
    }



    /**
    * Getter of the result
    * 
    * @return the resultant string
    */
    public String getResult()
    {
        return this.result;
    }



    


    /**
    * Function to convert the elements of an string into array of ascii values
    */
    private void convertStringToAsciiArray() {


        int length = this.toDecode.length();
        float[] asciiArray = new float[length];

        for (int i = 0; i < length; i++) {
            char character = this.toDecode.charAt(i);
            asciiArray[i] = (float)((int) character);
        }

        this.ASCIIValues = new float[][] {asciiArray};

    }



    
    public static String asciiToString(float[] asciiValues) {
        StringBuilder stringBuilder = new StringBuilder();
        char character;

        for (float asciiValue : asciiValues) {
    
            character = (char)(round(asciiValue));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }



    /**
    *
    * Function that porform s the matrix product
    *
    */
    private void matrixProduct()
    {


        float [][] ACCIItranspose = transpose(ASCIIValues);
        int rowsA = matrixEncoder.length;
        int columnsA = matrixEncoder[0].length;
        float[][] resultMatrix = new float[rowsA][1];
    
        // Perform matrix multiplication
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < columnsA; k++) {

                    resultMatrix[i][j] += matrixEncoder[i][k] * ACCIItranspose[k][j];
                    
                }
            }
        }
        
        this.resultVector = transpose(resultMatrix)[0];

    }


    


    /**
    * Function to thranspose a marix
    */
    public static float[][] transpose(float[][] matrix) {
        
        int rows = matrix.length;
        int columns = matrix[0].length;

        float[][] resultMatrix = new float[columns][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                resultMatrix[j][i] = matrix[i][j];
            }
        }

        return resultMatrix;
    }


    private static int round(float n)
    {
        return (n > (int)n + 0.5) ? (int)(n+1) : (int)n; 
    }

}