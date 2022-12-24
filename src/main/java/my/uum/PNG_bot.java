package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
        import org.telegram.telegrambots.meta.api.methods.ParseMode;
        import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
        import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
        import org.telegram.telegrambots.meta.api.objects.Message;
        import org.telegram.telegrambots.meta.api.objects.Update;
        import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
        import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
        import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

        import java.util.*;

public class PNG_bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "PNG_bot";
    }

    @Override
    public String getBotToken() {
        return "5813032321:AAFWCPiKtpUVrPa5mTzu6ZhZhXGVP7Va_vc";
    }

    /**
     * Hashmap for updating on User State
     */
    Map<Long, String> userState = new HashMap<Long, String>();

    /**
     * Hashmap for adding user information
     */
    Map<Long, Users> usersMap = new HashMap<Long, Users>();

    /**
     * Hashmap for adding school admin information
     */
    //Map<Long, SchoolAdmin> schoolAdminMap = new HashMap<Long, SchoolAdmin>();

    /**
     * Hashmap for adding booking information
     */
    //Map<Long, Booking> bookingMap = new HashMap<Long, Booking>();

    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        DatabaseManager databaseManager = new DatabaseManager();


        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(message.getChatId() + " " +  message.getText());


            if(!userState.containsKey(message.getChatId())){
                userState.put(message.getChatId(),"Start");
            }

            //seperate input from user to recognize the IC and Email Inputted by user
            String[] password = message.getText().split("@",2);



            /**
             * Check the command inputted by the user
             */
            switch (message.getText()) {
                case "/start":
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
                    break;
                case "/book":
                    //当command flow正当开始的第一步骤时，我们会给这个chatID开object来接收接下来的information
                    usersMap.put(message.getChatId(), new Users("","","","",""));

                    userState.put(message.getChatId(), "Book");
                    String info2 = "Do you intend to book a room?";
                    sendMessage = new SendMessage();
                    sendMessage.setText(info2);
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
                    inlineKeyboardButton1.setCallbackData("Book:Book_Y");
                    inlineKeyboardButton2.setCallbackData("Book:Book_N");
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
                /*case "/login":
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
                 */
            }

            //Go to check state if no command found AND State have the first word as 'Book'
            if(!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Book:")){
                switch (userState.get(message.getChatId())){
                    case "Book:IC":
                        //save user 在 Book:Book_Y 之后input的内容起来，进object
                        usersMap.get(message.getChatId()).setName(message.getText());

                        //set新的State
                        userState.put(message.getChatId(),"Book:Email");
                        sendMessage.setText("How about your NRIC number?\nExample: 001211080731");
                        sendMessage.setChatId(message.getChatId());

                        break;
                }

                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }

            //Go to check state if no command found AND State have the first word as 'Login'
            if(!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Login")){
                switch (userState.get(message.getChatId())){

                    case "Login:Verification":
                        //if password verification is true
                        if(databaseManager.passwordCheck(password[0],password[1])){
                            //先把Password里的IC放进去usersMap
                            usersMap.get(message.getChatId()).setICNO(password[0]);

                            //用Assign好的IC来找user ID, 然后放进int userID里面
                            int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());

                            //用userID 来找user的booking记录
                            String bookedRooms = databaseManager.viewBookedList(userID, "start");

                            //打招呼和问user要做什么
                            bookedRooms+= "\n\n" + databaseManager.greetings(usersMap.get(message.getChatId()).getICNO());

                            sendMessage = new SendMessage();
                            sendMessage.setText(bookedRooms);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);
                            sendMessage.setChatId(message.getChatId());

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList3 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("View Booking Details");
                            inlineKeyboardButton2.setText("Edit Booking");
                            inlineKeyboardButton3.setText("Cancel Booking");
                            inlineKeyboardButton4.setText("Edit Profile");
                            inlineKeyboardButton1.setCallbackData("Login:ViewBook");
                            inlineKeyboardButton2.setCallbackData("Login:EditBook");
                            inlineKeyboardButton3.setCallbackData("Login:CancelBook");
                            inlineKeyboardButton4.setCallbackData("Login:EditProfile");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                            inlineKeyboardButtonList3.add(inlineKeyboardButton4);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineButtons.add(inlineKeyboardButtonList3);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                        }

                        break;
                }

                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }


        }else if(update.hasCallbackQuery()){
            //buttonData will be categorized such as Book:Conf_Y, same reason as state
            String[] buttonData = update.getCallbackQuery().getData().split(":",2);

            Message message = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            sendMessage = new SendMessage();
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setChatId(message.getChatId());

            if(buttonData[0].equals("Book")){
                if(data.equals("Book:Book_Y")){


                    //记得在save东西之后换state,可以看Book:IC做example
                    userState.put(message.getChatId(),"Book:IC");
                    sendMessage.setText("May I have the your NAME (as per NRIC/PASSPORT) please?" +
                            "\n\n P.S.:Don't worry, you can edit your information after the information are entered ;)");
                }
            }
        }


    }
}
