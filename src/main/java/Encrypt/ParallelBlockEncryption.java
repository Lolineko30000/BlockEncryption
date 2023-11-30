package Encrypt;




public class ParallelBlockEncryption {
    



    public float[][] matrixE; //Encryption matrix
    public float[][] matrixI; //Inverse of the encrytion matrix
    private int encryptionSize; //Numer of the batch size
    private long publicKey;     
    private int numThreads; //number of threads
    private final int maxNumThreads;
    
    
    //Constructor
    public ParallelBlockEncryption(String key, int encryptionSize , int maxThreads )
    {
        this.publicKey = PasswordHash.Hash(key);
        this.encryptionSize = encryptionSize;
        this.matrixE = EncryptMatrix.createEncryptionMatrix(this.publicKey , this.encryptionSize);
        this.matrixI = EncryptMatrix.inverseMatrix(matrixE);
        this.numThreads = 0;
        this.maxNumThreads = maxThreads;
    }



    /**
    * Function that encrypt a a message
    *
    * @param originalString the string to be decoded
    * @return the message encrypted
    */
    public String Encrypt(String originalString)
    {
        return ThreadExecution(originalString, matrixE);
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
        String[] bathces = divideString(mesasge);
        ParallelLinearTransform[] threads;  
        
        int secondIndex = 1;
        int[] threadBatches;


        secondIndex = (this.numThreads/this.maxNumThreads) + ((this.numThreads%this.maxNumThreads > 0)? 1 : 0);
        threadBatches = new int[secondIndex];

        //Creation of the number of the batches
        for(int j = 0; j < secondIndex;  j++) threadBatches[j] = maxNumThreads;
        if(this.numThreads%this.maxNumThreads > 0) threadBatches[secondIndex-1] = this.numThreads%this.maxNumThreads;
        
        

        long startTime = System.currentTimeMillis();
        for (int j = 0; j < secondIndex; j++) {
        //Creation of the threads


            threads =  new ParallelLinearTransform[threadBatches[j]]; 
            
            for (int i = 0; i < threadBatches[j]; i++) {
                threads[i] = new ParallelLinearTransform(decripter, bathces[j*maxNumThreads + i]);
                threads[i].start();
            }


            // Wait for all threads to finish
            try {
                for (ParallelLinearTransform thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Joining the results
            for(ParallelLinearTransform thread : threads) res +=  thread.getResult();

        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        double seconds = executionTime / 1000.0;
        
        
        System.out.println("Execution time: " + seconds + " seconds");
        System.err.println("=========================================");

        return res;

    }





    /**
    * Function to divide the message in batches
    *
    * @param inputString the message 
    * @param n number of batches
    * @return array of the batches of the string
    */
    public String[] divideString(String inputString) {
        

        int stringLen = inputString.length();
        int needed = stringLen%this.encryptionSize ;
        if(needed > 0)needed = (this.encryptionSize - needed);
        

        for(int i = 0 ; i < needed; i++) inputString += " "; //Filling of the missing values
        stringLen = inputString.length();

        this.numThreads = stringLen/this.encryptionSize  + (stringLen%this.encryptionSize > 0 ? 1 : 0);

        String[] res = new String[this.numThreads];
        for(int i = 0 ; i < this.numThreads; i++) res[i] = inputString.substring(i*this.encryptionSize, (i+1)*(this.encryptionSize));
    
        

        return res;
    }




}
