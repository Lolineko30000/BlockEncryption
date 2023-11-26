package Encrypt;




public class BlockEncryption {
    



    public float[][] matrixE; //Encryption matrix
    public float[][] matrixI; //Inverse of the encrytion matrix
    private int encryptionSize; //Numer of the batch size
    private long publicKey;     
    private final int numThreads; //number of threads
    
    
    //Constructor
    public BlockEncryption(long publicKey, int encryptionSize )
    {
        this.publicKey = publicKey;
        this.encryptionSize = encryptionSize;
        this.matrixE = EncryptMatrix.createEncryptionMatrix(this.publicKey , this.encryptionSize);
        this.matrixI = EncryptMatrix.inverseMatrix(matrixE);
        this.numThreads = 10;
    }



    /**
    * Function that encrypt a a message
    *
    * @param originalString the string to be decoded
    * @return the message encrypted
    */
    public String Encrypt(String originalString)
    {

        String pan = ThreadExecution(originalString, matrixE);
        System.out.println(pan);
        return pan;
    }



    /**
    * Function that decrypt a a message
    *
    * @param encryptedString the encrypted string
    * @return the message decrypted
    */
    public String Decrypt(String encryptedString)
    {
        return ThreadExecution(encryptedString, matrixI);
    }

    


    /**
    * Function to awake the threads
    *
    * @param mesasge the message to calculate
    * @param decripter the matrix to perform the calculation
    * @return the calculated mesage
    */
    private String ThreadExecution(String mesasge, float[][] decripter)
    {


        String res = "";
        String[] bathces = divideString(mesasge, this.numThreads);
        LinearTransform[] threads = new LinearTransform[this.numThreads];


        //Creation of the threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new LinearTransform(decripter, bathces[i]);
            threads[i].start();
        }

        // Wait for all threads to finish
        try {
            for (LinearTransform thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Joining the results
        for(LinearTransform thread : threads)
            res +=  thread.getResult();

        return res;

    }





    /**
    * Function to divide the message in batches
    *
    * @param inputString the message 
    * @param n number of batches
    * @return array of the batches of the string
    */
    public String[] divideString(String inputString, int n) {
        
        String[] res = new String[n];
        int stringLen = inputString.length();
        int needed = stringLen%n;
    
        for(int i = 0 ; i < needed; i++) inputString += " "; //Filling of the missing values
        int bathSize = inputString.length()/n; 

        //Creation of the batches
        for(int i = 0 ; i < n-1; i++) res[i] = inputString.substring(i*bathSize, (i+1)*(bathSize));
        
        return res;
    }


}
