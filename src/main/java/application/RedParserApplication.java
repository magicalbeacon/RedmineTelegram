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
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


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
                IssuesModel base = response.body();
                System.out.println("Base:" + base.toString());
                String message = "";
                if(response != null && response.body() != null) {
                    if (base.getIssues().size() > 0) {
                        base.getIssues().forEach(System.out::println);
                        if(getTimestamp().equals(base.getIssues().get(0).getCreatedOn())){
                                System.out.println("No new issues");
                        }else {
                            System.out.println("Newest Issue:" + base.getIssues().get(0).toString());
                            message = formatText(base.getIssues().get(0));
                            writeNewTimestamp(base.getIssues().get(0).getCreatedOn());
                            msg.setText(message);
                            msg.setChatId("-1001160531201");
                        }
                    } else {
                        System.out.println("No Stuff :(");
                        msg.setText("HELLO NO NEW STUFF");
                        msg.setChatId("-1001160531201");
                    }
                }else{

                }
                if(!message.equals("")){
                    try {
                        msg.setParseMode(Constants.TELEGRAM_MARKDOWN);
                        telegram.execute(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<IssuesModel> call, Throwable t) {
                System.out.println("Call Failed");
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
        builder.append(String.format("*%-12s:* `%s`", "Assigned to", data.getAssignedTo().getName()));
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
            e.printStackTrace();
        }
    }

    public boolean isNewData(String timestamp){
        Date currentDate = Date.from(Instant.parse(getTimestamp()));
        return false;
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
                while (sc.hasNext()) {

                    return sc.nextLine();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
