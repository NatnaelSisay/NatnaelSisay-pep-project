package Util;

import Model.Account;
import Model.Message;

public class ValidationUtil {
    public static boolean isValidAccount(Account account){
        return !(account.username.isEmpty() || account.password.length() < 4);
    }

    public static boolean isValidMessage(Message message){
        return !(message.message_text.isEmpty() || message.message_text.length() > 255);
    }
    
}
