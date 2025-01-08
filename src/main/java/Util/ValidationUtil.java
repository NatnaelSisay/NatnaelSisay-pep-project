package Util;

import Model.Account;

public class ValidationUtil {
    public static boolean isValidAccount(Account account){
        // non empty string and password length greater than or equal to 4
        return !(account.username.isEmpty() || account.password.length() < 4);
    }

    
}
