
import Encrypt.BlockEncryption;

public class Main {

    public static void main(String[] args)
    {


        String uwu = "Pero tu prima robo mas";
        BlockEncryption en = new BlockEncryption(3313, 10);

        System.err.println("========================================");
        String awa = en.Encrypt(uwu);
        System.err.println(awa);
        System.err.println("========================================");
        awa = en.Decrypt(awa);
        System.err.println(awa);
        System.err.println("========================================");


    }   

}
