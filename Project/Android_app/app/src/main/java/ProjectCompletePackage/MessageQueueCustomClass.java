package ProjectCompletePackage;

import java.util.*;

/**
 * Created by Pratik on 10/1/2017.
 */

public class MessageQueueCustomClass {
    static public Vector<Character> messages = new Vector<Character>();

    public static boolean isNewMessageArrived(){
        if(messages.size() > 0)
            return true;
        else
            return false;
    }

    public static void sendMessage(String message){
        if(message.length() > 0) {
            for(char c: message.toCharArray())
            messages.add(c);
        }
    }

    public static String getFirstMessage(){
        int toCheck = messages.elementAt(0);
        int start = '0';
        int end = '9';

        if(toCheck >= start && toCheck <= end){
            String tempID = "";

            if(messages.size() >= 3 ){
                for(int i=0; i<3; i++){
                    tempID += messages.elementAt(0);
                    messages.removeElementAt(0);
                }
            }

            return tempID;
        }
        else{
            char temp = messages.elementAt(0);
            messages.removeElementAt(0);
            return (temp + "");
        }

    }

    public static void clearAll(){
        messages.clear();
    }

}
