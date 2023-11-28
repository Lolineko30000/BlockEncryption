package Encrypt;

import java.util.Objects;

public class PasswordHash {
    
    public static int Hash(String input) {
        if (input == null) {
            return 0;
        }
        return (Objects.hash(input) & Integer.MAX_VALUE)&11;
    }

}
