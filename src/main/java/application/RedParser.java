package application;

import Utils.RedmineProperties;
import http.OkHttpHelper;

public class RedParser {

    public static void main(String[] args){
            if(args.length == 0
                || args[0].equals("")
                    || args[1].equals("")
                    || args[0] == null
                    || args[1] == null){
                System.out.println("Please provide an API Key and IP Address: java application.RedParser <IP Address> <Key>");
            }else{
                new RedmineProperties(args[0], args[1]);
                new OkHttpHelper().init();
                new RedParserApplication().run();
            }

    }
}
