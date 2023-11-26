
import Encrypt.BlockEncryption;

public class Main {

    public static void main(String[] args)
    {


        String uwu = "Me gusta el pan";
        BlockEncryption en = new BlockEncryption(3313, 2);

        System.err.println("========================================");
        String awa = en.Encrypt(uwu);
        System.err.println("========================================");
        awa = en.Decrypt(awa);
        System.err.println("========================================");


    }   

}
