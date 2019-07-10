package application;

import Utils.RedmineProperties;
import http.OkHttpHelper;

public class RedParser {

    public static void main(String[] args){
            if(args.length == 0
                || args[0].equals("")
                    || args[1].equals("")
                    || args[2].equals("")
                    || args[3].equals("")
                    || args[0] == null
                    || args[1] == null
                    || args[2] == null
                    || args[3] == null
                    || args.length != 4){
                System.out.println("Check your parameters! Correct Usage: java RedParser <URL> <Key> <ChatID> <BotID>");
            }else{
                new RedmineProperties(args[0], args[1], args[2], args[3]);
                new OkHttpHelper().init();
                new RedParserApplication().run();
            }

    }
}
