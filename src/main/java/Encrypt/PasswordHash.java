package Encrypt;

import java.util.Objects;

public class PasswordHash {
    
    public static int Hash(String input) {
        return ((Objects.hash(input) & Integer.MAX_VALUE)&104729)+1;
    }

}
