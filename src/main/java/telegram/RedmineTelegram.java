package telegram;

import Utils.Constants;
import Utils.RedmineProperties;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RedmineTelegram extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "TelegramRedmine_Bot";

    }

    @Override
    public String getBotToken() {
        return RedmineProperties.getProperties().getProperty(Constants.BOT_ID);
    }
}
