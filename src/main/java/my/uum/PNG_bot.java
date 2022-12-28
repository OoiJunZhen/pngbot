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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    Map<Long, Booking> bookingMap = new HashMap<Long, Booking>();

    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        DatabaseManager databaseManager = new DatabaseManager();
        InputFormatChecker inputFormatChecker = new InputFormatChecker();


        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(message.getChatId() + " " +  message.getText());


            if(!userState.containsKey(message.getChatId())){
                userState.put(message.getChatId(),"Start");
            }


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
                case "/login":
                    usersMap.put(message.getChatId(), new Users("","","","",""));
                    userState.put(message.getChatId(), "Login:Verification");
                    String info3 = "Please enter your IC and Email to access your account\n\n" +
                            "Example: 990724070661@MyEmail@hotmail.com";
                    sendMessage = new SendMessage();
                    sendMessage.setText(info3);
                    sendMessage.setChatId(message.getChatId().toString());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/test":
                    usersMap.put(message.getChatId(), new Users("","","","",""));
                    userState.put(message.getChatId(), "Test:Input");
                    sendMessage = new SendMessage();
                    sendMessage.setText("Enter Date\n\nExample: 23/02/2023");
                    sendMessage.setChatId(message.getChatId().toString());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

            }


            //Go to check state if State have the first word as 'Test:'
            if(!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Test:")){

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();

                try{
                    Date bookDate = sdf.parse(message.getText());


                        if(databaseManager.checkBook(2, message.getText())){
                            sendMessage.setText("True");
                        } else{
                            sendMessage.setText("I'm here");
                        }

                } catch (ParseException e){
                    e.printStackTrace();
                }

                sendMessage.setChatId(message.getChatId());
                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }

            //Go to check state if State have the first word as 'Book'
            else if(!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Book:")){
                //Get User State
                switch (userState.get(message.getChatId())){
                    case "Book:IC":
                        if(inputFormatChecker.NameFormat(message.getText())){
                            //save user 在 Book:Book_Y 之后input的内容起来，进object
                            usersMap.get(message.getChatId()).setName(message.getText());

                            //set新的State
                            userState.put(message.getChatId(),"Book:Email");
                            sendMessage.setText("How about your NRIC number?\nExample: 001211080731");
                            sendMessage.setChatId(message.getChatId());

                        }else{
                            sendMessage.setText("Please re-enter your name.");
                            sendMessage.setChatId(message.getChatId());
                        }

                        break;

                    case "Book:Email":
                        if(inputFormatChecker.checkICFormat(message.getText())) {
                            usersMap.get(message.getChatId()).setICNO(message.getText());
                            //if database has user
                            if (databaseManager.checkUser(message.getText())) {
                                String text = databaseManager.displayUserInfo(usersMap.get(message.getChatId()).getICNO());

                                sendMessage = new SendMessage();
                                sendMessage.setText(text);
                                sendMessage.setParseMode(ParseMode.MARKDOWN);
                                sendMessage.setChatId(message.getChatId());

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                inlineKeyboardButton1.setText("Yes");
                                inlineKeyboardButton2.setText("No this is not me");
                                inlineKeyboardButton1.setCallbackData("Book:Verification");
                                inlineKeyboardButton2.setCallbackData("Book:FillIC");
                                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                inlineButtons.add(inlineKeyboardButtonList1);
                                inlineButtons.add(inlineKeyboardButtonList2);
                                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            } else {
                                //save user 在 Book:Book_Y 之后input的内容起来，进object
                                usersMap.get(message.getChatId()).setICNO(message.getText());
                                userState.put(message.getChatId(), "Book:StaffID");
                                sendMessage.setText("How about your Email?");
                                sendMessage.setChatId(message.getChatId());
                            }

                        }else{
                            sendMessage.setText("Please enter your IC in correct format thank you :).");
                            sendMessage.setChatId(message.getChatId());
                        }

                        break;

                    case "Book:StaffID":
                        if(inputFormatChecker.EmailFormat(message.getText())){
                            usersMap.get(message.getChatId()).setEmail(message.getText());
                            userState.put(message.getChatId(),"Book:Mobile");
                            sendMessage.setText("Almost There! How about your Staff ID?\n\nExample: abc123");
                            sendMessage.setChatId(message.getChatId());
                        }else{
                            sendMessage.setText("Please enter your email in correct format, thank you :)");
                            sendMessage.setChatId(message.getChatId());
                        }

                        break;

                    case "Book:Mobile":
                        usersMap.get(message.getChatId()).setStaffID(message.getText());
                        userState.put(message.getChatId(),"Book:Confirmation");

                        sendMessage.setText("What is the best contact number to reach you? \n\n Example: 0124773579");
                        sendMessage.setChatId(message.getChatId());

                    break;

                    case "Book:Confirmation":
                    case "Book:Chan_Name":
                    case "Book:Chan_IC":
                    case "Book:Chan_Email":
                    case "Book:Chan_StaffID":
                    case "Book:Chan_TelNo":

                        boolean output = false;
                        if(userState.get(message.getChatId()).equals("Book:Confirmation") || userState.get(message.getChatId()).equals("Book:Chan_TelNo")){
                            if(inputFormatChecker.TelNumFormat(message.getText())){
                                usersMap.get(message.getChatId()).setTelNo(message.getText());
                                output = true;
                            }else{
                                sendMessage.setText("Please enter your phone number in correct format thank you.\n\n" +
                                        "Example: 0124773579");
                                sendMessage.setChatId(message.getChatId());
                            }
                        } else if (userState.get(message.getChatId()).equals("Book:Chan_Name")) {
                            if(inputFormatChecker.NameFormat(message.getText())){
                                usersMap.get(message.getChatId()).setName(message.getText());
                                output = true;
                            }else{
                                sendMessage.setText("Please re-enter your name.\n\n" +
                                        "Example: Ang Toon Phng");
                                sendMessage.setChatId(message.getChatId());
                            }

                        } else if (userState.get(message.getChatId()).equals("Book:Chan_IC")) {

                            //check IC format
                            if(inputFormatChecker.checkICFormat(message.getText())){

                                //check if user exist in the database
                                if(!databaseManager.checkUser(message.getText())){
                                    usersMap.get(message.getChatId()).setICNO(message.getText());
                                    output = true;
                                }else {
                                    //if IC already used, then it is invalid
                                    sendMessage.setText("Sorry, this IC had been used by someone else, please enter another IC number\n\n" +
                                            "Example: 001211080731");
                                    sendMessage.setChatId(message.getChatId());
                                }
                            }else{
                                sendMessage.setText("Please re-enter your IC in correct format thank you.\n\n" +
                                        "Example: 001211080731");
                                sendMessage.setChatId(message.getChatId());
                            }
                        } else if (userState.get(message.getChatId()).equals("Book:Chan_StaffID")) {
                            usersMap.get(message.getChatId()).setStaffID(message.getText());
                            output=true;

                        } else if (userState.get(message.getChatId()).equals("Book:Chan_Email")) {

                            //check email format
                            if (inputFormatChecker.EmailFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setEmail(message.getText());
                                output = true;
                            } else {
                                sendMessage.setText("Please re-enter the email in correct format thank you.\n\n" +
                                        "Example: MyEmail@hotmail.com");
                                sendMessage.setChatId(message.getChatId());
                            }
                        }

                        if(output){
                            String info = "Are these the correct information?\n" +
                                    "\nName: " + usersMap.get(message.getChatId()).getName() +
                                    "\nIC: " + usersMap.get(message.getChatId()).getICNO() +
                                    "\nEmail: " + usersMap.get(message.getChatId()).getEmail() +
                                    "\nStaff ID: " + usersMap.get(message.getChatId()).getStaffID() +
                                    "\nTel No.: " + usersMap.get(message.getChatId()).getTelNo();


                            sendMessage = new SendMessage();
                            sendMessage.setText(info);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);
                            sendMessage.setChatId(message.getChatId());

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("Yes");
                            inlineKeyboardButton2.setText("No I would like to change something");
                            inlineKeyboardButton1.setCallbackData("Book:Conf_Y");
                            inlineKeyboardButton2.setCallbackData("Book:Conf_N");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        }

                    break;

                    case "Book:School":
                        if(message.getText().contains("@")){
                            String[] password = message.getText().split("@", 2);

                            if(databaseManager.passwordCheck(password[0],password[1])){

                                Integer userID = databaseManager.getUserID(password[0]);
                                Date date = new Date();
                                bookingMap.put(message.getChatId(),new Booking(date, date, date, 0, "", "", 0, userID));
                                userState.put(message.getChatId(),"Book:Room");

                                String list = databaseManager.schoolList();
                                list+="Excellent! Which school do you wish to book in?\nExample reply: 1";

                            sendMessage.setText(list);
                            sendMessage.setChatId(message.getChatId());

                            }else {
                                sendMessage.setText("We can't find you in the database. Please re-enter your IC and Email thank you.\n\n" +
                                        "Example: 990724070661@MyEmail@hotmail.com");
                                sendMessage.setChatId(message.getChatId());
                            }
                        }else{
                            sendMessage.setText("Incorrect format, please re-enter your IC and Email thank you.\n\n" +
                                    "Example: 990724070661@MyEmail@hotmail.com");
                            sendMessage.setChatId(message.getChatId());
                        }


                    break;

                    case "Book:Room":
                        if(databaseManager.checkSchool(message.getText())){
                            userState.put(message.getChatId(),"Book:RoomDetails");
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));
                            String roomList = databaseManager.getRoomList(bookingMap.get(message.getChatId()).getRoomID());

                            roomList += "Which room do you want to book?\nExample reply: 1";
                            sendMessage.setText(roomList);
                            sendMessage.setChatId(message.getChatId());


                        }else{
                            String list2 = databaseManager.schoolList();
                            list2 += "This school does not exist. Please re-enter the school that you wish to book in.\nExample reply: 1";

                            sendMessage.setText(list2);
                            sendMessage.setChatId(message.getChatId());
                        }
                    break;

                    case "Book:RoomDetails":
                        if(databaseManager.checkRoom(message.getText(),bookingMap.get(message.getChatId()).getRoomID())){
                            //update roomID in hashmap to actual room ID (Initially it is School ID)
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));

                            //display room information in details
                            String list1 = databaseManager.getRoomInfo(bookingMap.get(message.getChatId()).getRoomID());

                            sendMessage = new SendMessage();
                            sendMessage.setText(list1);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);
                            sendMessage.setChatId(message.getChatId());

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("Yes");
                            inlineKeyboardButton2.setText("No, choose another room");
                            inlineKeyboardButton1.setCallbackData("Book:Room_Conf_Y");
                            inlineKeyboardButton2.setCallbackData("Book:Room_Conf_N");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);



                        }else{
                            String list2 = databaseManager.getRoomList(bookingMap.get(message.getChatId()).getRoomID());
                            list2 += "This room does not exist. Please re-enter the room that you wish to book.\nExample reply: 1";

                            sendMessage.setText(list2);
                            sendMessage.setChatId(message.getChatId());
                        }

                    break;
                    case "Book:StartTime":
                        if(inputFormatChecker.DateFormat(message.getText())){

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();

                            try {
                                Date bookDate = sdf.parse(message.getText());

                                if(inputFormatChecker.bookDate(bookDate, date)){

                                   bookingMap.get(message.getChatId()).setTemp(message.getText());



                                }else{
                                    sendMessage.setText("The booking date needs to be at least 1 month prior and also can't be made if that day is over 1 year away.\n" +
                                            "As an example, to book a day in April 1st, booking needs to be made on March 1st or before.\n\n" +
                                            "Please enter the day you would like to book\nExample: 27/04/2023");
                                    sendMessage.setChatId(message.getChatId());
                                }
                            }catch(ParseException e){
                                e.printStackTrace();
                            }


                        }else{
                            sendMessage.setText("Please enter the date in correct format.\n" +
                                    "Example: 27/04/2023");
                            sendMessage.setChatId(message.getChatId());
                        }

                    break;

                }


                try{
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }


            //Go to check state if State have the first word as 'Login'
            else if(!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Login:")){
                //Get user state
                switch (userState.get(message.getChatId())){
                    case "Login:Verification":
                        //seperate input from user to recognize the IC and Email Inputted by user
                        String[] password = message.getText().split("@",2);
                        //if password verification is true
                        if(message.getText().contains("@")) {
                            if (databaseManager.passwordCheck(password[0], password[1])) {

                                //先把Password里的IC放进去usersMap
                                usersMap.get(message.getChatId()).setICNO(password[0]);

                                //用Assign好的IC来找user ID, 然后放进int userID里面
                                int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());

                                //用userID 来找user的booking记录
                                String bookedRooms = databaseManager.viewBookedList(userID, "start");

                                //打招呼和问user要做什么
                                bookedRooms += "\n\n" + databaseManager.greetings(usersMap.get(message.getChatId()).getICNO());

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

                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText("Oops! we can't find your information. Please enter again,\n\nExample: 990724070661@MyEmail@hotmail.com");
                            }
                        }else{
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter your IC and Email in correct format.\n\nExample: 990724070661@MyEmail@hotmail.com");
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

                }else if(data.equals("Book:Book_N")){
                    userState.put(message.getChatId(),"Start");
                    sendMessage.setText("I'll be here whenever you need me :)");
                    sendMessage.setChatId(message.getChatId());

                }else if(data.equals("Book:Verification")){
                    userState.put(message.getChatId(),"Book:School");
                    sendMessage.setText("Please enter your IC and Email for verification purpose.\n\n" +
                            "Example: 990724070661@MyEmail@hotmail.com");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:FillIC")) {
                    userState.put(message.getChatId(),"Book:Email");
                    sendMessage.setText("This IC had been used by others, please use another IC\n\n" +
                            "Example: 990724070661");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Conf_N")) {
                    //Display information to double check
                    String info = "Which information do you want to change\n" +
                            "\nName: " + usersMap.get(message.getChatId()).getName() +
                            "\nIC: " + usersMap.get(message.getChatId()).getICNO() +
                            "\nEmail: " + usersMap.get(message.getChatId()).getEmail() +
                            "\nStaff ID: " + usersMap.get(message.getChatId()).getStaffID() +
                            "\nTel No.: " + usersMap.get(message.getChatId()).getTelNo();


                    sendMessage = new SendMessage();
                    sendMessage.setText(info);
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
                    InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("Name");
                    inlineKeyboardButton2.setText("IC");
                    inlineKeyboardButton3.setText("Email");
                    inlineKeyboardButton4.setText("Staff ID");
                    inlineKeyboardButton5.setText("Tel No.");
                    inlineKeyboardButton1.setCallbackData("Book:Chan_Name");
                    inlineKeyboardButton2.setCallbackData("Book:Chan_IC");
                    inlineKeyboardButton3.setCallbackData("Book:Chan_Email");
                    inlineKeyboardButton4.setCallbackData("Book:Chan_StaffID");
                    inlineKeyboardButton5.setCallbackData("Book:Chan_TelNo");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton4);
                    inlineKeyboardButtonList3.add(inlineKeyboardButton5);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineButtons.add(inlineKeyboardButtonList2);
                    inlineButtons.add(inlineKeyboardButtonList3);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                }else if(data.equals("Book:Chan_Name") || data.equals("Book:Chan_IC") ||data.equals("Book:Chan_Email") ||data.equals("Book:Chan_StaffID") ||data.equals("Book:Chan_TelNo")) {
                    if (data.equals("Book:Chan_Name")) {
                        userState.put(message.getChatId(), "Book:Chan_Name");
                        sendMessage.setText("What do you want to change your name to?\n\n" +
                                "Example: Ang Toon Ph'ng");
                    } else if (data.equals("Book:Chan_IC")) {
                        userState.put(message.getChatId(), "Book:Chan_IC");
                        sendMessage.setText("What do you want to change your ic to?\n\n" +
                                "Example: 990724070661");
                    } else if (data.equals("Book:Chan_Email")) {
                        userState.put(message.getChatId(), "Book:Chan_Email");
                        sendMessage.setText("What do you want to change your email to?\n\n" +
                                "Example: MyEmail@hotmail.com");
                    } else if (data.equals("Book:Chan_StaffID")) {
                        userState.put(message.getChatId(), "Book:Chan_StaffID");
                        sendMessage.setText("What do you want to change your staff id to?\n\n" +
                                "Example: abc123");
                    } else if (data.equals("Book:Chan_TelNo")) {
                        userState.put(message.getChatId(), "Book:Chan_TelNo");
                        sendMessage.setText("What do you want to change your mobile number to?\n\n" +
                                "Example: 0124773579");
                    }

                    sendMessage.setChatId(message.getChatId());
                }else if(data.equals("Book:Conf_Y")){
                    databaseManager.insertUser(usersMap.get(message.getChatId()).getName(), usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getEmail(), usersMap.get(message.getChatId()).getStaffID(), usersMap.get(message.getChatId()).getTelNo());

                    Date date = new Date();
                    bookingMap.put(message.getChatId(),new Booking(date, date, date, 0, "", "", 0, databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO())));

                    userState.put(message.getChatId(),"Book:Room");

                    String list = databaseManager.schoolList();
                    list+="Excellent! Which school do you wish to book in?\nExample reply: 1";

                    sendMessage.setText(list);
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Room_Conf_N")) {

                    Date date = new Date();
                    bookingMap.put(message.getChatId(),new Booking(date, date, date, 0, "", "", 0, databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO())));
                    userState.put(message.getChatId(),"Book:Room");

                    String list = databaseManager.schoolList();
                    list+="Which school do you wish to book in?\nExample reply: 1";

                    sendMessage.setText(list);
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Room_Conf_Y")) {
                    userState.put(message.getChatId(), "Book:StartTime");
                    sendMessage.setText("Please enter the date that you want to book this room.\n\n" +
                            "Example: 27/04/2023");
                    sendMessage.setChatId(message.getChatId());
                }


            }

            else if(buttonData[0].equals("Login")){

            }

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }


}
