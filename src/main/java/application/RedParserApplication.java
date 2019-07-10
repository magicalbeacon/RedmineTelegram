package application;

import Utils.Constants;
import Utils.RedmineProperties;
import Utils.RedmineRequest;
import http.OkHttpHelper;
import model.Issue;
import model.IssuesModel;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import telegram.RedmineTelegram;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class RedParserApplication {

    private String filename = RedmineProperties.getProperties().getProperty(Constants.TOKEN);
    private String createdAfter;
    private RedmineRequest client;
    private RedmineTelegram telegram;
    private boolean initialized = false;
    private SendMessage msg;
    private Call<IssuesModel> call;

    public void run(){
        if(!initialized)
            init();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                parseNewStuff();
            }
        }, 0,30000);
    }

    public void init(){
        initTelegram();
        client = OkHttpHelper.getInstance();
        createdAfter = (getTimestamp() == null) ? null : Constants.CREATED_AFTER_TAG + getTimestamp();
        msg = new SendMessage();
        call = client.getAllIssues(createdAfter, Constants.SORT_DATE_DESC);
        initialized = !initialized;
    }

    private void parseNewStuff(){
        System.out.println("PARSING NEW STUFF!");
        System.out.println("Created after:" +createdAfter);
        System.out.println("Timestamp: " + getTimestamp());
        createdAfter = (getTimestamp() == null) ? null : Constants.CREATED_AFTER_TAG + getTimestamp();
        call.clone().enqueue(new Callback<IssuesModel>() {
            @Override
            public void onResponse(Call<IssuesModel> call, Response<IssuesModel> response) {
                System.out.println("Response:" + response.toString());
                if(response.code() == Constants.HTTP_UNAUTHORIZED){
                    System.out.println("Invalid API Key!");
                    System.exit(0);
                }else if(response.code() == Constants.HTTP_NOT_FOUND){
                    System.out.println("Invalid URL!");
                    System.exit(0);
                }
                IssuesModel base = response.body();
                LocalDateTime date = null;
                try {
                    date = LocalDateTime.parse(getTimestamp(), DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
                    System.out.println(date.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }
                String message = "";
                if(response != null && response.body() != null) {
                    LocalDateTime newestTime = LocalDateTime.parse(base.getIssues().get(0).getCreatedOn(), DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
                    if (base.getIssues().size() > 0) {
                        base.getIssues().forEach(System.out::println);
                        if(date.equals(newestTime)){
                                System.out.println("No new issues");
                        }else {
                            for(Issue issue : base.getIssues()) {
                                LocalDateTime tempTime = LocalDateTime.parse(issue.getCreatedOn(), DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
                                if(tempTime.isAfter(date)) {
                                    System.out.println("Attempting to send message...");
                                    System.out.println("Newest Issue:" + issue);
                                    message = formatText(issue);
                                    msg.setText(message);
                                    msg.setChatId(RedmineProperties.getProperties().getProperty(Constants.TELEGRAM_ID));
                                    msg.setParseMode(Constants.TELEGRAM_MARKDOWN);
                                    try {
                                        telegram.execute(msg);
                                    }catch(Exception e) {
                                        System.out.println("Invalid Chat Id!!");
                                        System.exit(0);
                                    }
                                }else
                                    break;
                            }
                            writeNewTimestamp(base.getIssues().get(0).getCreatedOn());
                        }
                    } else {
                        System.out.println("No New Issues");
                        //-1001160531201
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<IssuesModel> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Something went wrong! Ensure that your parameters are correct!");
                System.exit(0);
            }
        });


    }

    private String formatText(Issue data){
        StringBuilder builder = new StringBuilder();
        String.format("*%15s: *", data.getTracker());
        builder.append(String.format("*%s #%s*", data.getTracker().getName(), data.getId()));
        builder.append("\n");
        builder.append(String.format("*%-16s:* `%s`", "Subject",data.getSubject()));
        builder.append("\n");
        builder.append(String.format("*%-16s:* `%s`", "Priority",data.getPriority().getName()));
        builder.append("\n");
        builder.append(String.format("*%-13s:* `%s`", "Start Date",data.getStartDate()));
        builder.append("\n");
        builder.append(String.format("*%-13s:* `%s`", "Due Date",data.getDueDate()));
        builder.append("\n");
        builder.append(String.format("*%-13s*", "Description :"));
        builder.append("\n");
        builder.append(String.format("```\n" +
                "%s```", data.getDescription()));
        builder.append("\n");
        builder.append(String.format("*%-11s:* `%s`", "Reported By", data.getAuthor().getName()));
        builder.append("\n");
        builder.append(String.format("*%-12s:* `%s`", "Assigned to", (data.getAssignedTo() == null ? "" : data.getAssignedTo().getName())));
        builder.append("\n\n");
        builder.append("_ Created on: " +formatDate(data.getCreatedOn()) + "_");
        System.out.println(builder.toString());
        return builder.toString();
    }

    private String formatDate(String date){
        return Date.from( Instant.parse(date)).toString();
    }


    private void initTelegram(){
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        telegram = new RedmineTelegram();
        try {
            botsApi.registerBot(new RedmineTelegram());
        }catch(TelegramApiException e){
            System.out.println("Error Initializing Bot! Check your Bot Token!");
            System.exit(0);
        }
    }

    public void writeNewTimestamp(String timestamp){
        PrintWriter writer = null;
        File file = new File(filename);
        try {
            writer = new PrintWriter(file, "UTF-8");
            writer.print(timestamp);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getTimestamp() {
        File file = new File(filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return null;
            } else {
                Scanner sc = new Scanner(file);
                if(sc.hasNext()) {
                    return sc.nextLine();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error reading timestamp file! Do not delete file with your API Key!");
            System.exit(0);
        }
        return null;
    }
}
