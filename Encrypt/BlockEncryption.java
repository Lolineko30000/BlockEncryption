package Encrypt;




public class BlockEncryption {
    



    public float[][] matrixE; //Encryption matrix
    public float[][] matrixI; //Inverse of the encrytion matrix
    private int encryptionSize; //Numer of the batch size
    private long publicKey;     
    private int numThreads; //number of threads
    private final int maxNumThreads;
    
    
    //Constructor
    public BlockEncryption(long publicKey, int encryptionSize )
    {
        this.publicKey = publicKey;
        this.encryptionSize = encryptionSize;
        this.matrixE = EncryptMatrix.createEncryptionMatrix(this.publicKey , this.encryptionSize);
        this.matrixI = EncryptMatrix.inverseMatrix(matrixE);
        this.numThreads = 0;
        this.maxNumThreads = 10;
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
        String[] bathces = divideString(mesasge);
        LinearTransform[] threads = new LinearTransform[this.numThreads];   
        int secondIndex = 1;
        int numThreadsBatches = 1;
        int iterator = 0;
        boolean flag = false;

        if(this.numThreads > this.maxNumThreads)
        {
            secondIndex = Math.ceilDiv(this.maxNumThreads, this.numThreads);
            numThreadsBatches = this.maxNumThreads/this.numThreads;
            flag = true;
        }

        
        for (int j = 0; j < secondIndex; j++) {
        //Creation of the threads
            
            if(flag){
                iterator = Math.max( numThreadsBatches ,this.maxNumThreads%this.numThreads);
            }else{
                iterator = this.numThreads;
            }

            
            for (int i = 0; i < iterator; i++) {
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
            for(LinearTransform thread : threads) res +=  thread.getResult();

            numThreadsBatches -= this.maxNumThreads;
        }

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
