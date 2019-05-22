package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RedmineTelegram extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "redquarry_bot";

    }

    @Override
    public String getBotToken() {
        return "734502753:AAHxXH1qWrmhBpA3nC3ScmCsL8PCIvjGTGg";
    }
}
