package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class PNG_Bot extends TelegramLongPollingBot {

    /**
     * Hashmap for updating on User State
     */
    Map<Long, String> userState = new HashMap<>();

    /**
     * Hashmap for adding user information
     */
    Map<Long, Users> usersMap = new HashMap<>();

    /**
     * Hashmap for adding school admin information
     */
    Map<Long, SchoolAdmin> schoolAdminMap = new HashMap<>();

    /**
     * Hashmap for adding booking information
     */
    Map<Long, Booking> bookingMap = new HashMap<>();

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();


        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();
            String[] command = message.getText().split(" ");

            //If the user chatID does not appear in userState hashmap, add/put the chatID into hashmap
            if (!userState.containsKey(message.getChatId())) {
                userState.put(message.getChatId(), "Start");
            }


            //usersMap.put(message.getChatId(), new Users("","","","",""));
            /**
             * Check the command inputted by the user
             */
            switch (command[0]) {
                case "/start":
                    if (command.length == 1) {
                        String info = "Hi there! I'm Turtle, your UUM Room Booking Assistant.\n\n" +
                                "Enter /book to book a room! The rooms can be booked between 8am to 8pm";
                        sendMessage = new SendMessage();
                        sendMessage.setText(info);
                        sendMessage.setChatId(message.getChatId().toString());
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "/book":
                    userState.put(message.getChatId(), "Book");
                    String info = "Do you intend to book a room?";
                    sendMessage = new SendMessage();
                    sendMessage.setText(info);
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("Yes");
                    inlineKeyboardButton2.setText("No");
                    inlineKeyboardButton1.setCallbackData("Book1_Y");
                    inlineKeyboardButton2.setCallbackData("Book1_N");
                    inlineKeyboardButtonList.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList.add(inlineKeyboardButton2);
                    inlineButtons.add(inlineKeyboardButtonList);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/login":
                    String info2 = "Please enter your IC and Email to access your account\n\n" +
                            "Example: 990724070661@MyEmail@hotmail.com";
                    sendMessage = new SendMessage();
                    sendMessage.setText(info2);
                    sendMessage.setChatId(message.getChatId().toString());
                    userState.put(message.getChatId(), "Login");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            //Go to check state if no command found AND State have the first word as 'B'
            if(!String.valueOf(command[0].charAt(0)).equals("/")){

            }

        }


    }
}
