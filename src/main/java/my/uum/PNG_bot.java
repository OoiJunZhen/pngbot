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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    /**
     * Hashmap for adding room information
     */
    Map<Long, Room> RegisterRoomMap = new HashMap<Long, Room>();

    /**
     * Hashmap for adding roomlist information
     */
    Map<Long, RoomList> roomListMap = new HashMap<Long, RoomList>();

    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        DatabaseManager databaseManager = new DatabaseManager();
        InputFormatChecker inputFormatChecker = new InputFormatChecker();


        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(message.getChatId() + " " + message.getText());


            if (!userState.containsKey(message.getChatId())) {
                userState.put(message.getChatId(), "Start");
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
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));

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
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));
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

                case "/registeruser":
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));
                    userState.put(message.getChatId(), "Register");
                    String msg10 = "Do you want register an account?";
                    sendMessage = new SendMessage();
                    sendMessage.setText(msg10);
                    //sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup10 = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons10 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList10 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton10 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton11 = new InlineKeyboardButton();
                    inlineKeyboardButton10.setText("Yes");
                    inlineKeyboardButton11.setText("No");
                    inlineKeyboardButton10.setCallbackData("Register:Register_Y");
                    inlineKeyboardButton11.setCallbackData("Register:Register_N");
                    inlineKeyboardButtonList10.add(inlineKeyboardButton10);
                    inlineKeyboardButtonList10.add(inlineKeyboardButton11);
                    inlineButtons10.add(inlineKeyboardButtonList10);
                    inlineKeyboardMarkup10.setKeyboard(inlineButtons10);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup10);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/roomlist":
                    roomListMap.put(message.getChatId(),new RoomList("",0));
                    userState.put(message.getChatId(),"RoomList:Date_School");
                    sendMessage.setText("Please input the day that you want to view available rooms.\n\n" +
                            "Example: 27/04/2023");

                    try {
                        sendMessage.setChatId(message.getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "/sysaccess":
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));
                    userState.put(message.getChatId(),"System:Main");
                    sendMessage.setText("Please enter the verification code to access the system.");
                    try {
                        sendMessage.setChatId(message.getChatId());
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                break;

                case "/test":
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));
                    userState.put(message.getChatId(), "Test:Input");
                    sendMessage = new SendMessage();
                    sendMessage.setText("Enter Date\n\nExample: 23/02/2023");

                    //sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup11 = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons11 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList11 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton12 = new InlineKeyboardButton();
                    inlineKeyboardButton12.setText("Go Resign");
                    inlineKeyboardButton12.setCallbackData("System:Resign");
                    inlineKeyboardButtonList11.add(inlineKeyboardButton12);
                    inlineButtons11.add(inlineKeyboardButtonList11);
                    inlineKeyboardMarkup11.setKeyboard(inlineButtons11);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup11);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

            }


            //Go to check state if State have the first word as 'Test:'
            if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Test:")) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();

                try {
                    Date bookDate = sdf.parse(message.getText());

                    if (databaseManager.checkBook(7, message.getText())) {
                        sendMessage.setText("True");
                    } else {
                        sendMessage.setText("I'm here");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sendMessage.setChatId(message.getChatId());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //Go to check state if State have the first word as 'Book'
            else if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Book:")) {
                //Get User State
                switch (userState.get(message.getChatId())) {
                    case "Book:IC":
                        if (inputFormatChecker.NameFormat(message.getText())) {
                            //save user 在 Book:Book_Y 之后input的内容起来，进object
                            usersMap.get(message.getChatId()).setName(message.getText());

                            //set新的State
                            userState.put(message.getChatId(), "Book:Email");
                            sendMessage.setText("How about your NRIC number?\nExample: 001211080731");

                        } else {
                            sendMessage.setText("Please re-enter your name.");
                        }

                        break;

                    case "Book:Email":
                        if (inputFormatChecker.checkICFormat(message.getText())) {
                            usersMap.get(message.getChatId()).setICNO(message.getText());
                            //if database has user
                            if (databaseManager.checkUser(message.getText())) {
                                String text = databaseManager.displayUserInfo(usersMap.get(message.getChatId()).getICNO());

                                sendMessage = new SendMessage();
                                sendMessage.setText(text);
                                sendMessage.setParseMode(ParseMode.MARKDOWN);

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
                            }

                        } else {
                            sendMessage.setText("Please enter your IC in correct format thank you :).");
                        }

                        break;

                    case "Book:StaffID":
                        if (inputFormatChecker.EmailFormat(message.getText())) {
                            usersMap.get(message.getChatId()).setEmail(message.getText());
                            userState.put(message.getChatId(), "Book:Mobile");
                            sendMessage.setText("Almost There! How about your Staff ID?\n\nExample: abc123");
                            sendMessage.setChatId(message.getChatId());
                        } else {
                            sendMessage.setText("Please enter your email in correct format, thank you :)");
                        }

                        break;

                    case "Book:Mobile":
                        usersMap.get(message.getChatId()).setStaffID(message.getText());
                        userState.put(message.getChatId(), "Book:Confirmation");

                        sendMessage.setText("What is the best contact number to reach you? \n\n Example: 0124773579");

                        break;

                    case "Book:Confirmation":
                    case "Book:Chan_Name":
                    case "Book:Chan_IC":
                    case "Book:Chan_Email":
                    case "Book:Chan_StaffID":
                    case "Book:Chan_TelNo":

                        boolean output = false;
                        if (userState.get(message.getChatId()).equals("Book:Confirmation") || userState.get(message.getChatId()).equals("Book:Chan_TelNo")) {
                            if (inputFormatChecker.TelNumFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setTelNo(message.getText());
                                output = true;
                            } else {
                                sendMessage.setText("Please enter your phone number in correct format thank you.\n\n" +
                                        "Example: 0124773579");
                            }
                        } else if (userState.get(message.getChatId()).equals("Book:Chan_Name")) {
                            if (inputFormatChecker.NameFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setName(message.getText());
                                output = true;
                            } else {
                                sendMessage.setText("Please re-enter your name.\n\n" +
                                        "Example: Ang Toon Phng");
                            }

                        } else if (userState.get(message.getChatId()).equals("Book:Chan_IC")) {

                            //check IC format
                            if (inputFormatChecker.checkICFormat(message.getText())) {

                                //check if user exist in the database
                                if (!databaseManager.checkUser(message.getText())) {
                                    usersMap.get(message.getChatId()).setICNO(message.getText());
                                    output = true;
                                } else {
                                    //if IC already used, then it is invalid
                                    sendMessage.setText("Sorry, this IC had been used by someone else, please enter another IC number\n\n" +
                                            "Example: 001211080731");
                                }
                            } else {
                                sendMessage.setText("Please re-enter your IC in correct format thank you.\n\n" +
                                        "Example: 001211080731");
                            }
                        } else if (userState.get(message.getChatId()).equals("Book:Chan_StaffID")) {
                            usersMap.get(message.getChatId()).setStaffID(message.getText());
                            output = true;

                        } else if (userState.get(message.getChatId()).equals("Book:Chan_Email")) {

                            //check email format
                            if (inputFormatChecker.EmailFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setEmail(message.getText());
                                output = true;
                            } else {
                                sendMessage.setText("Please re-enter the email in correct format thank you.\n\n" +
                                        "Example: MyEmail@hotmail.com");
                            }
                        }

                        if (output) {
                            String info = "Are these the correct information?\n" +
                                    "\nName: " + usersMap.get(message.getChatId()).getName() +
                                    "\nIC: " + usersMap.get(message.getChatId()).getICNO() +
                                    "\nEmail: " + usersMap.get(message.getChatId()).getEmail() +
                                    "\nStaff ID: " + usersMap.get(message.getChatId()).getStaffID() +
                                    "\nTel No.: " + usersMap.get(message.getChatId()).getTelNo();


                            sendMessage = new SendMessage();
                            sendMessage.setText(info);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

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
                        if (message.getText().contains("@")) {
                            String[] password = message.getText().split("@", 2);

                            if (databaseManager.passwordCheck(password[0], password[1])) {

                                Date date = new Date();
                                bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, password[0]));
                                userState.put(message.getChatId(), "Book:Room");

                                String list = databaseManager.schoolBookList();
                                list += "Excellent! Which school do you wish to book in?\nExample reply: 1";

                                sendMessage.setText(list);

                            } else {
                                sendMessage.setText("We can't find you in the database. Please re-enter your IC and Email thank you.\n\n" +
                                        "Example: 990724070661@MyEmail@hotmail.com");
                            }
                        } else {
                            sendMessage.setText("Incorrect format, please re-enter your IC and Email thank you.\n\n" +
                                    "Example: 990724070661@MyEmail@hotmail.com");
                        }


                        break;

                    case "Book:Room":
                        boolean schoolIDCheck = false;
                        try{
                            Integer.parseInt(message.getText());
                            schoolIDCheck=true;
                        }catch(NumberFormatException e){
                            System.out.println("Wrong School ID input");
                            sendMessage.setText("Please enter a  number.");
                        }

                        if(schoolIDCheck){
                            if(databaseManager.SchoolHaveRoom(Integer.parseInt(message.getText()))){
                                if (databaseManager.checkSchool(message.getText())&&databaseManager.SchoolHaveRoom(Integer.parseInt(message.getText()))) {
                                    userState.put(message.getChatId(), "Book:RoomDetails");
                                    bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));
                                    String roomList = databaseManager.getBookRoomList(bookingMap.get(message.getChatId()).getRoomID());

                                    roomList += "Which room do you want to book?\nExample reply: 1";
                                    sendMessage.setText(roomList);

                                } else {
                                    String list2 = databaseManager.schoolBookList();
                                    list2 += "This school does not exist. Please re-enter the school that you wish to book in.\nExample reply: 1";

                                    sendMessage.setText(list2);
                                }
                            }
                        }

                        break;

                    case "Book:RoomDetails":
                        if (databaseManager.checkRoom(message.getText(), bookingMap.get(message.getChatId()).getRoomID())) {
                            //update roomID in hashmap to actual room ID (Initially it is School ID)
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));

                            //display room information in details
                            String list1 = databaseManager.getRoomInfo(bookingMap.get(message.getChatId()).getRoomID());

                            sendMessage = new SendMessage();
                            sendMessage.setText(list1);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

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


                        } else {
                            String list2 = databaseManager.getBookRoomList(bookingMap.get(message.getChatId()).getRoomID());
                            list2 += "This room does not exist. Please re-enter the room that you wish to book.\n\nExample reply: 1";

                            sendMessage.setText(list2);
                        }

                        break;
                    case "Book:StartTime":
                        String text = "";
                        if (inputFormatChecker.DateFormat(message.getText())) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();

                            try {
                                Date bookDate = sdf.parse(message.getText());

                                if (inputFormatChecker.bookDate(bookDate, date)) {
                                    userState.put(message.getChatId(), "Book:EndTime");
                                    bookingMap.get(message.getChatId()).setTemp(message.getText());

                                    //if that day, the room got booking
                                    if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())){
                                        text += "Booked time:\n";
                                        //display booked Time
                                        text += databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), message.getText()) + "\n";
                                    }

                                    text += "When do you want to start using the room?(In 24 hours time format)\n\nExample: 08:30";
                                    sendMessage.setText(text);


                                } else {
                                    sendMessage.setText("The booking date needs to be at least 1 month prior and also can't be made if that day is over 1 year away.\n" +
                                            "As an example, to book a day in April 1st, booking needs to be made on March 1st or before.\n\n" +
                                            "Please enter the day you would like to book\n\nExample: 27/04/2023");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } else {
                            sendMessage.setText("Please enter the date in correct format.\n\n" +
                                    "Example: 27/04/2023");
                        }

                        break;

                    case "Book:EndTime":

                        if (inputFormatChecker.timeFormat(message.getText())) {

                            if (inputFormatChecker.timeOpen(message.getText())) {

                                //if that day got booking
                                if(databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(),bookingMap.get(message.getChatId()).getTemp())){
                                    System.out.println("this date got time booked in this room");

                                    //if the time chosen does not contradict with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp(), message.getText())) {

                                        SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        try {
                                            userState.put(message.getChatId(), "Book:Purpose");
                                            Date dateTemp = combine.parse(bookingMap.get(message.getChatId()).getTemp() + " " + message.getText());

                                            //save starting time
                                            bookingMap.get(message.getChatId()).setStartDate(dateTemp);
                                            sendMessage.setText("When will the booking end?\n\n" +
                                                    "Example: 18:30");

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            System.out.println("At Book:EndTime");
                                        }

                                    } else {

                                        sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                + "\nPlease choose a time that is not booked.");
                                    }
                                }else{

                                    SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    try {
                                        System.out.println("this date doesn't have time booked");
                                        userState.put(message.getChatId(), "Book:Purpose");
                                        Date dateTemp = combine.parse(bookingMap.get(message.getChatId()).getTemp() + " " + message.getText());

                                        //save starting time
                                        bookingMap.get(message.getChatId()).setStartDate(dateTemp);
                                        sendMessage.setText("When will the booking end?\n\n" +
                                                "Example: 18:30");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        System.out.println("At Book:EndTime");
                                    }

                                }

                            } else {
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.(In 24 hours time format)\n\n" +
                                        "Example: 08:30");
                            }

                        } else {
                            sendMessage.setText("Please enter the time in correct format.(In 24 hours time format)\n\n" +
                                    "Example: 08:30");
                        }


                        break;

                    case "Book:Purpose":

                        Date dateTemp = new Date();

                        if (inputFormatChecker.timeFormat(message.getText())) {

                            //check whether the time is within 8AM to 8PM
                            if (inputFormatChecker.timeOpen(message.getText())) {
                                SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                try {
                                    dateTemp = combine.parse(bookingMap.get(message.getChatId()).getTemp() + " " + message.getText());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                //check whether the end time is before start time
                                if (dateTemp.after(bookingMap.get(message.getChatId()).getStartDate())) {

                                    //check whether the time contradicts with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp(), message.getText()) && !databaseManager.checkTimeDatabase2(bookingMap.get(message.getChatId()).getRoomID(),bookingMap.get(message.getChatId()).getTemp(),bookingMap.get(message.getChatId()).getStartDate(),message.getText())){

                                        bookingMap.get(message.getChatId()).setEndDate(dateTemp);
                                        userState.put(message.getChatId(), "Book:Booking_Confirmation");
                                        sendMessage.setText("Excellent! Please state your booking purpose.\n\n" +
                                                "Example: Club meeting.");

                                    } else {
                                        sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                + "\nPlease choose a time that is not booked.");
                                    }

                                } else {
                                    sendMessage.setText("The booking end time cannot be before start time. Please enter your end time.(In 24 hours time format)\n\n" +
                                            "Example: 14:30");

                                }

                            } else {
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.(In 24 hours time format)\n\n" +
                                        "Example: 08:30");
                            }
                        } else {
                            sendMessage.setText("Please enter the time in correct format.(In 24 hours time format)\n\n" +
                                    "Example: 08:30");
                        }

                        break;

                    case "Book:Booking_Confirmation":
                    case "Book:Chan_Date":
                    case "Book:Chan_StartTime":
                    case "Book:Chan_EndTime":
                    case "Book:Chan_Purpose":
                        String info5;
                        boolean success = false;
                        //Variables to assign the date
                        Date dateTemp2 = new Date();

                        //This is for current day and time
                        Date date = new Date();


                        if (userState.get(message.getChatId()).equals("Book:Chan_Date")) {
                            if (inputFormatChecker.DateFormat(message.getText())) {
                                //Variables to back up the time
                                String startTime;
                                String endTime;

                                //This is for combining String input which is Date and String time which is backed up by Start Time and End Time
                                SimpleDateFormat startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                //This is for separating and assign the initial time of Date in hashmap Date object into startTime and endTime
                                DateFormat dateFormat = new SimpleDateFormat("HH:mm");

                                //Save the original Time as String into both variables
                                startTime = dateFormat.format(bookingMap.get(message.getChatId()).getStartDate());
                                endTime = dateFormat.format(bookingMap.get(message.getChatId()).getEndDate());

                                //Combining date from input and startTime into dateTemp
                                try {
                                    dateTemp2 = startDate.parse(message.getText() + " " + startTime);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                //Check whether the date is prior one month or before a maximum of 1 year
                                if (inputFormatChecker.bookDate(dateTemp2, date)) {

                                    //check whether there are people who book this day
                                    if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), message.getText())) {

                                        //check whether the start and end time contradicts with booked time
                                        if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), message.getText(), startTime) &&
                                                !databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), message.getText(), endTime)) {

                                            //if no, Save dateTemp2 into start date
                                            bookingMap.get(message.getChatId()).setStartDate(dateTemp2);

                                            //override dateTemp2 into end date
                                            try {
                                                dateTemp2 = startDate.parse(message.getText() + " " + endTime);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }

                                            //Save dateTemp2 into end date
                                            bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                            success = true;

                                        } else {
                                            sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                    + "\nPlease choose a date that does not contradict the booking time.\n If you still wish to book this day, you can change the time first.");
                                        }

                                    } else {
                                        //if no, Save dateTemp2 into start date
                                        bookingMap.get(message.getChatId()).setStartDate(dateTemp2);

                                        //override dateTemp2 into end date
                                        try {
                                            dateTemp2 = startDate.parse(message.getText() + " " + endTime);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }

                                        //Save dateTemp2 into end date
                                        bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                        success = true;
                                    }

                                } else {
                                    sendMessage.setText("The booking date needs to be at least 1 month prior and also can't be made if that day is over 1 year away.\n" +
                                            "As an example, to book a day in April 1st, booking needs to be made on March 1st or before.\n\n" +
                                            "Please enter the day you would like to book\n\nExample: 27/04/2023");
                                }

                            } else {
                                sendMessage.setText("Please enter the date in correct format.(In 24 hours time format)\n\n" +
                                        "Example: 27/04/2023");
                            }

                        } else if (userState.get(message.getChatId()).equals("Book:Chan_StartTime")) {

                            if (inputFormatChecker.timeFormat(message.getText())) {

                                if (inputFormatChecker.timeOpen(message.getText())) {

                                    //This is for separating and assign the initial date of Starting Date in hashmap Date object into dateTemp
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                    //This is for combining String input which is Date and String time which is backed up by Start Time and End Time
                                    SimpleDateFormat startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                    try {
                                        //Save dd/MM/yyyy into newDate
                                        String newDate = dateFormat.format(bookingMap.get(message.getChatId()).getStartDate());

                                        //combine newDate and message
                                        dateTemp2 = startDate.parse(newDate + " " + message.getText());

                                        //Check whether the end date is after the new start date
                                        if (bookingMap.get(message.getChatId()).getEndDate().after(dateTemp2)) {

                                            if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), newDate)) {

                                                if (databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), newDate, message.getText())) {
                                                    //assign dateTemp as new Date
                                                    bookingMap.get(message.getChatId()).setStartDate(dateTemp2);
                                                    success = true;

                                                } else {
                                                    sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                            + "\nPlease choose a time that is not booked.");
                                                }
                                            } else {
                                                //assign dateTemp as new Date
                                                bookingMap.get(message.getChatId()).setStartDate(dateTemp2);
                                                success = true;
                                            }

                                        } else {
                                            sendMessage.setText("The new start time cannot be after the initial end time :(\n" +
                                                    "Please Re-enter the Time thank you.(In 24 hours time format)" +
                                                    "\n\nExample: 08:30");
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.(In 24 hours time format)\n\n" +
                                            "Example: 08:30");
                                }

                            } else {
                                sendMessage.setText("Please enter the time in correct format.(In 24 hours time format)\n\n" +
                                        "Example: 08:30");
                            }


                        } else if (userState.get(message.getChatId()).equals("Book:Chan_EndTime")) {

                            if (inputFormatChecker.timeFormat(message.getText())) {

                                //This is for separating and assign the initial date of Starting Date in hashmap Date object into dateTemp
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                //This is for combining String input which is Date and String time which is backed up by Start Time and End Time
                                SimpleDateFormat startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                try {
                                    //Save dd/MM/yyyy into newDate
                                    String newDate = dateFormat.format(bookingMap.get(message.getChatId()).getEndDate());

                                    //combine newDate and message
                                    dateTemp2 = startDate.parse(newDate + " " + message.getText());

                                    //Check whether the start date is before the new end date
                                    if (bookingMap.get(message.getChatId()).getStartDate().before(dateTemp2)) {

                                        if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), newDate)) {

                                            if (databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), newDate, message.getText())) {
                                                //assign dateTemp as new Date
                                                bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                                success = true;

                                            } else {
                                                sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                        + "\nPlease choose a time that is not booked.");
                                            }
                                        } else {
                                            //assign dateTemp as new Date
                                            bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                            success = true;
                                        }

                                    } else {
                                        sendMessage.setText("The new end time cannot be before the initial start time :(\n" +
                                                "Please Re-enter the Time thank you.(In 24 hours time format)" +
                                                "\n\nExample: 08:30");
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    success = false;
                                }

                            } else {
                                sendMessage.setText("Please enter the time in correct format.(In 24 hours time format)\n\n" +
                                        "Example: 08:30");
                            }

                        }

                        if (userState.get(message.getChatId()).equals("Book:Booking_Confirmation") || userState.get(message.getChatId()).equals("Book:Chan_Purpose")) {
                            if (inputFormatChecker.NameFormat(message.getText())) {
                                bookingMap.get(message.getChatId()).setBookPurpose(message.getText());
                                success = true;
                            } else {
                                sendMessage.setText("Please re-enter the booking purpose thank you.");
                            }
                        }

                        if (success || userState.get(message.getChatId()).equals("Book:Booking_Confirmation")) {
                            //Display Booking Information for confirmation
                            SimpleDateFormat ForDay = new SimpleDateFormat("EEEE");
                            SimpleDateFormat ForDate = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat ForStartTime = new SimpleDateFormat("hh:mm a");

                            //find booking duration
                            long difference_In_Time = bookingMap.get(message.getChatId()).getEndDate().getTime() - bookingMap.get(message.getChatId()).getStartDate().getTime();
                            long difference_In_Minutes
                                    = TimeUnit
                                    .MILLISECONDS
                                    .toMinutes(difference_In_Time)
                                    % 60;
                            long difference_In_Hours
                                    = TimeUnit
                                    .MILLISECONDS
                                    .toHours(difference_In_Time)
                                    % 24;

                            info5 = "DotW: " + ForDay.format(bookingMap.get(message.getChatId()).getStartDate());
                            info5 += "\nRoom: " + databaseManager.getRoomName(bookingMap.get(message.getChatId()).getRoomID());
                            info5 += "\nDate: " + ForDate.format(bookingMap.get(message.getChatId()).getStartDate());
                            info5 += "\nStart Time: " + ForStartTime.format(bookingMap.get(message.getChatId()).getStartDate());
                            info5 += "\nEnd Time: " + ForStartTime.format(bookingMap.get(message.getChatId()).getEndDate()) + "\nDuration: ";
                            if (difference_In_Hours >= 1) {
                                info5 += difference_In_Hours + "hour(s) ";
                            }
                            if (difference_In_Minutes >= 1) {
                                info5 += difference_In_Minutes + "minute(s)";
                            }
                            info5 += "\nBooking Purpose: " + bookingMap.get(message.getChatId()).getBookPurpose();
                            info5 += "\n\nAre these correct?";


                            sendMessage = new SendMessage();
                            sendMessage.setText(info5);
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList5 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList6 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                            inlineKeyboardButton5.setText("Yes");
                            inlineKeyboardButton6.setText("No I would like to change something");
                            inlineKeyboardButton5.setCallbackData("Book:Booking_Conf_Y");
                            inlineKeyboardButton6.setCallbackData("Book:Booking_Conf_N");
                            inlineKeyboardButtonList5.add(inlineKeyboardButton5);
                            inlineKeyboardButtonList6.add(inlineKeyboardButton6);
                            inlineButtons2.add(inlineKeyboardButtonList5);
                            inlineButtons2.add(inlineKeyboardButtonList6);
                            inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup2);
                        }

                        break;

                }


                try {
                    sendMessage.setChatId(message.getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //Go to check state if State have the first word as 'Login'
            else if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Login:")) {
                //Get user state
                switch (userState.get(message.getChatId())) {
                    case "Login:Verification":
                        //seperate input from user to recognize the IC and Email Inputted by user
                        String[] password = message.getText().split("@", 2);
                        //if password verification is true
                        if (message.getText().contains("@")) {
                            if (databaseManager.passwordCheck(password[0], password[1])) {
    
                                //先把Password里的IC放进去usersMap
                                usersMap.get(message.getChatId()).setICNO(password[0]);
                                usersMap.get(message.getChatId()).setEmail(password[1]);

                                if(databaseManager.getUserRole(usersMap.get(message.getChatId()).getICNO()).equals("User")){
                                    //用userID 来找user的booking记录
                                    String bookedRooms = databaseManager.viewBooked(password[0], "view");

                                    //打招呼和问user要做什么
                                    bookedRooms += "\n\n" + databaseManager.greetings(usersMap.get(message.getChatId()).getICNO());

                                    sendMessage = new SendMessage();
                                    sendMessage.setText(bookedRooms);
                                    sendMessage.setParseMode(ParseMode.MARKDOWN);

                                    //Inline Keyboard Button
                                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList3 = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList4 = new ArrayList<>();
                                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                                    inlineKeyboardButton1.setText("View Booking Details");
                                    inlineKeyboardButton2.setText("Edit Booking");
                                    inlineKeyboardButton3.setText("Cancel Booking");
                                    inlineKeyboardButton4.setText("Edit Profile");
                                    inlineKeyboardButton5.setText("Register as School Admin");
                                    inlineKeyboardButton1.setCallbackData("Login:ViewBook");
                                    inlineKeyboardButton2.setCallbackData("Login:EditBook");
                                    inlineKeyboardButton3.setCallbackData("Login:CancelBook");
                                    inlineKeyboardButton4.setCallbackData("Login:EditProfile");
                                    inlineKeyboardButton5.setCallbackData("Login:RegisterSchoolAdmin");
                                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                    if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                                        //if user have booking{
                                        inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                        inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                                    }
                                    inlineKeyboardButtonList3.add(inlineKeyboardButton4);
                                    inlineKeyboardButtonList4.add(inlineKeyboardButton5);
                                    inlineButtons.add(inlineKeyboardButtonList1);
                                    if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                                        inlineButtons.add(inlineKeyboardButtonList2);
                                    }
                                    inlineButtons.add(inlineKeyboardButtonList3);
                                    inlineButtons.add(inlineKeyboardButtonList4);
                                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                                } else if (databaseManager.getUserRole(usersMap.get(message.getChatId()).getICNO()).equals("School Admin")) {
                                    if(databaseManager.checkOfficeNum(usersMap.get(message.getChatId()).getICNO())){
                                        String output = "School Name: " + databaseManager.getSchoolName(usersMap.get(message.getChatId()).getICNO()) + "\n";
                                        output += databaseManager.getSchoolAdRoomList(usersMap.get(message.getChatId()).getICNO());

                                        output += databaseManager.greetings(usersMap.get(message.getChatId()).getICNO());

                                        sendMessage = new SendMessage();
                                        sendMessage.setText(output);
                                        sendMessage.setParseMode(ParseMode.MARKDOWN);

                                        //Inline Keyboard Button
                                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList3 = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList4 = new ArrayList<>();

                                        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                                        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
                                        inlineKeyboardButton1.setText("View Booking Details");
                                        inlineKeyboardButton2.setText("Edit Booking");
                                        inlineKeyboardButton3.setText("Cancel Booking");
                                        inlineKeyboardButton4.setText("Edit Profile");
                                        inlineKeyboardButton5.setText("Add Room");
                                        inlineKeyboardButton6.setText("Edit Room");
                                        inlineKeyboardButton7.setText("Delete Room");
                                        inlineKeyboardButton1.setCallbackData("Login:ViewBook");
                                        inlineKeyboardButton2.setCallbackData("Login:EditBook");
                                        inlineKeyboardButton3.setCallbackData("Login:CancelBook");
                                        inlineKeyboardButton4.setCallbackData("Login:EditProfile");
                                        inlineKeyboardButton5.setCallbackData("Login:AddRoom");
                                        inlineKeyboardButton6.setCallbackData("Login:EditRoom");
                                        inlineKeyboardButton7.setCallbackData("Login:DeleteRoom");

                                        inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                        if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                                            //if user have booking{
                                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                            inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                                        }
                                        inlineKeyboardButtonList3.add(inlineKeyboardButton4);
                                        inlineKeyboardButtonList4.add(inlineKeyboardButton5);
                                        inlineKeyboardButtonList4.add(inlineKeyboardButton6);
                                        inlineKeyboardButtonList4.add(inlineKeyboardButton7);

                                        inlineButtons.add(inlineKeyboardButtonList1);
                                        if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                                            inlineButtons.add(inlineKeyboardButtonList2);
                                        }
                                        inlineButtons.add(inlineKeyboardButtonList3);
                                        inlineButtons.add(inlineKeyboardButtonList4);
                                        inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);


                                    }else{
                                        userState.put(message.getChatId(),"Login:SchoolAd_AddRoom");
                                        sendMessage.setText("Congratulation on becoming school admin of " + databaseManager.getSchoolName(usersMap.get(message.getChatId()).getICNO())
                                        + "!\n\nPlease add office number to complete your school admin's information.\n" +
                                                "What is the best Office contact number to reach you?");
                                    }
                                }


                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setText("Oops! we can't find your information. Please enter again,\n\nExample: 990724070661@MyEmail@hotmail.com");
                            }
                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setText("Please enter your IC and Email in correct format.\n\nExample: 990724070661@MyEmail@hotmail.com");
                        }

                        break;

                    case "Login:EditProfile_Name":
                    case "Login:EditProfile_Email":
                    case "Login:EditProfile_StaffID":
                    case "Login:EditProfile_Mobile":

                        boolean editOutput = false;
                        if (userState.get(message.getChatId()).equals("Login:EditProfile_Name") ) {
                            if (inputFormatChecker.NameFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setName(message.getText());
                                databaseManager.editProfileName(usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getName());
                                editOutput = true;
                            } else {
                                sendMessage.setText("Please re-enter your name.\n\n" +
                                        "Example: Ang Toon Phng");
                            }

                        } else if (userState.get(message.getChatId()).equals("Login:EditProfile_Email")) {
                            if (inputFormatChecker.EmailFormat(message.getText())) {
                                System.out.println("Hello");
                                usersMap.get(message.getChatId()).setEmail(message.getText());
                                databaseManager.editProfileEmail(usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getEmail());
                                editOutput = true;
                            } else {
                                sendMessage.setText("Please re-enter the email in correct format thank you.\n\n" +
                                        "Example: MyEmail@hotmail.com");
                            }
                        } else if (userState.get(message.getChatId()).equals("Login:EditProfile_StaffID")) {

                            usersMap.get(message.getChatId()).setStaffID(message.getText());
                            databaseManager.editProfileStaffID(usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getStaffID());
                            editOutput = true;

                        } else if (userState.get(message.getChatId()).equals("Login:EditProfile_Mobile")) {
                            if (inputFormatChecker.TelNumFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setTelNo(message.getText());
                                databaseManager.editProfileTelNo(usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getTelNo());
                                editOutput = true;
                            } else {
                                sendMessage.setText("Please enter your phone number in correct format thank you.\n\n" +
                                        "Example: 0124773579");
                            }
                        }

                        if (editOutput) {

                            String info = databaseManager.userProfile(usersMap.get(message.getChatId()).getICNO(), "edit");

                            sendMessage.setText(info);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);


                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("Yes");
                            inlineKeyboardButton2.setText("No, Return to Main Menu");
                            inlineKeyboardButton1.setCallbackData("Login:EditProfile");
                            inlineKeyboardButton2.setCallbackData("Login:Main");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        }

                        break;

                    case "Login:CancelBook":
                        if (userState.get(message.getChatId()).equals("Login:CancelBook") ) {

                            if (databaseManager.checkBookId(usersMap.get(message.getChatId()).getICNO(), message.getText())) {
                                Date date = new Date();
                                bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, usersMap.get(message.getChatId()).getICNO()));
//                                String del = databaseManager.getBookList(bookingMap.get(message.getChatId()).getBookID());
                                bookingMap.get(message.getChatId()).setBookID(Integer.parseInt(message.getText()));
                                String del = databaseManager.getBookList(bookingMap.get(message.getChatId()).getBookID());
                                del += "\n\nAre you sure you want to delete this room?";
                                sendMessage = new SendMessage();
                                sendMessage.setText(del);
                                sendMessage.setParseMode(ParseMode.MARKDOWN);

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                inlineKeyboardButton1.setText("Yes");
                                inlineKeyboardButton2.setText("No, Return to Main Menu");
                                inlineKeyboardButton1.setCallbackData("Login:CancelBook_confirm");
                                inlineKeyboardButton2.setCallbackData("Login:Main");
                                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                inlineButtons.add(inlineKeyboardButtonList1);
                                inlineButtons.add(inlineKeyboardButtonList2);
                                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            }else {
                                String list = databaseManager.viewBookedList(usersMap.get(message.getChatId()).getICNO(), "start");
                                list += "This booking id does not exist. Please re-enter the booking id that you wish to delete.\n\nExample reply: 1";
                                sendMessage = new SendMessage();
                                sendMessage.setText(list);
                            }
                        }
                        break;

                    case "Login:EditBook_Menu":
                        if (databaseManager.checkBookId(usersMap.get(message.getChatId()).getICNO(), message.getText())) {
                            bookingMap.get(message.getChatId()).setBookID(Integer.parseInt(message.getText()));
                            //System.out.println("BOOK ID" + bookingMap.get(message.getChatId()).getBookID());
                            String bookingList = databaseManager.getBookList(Integer.parseInt(message.getText()));

                            bookingList += "What do you want to edit?";
                            sendMessage = new SendMessage();
                            sendMessage.setText(bookingList);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);
                            sendMessage.setChatId(message.getChatId());

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("Booking Location");
                            inlineKeyboardButton2.setText("Booking Date");
                            inlineKeyboardButton3.setText("Booking Start Time");
                            inlineKeyboardButton1.setCallbackData("Login:EditBook_Location");
                            inlineKeyboardButton2.setCallbackData("Login:EditBook_Date");
                            inlineKeyboardButton3.setCallbackData("Login:EditBook_Time");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                        } else {

                            String list = databaseManager.viewBookedList(usersMap.get(message.getChatId()).getICNO(), "Start");
                            list += "This booking id does not exist. Please re-enter the booking id that you wish to edit.\n\nExample reply: 1";
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText(list);
                        }
                        break;


                    case "Login:EditBook_Location_Room":
                        if (databaseManager.checkSchool(message.getText())) {
                            userState.put(message.getChatId(), "Login:EditBook_Location_Check");
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            String roomList = databaseManager.getBookedRoomList(bookingMap.get(message.getChatId()).getRoomID(), usersMap.get(message.getChatId()).getICNO(), bookID);
                            roomList += "Which room do you want to change to?\nExample reply: 1";
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText(roomList);


                        } else {
                            String list2 = databaseManager.schoolList();
                            list2 += "This school does not exist. Please re-enter the school that you wish to book in.\nExample reply: 1";
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText(list2);
                        }
                        break;

                    case "Login:EditBook_Location_Check":
                        String list = "";
                        if (databaseManager.checkRoom(message.getText(), bookingMap.get(message.getChatId()).getRoomID())) {
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));
                            //display room information in details
                            list = databaseManager.getRoomDetail(bookingMap.get(message.getChatId()).getRoomID());
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            int roomID = bookingMap.get(message.getChatId()).getRoomID();
                            String dateTemp = databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);
                            //System.out.println(dateTemp);
                            //System.out.println(databaseManager.checkBook(roomID, dateTemp));
                            if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)) {
                                list += "\nDate: " + dateTemp + "\nBooked time:\n";
                                //display booked Time
                                list += databaseManager.checkbookedRoomTime(bookingMap.get(message.getChatId()).getRoomID(), usersMap.get(message.getChatId()).getICNO(), bookID);
                                String timeStartTemp = databaseManager.getBookedRoomTime(usersMap.get(message.getChatId()).getICNO(), bookID);

                                //if the time chosen contradict with other booked time
                                String list2 = "";
                                if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, timeStartTemp)) {
                                    list2 = "\nYour current booking time: \n" + databaseManager.viewBookedRoomTime(usersMap.get(message.getChatId()).getICNO(), bookID)
                                            + "\nAre you sure you want to book this room?";

                                    sendMessage = new SendMessage();
                                    sendMessage.setChatId(message.getChatId());
                                    sendMessage.setText(list + list2);

                                    //Inline Keyboard Button
                                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                    inlineKeyboardButton1.setText("Yes");
                                    inlineKeyboardButton2.setText("No, I want to choose another room");
                                    inlineKeyboardButton1.setCallbackData("Login:EditBook_Location_Update3");
                                    inlineKeyboardButton2.setCallbackData("Login:EditBook_Location");
                                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                    inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                    inlineButtons.add(inlineKeyboardButtonList1);
                                    inlineButtons.add(inlineKeyboardButtonList2);
                                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                                } else {
                                    sendMessage = new SendMessage();
                                    sendMessage.setChatId(message.getChatId());
                                    sendMessage.setText(list + "\n" + list2 + "\nYou cannot book this room with your initial booking time, if you want to book this room you can change your booking time");
                                    //Inline Keyboard Button
                                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                    inlineKeyboardButton1.setText("Yes. I want to change booking time");
                                    inlineKeyboardButton2.setText("No, change room");
                                    inlineKeyboardButton1.setCallbackData("Login:EditBook_Location_Time");
                                    inlineKeyboardButton2.setCallbackData("Login:EditBook_Location");
                                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                    inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                    inlineButtons.add(inlineKeyboardButtonList1);
                                    inlineButtons.add(inlineKeyboardButtonList2);
                                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                                }

                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText(list);

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                inlineKeyboardButton1.setText("Yes");
                                inlineKeyboardButton2.setText("No, I want to choose another room");
                                inlineKeyboardButton1.setCallbackData("Login:EditBook_Location_Update3");
                                inlineKeyboardButton2.setCallbackData("Login:EditBook_Location");
                                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                inlineButtons.add(inlineKeyboardButtonList1);
                                inlineButtons.add(inlineKeyboardButtonList2);
                                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                            }

                        } else {
                            list = databaseManager.getRoomList(bookingMap.get(message.getChatId()).getRoomID());
                            list += "This room does not exist. Please re-enter the room that you wish to book.\n\nExample reply: 1";
                            sendMessage = new SendMessage();
                            sendMessage.setText(list);
                            sendMessage.setChatId(message.getChatId());
                        }


                        break;

                    case "Login:EditBook_Location_Start":
                        if (inputFormatChecker.timeFormat(message.getText())) {

                            if (inputFormatChecker.timeOpen(message.getText())) {
                                int bookID = bookingMap.get(message.getChatId()).getBookID();
                                String dateTemp = databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);

                                //if that day got booking
                                if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)) {
                                    System.out.println("this date got time booked in this room");

                                    //if the time chosen does not contradict with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, message.getText())) {

                                        SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        try {
                                            userState.put(message.getChatId(), "Login:EditBook_Location_End");
                                            Date date = combine.parse(dateTemp + " " + message.getText());
                                            sendMessage = new SendMessage();
                                            sendMessage.setChatId(message.getChatId());
                                            //save starting time
                                            bookingMap.get(message.getChatId()).setStartDate(date);
                                            sendMessage.setText("How about your booking end time?\n\n" +
                                                    "Example: 17:30");

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            System.out.println("At Book:EndTime");
                                        }

                                    } else {
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText("Booked time:\n" + databaseManager.checkbookedRoomTime(bookingMap.get(message.getChatId()).getRoomID(), usersMap.get(message.getChatId()).getICNO(), bookID)
                                                + "Please choose a time that is not booked.");
                                    }
                                } else {

                                    SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    try {
                                        System.out.println("this date doesn't have time booked");
                                        userState.put(message.getChatId(), "Login:EditBook_Location_End");
                                        Date date = combine.parse(dateTemp + " " + message.getText());
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        //save starting time
                                        bookingMap.get(message.getChatId()).setStartDate(date);
                                        sendMessage.setText("How about your booking end time?\n\n" +
                                                "Example: 17:30");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        System.out.println("At Book:EndTime");
                                    }

                                }


                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }

                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
                                    "Example: 08:30");
                        }


                        break;

                    case "Login:EditBook_Location_End":
                        Date date = new Date();

                        if (inputFormatChecker.timeFormat(message.getText())) {
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            String dateTemp = databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);

                            //check whether the time is within 8AM to 8PM
                            if (inputFormatChecker.timeOpen(message.getText())) {
                                SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                try {
                                    date = combine.parse(dateTemp + " " + message.getText());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                //check whether the end time is before start time
                                if (date.after(bookingMap.get(message.getChatId()).getStartDate())) {

                                    //check whether the time contradicts with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, message.getText())
                                            && !databaseManager.checkTimeDatabase2(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, bookingMap.get(message.getChatId()).getStartDate(),message.getText())) {
                                        bookingMap.get(message.getChatId()).setEndDate(date);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String startDate = sdf.format(bookingMap.get(message.getChatId()).getStartDate());
                                        String endDate = sdf.format(bookingMap.get(message.getChatId()).getEndDate());
                                        databaseManager.editBookingDateTimeLoc(usersMap.get(message.getChatId()).getICNO(), bookingMap.get(message.getChatId()).getRoomID(), startDate, endDate, bookID);
                                        String list3 = databaseManager.getBookList(bookID);
                                        list3 += "Excellent! Your new Booking information has been updated";
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText(list3);

                                        InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                                        List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                                        InlineKeyboardButton inlineKeyboardButtonBack = new InlineKeyboardButton();
                                        inlineKeyboardButtonBack.setText("Back to main menu");
                                        inlineKeyboardButtonBack.setCallbackData("Login:Main");
                                        inlineKeyboardButtonList.add(inlineKeyboardButtonBack);
                                        inlineButtons2.add(inlineKeyboardButtonList);
                                        inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                                        sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                                    } else {
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)
                                                + "Please choose a time that is not booked.");
                                    }

                                } else {
                                    sendMessage = new SendMessage();
                                    sendMessage.setChatId(message.getChatId());
                                    sendMessage.setText("The booking end time cannot be before start time. Please enter your end time.\n\n" +
                                            "Example: 16:30");

                                }

                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }
                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
                                    "Example: 08:30");
                        }

                        break;

                    case "Login:EditBook_Date_Check":
                        String text = "";
                        if (inputFormatChecker.DateFormat(message.getText())) {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Date date1 = new Date();
                            SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            String dateTemp1 = databaseManager.getTimestamp(usersMap.get(message.getChatId()).getICNO(), bookID);
                            //System.out.println(dateTemp1);
                            try {
                                date1 = sdf.parse(dateTemp1);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            try {
                                Date bookDate = sdf.parse(message.getText());

                                if (inputFormatChecker.bookDate(bookDate, date1)) {
                                    if (inputFormatChecker.bookDateCurrent(bookDate)) {
                                        bookingMap.get(message.getChatId()).setTemp(message.getText());
                                        Date dateTemp = combine.parse(bookingMap.get(message.getChatId()).getTemp() + " " + databaseManager.getBookedRoomTime(usersMap.get(message.getChatId()).getICNO(), bookID));
                                        Date dateTemp2 = combine.parse(bookingMap.get(message.getChatId()).getTemp() + " " + databaseManager.getBookedRoomEndTime(usersMap.get(message.getChatId()).getICNO(), bookID));
                                        bookingMap.get(message.getChatId()).setStartDate(dateTemp);
                                        bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                        text += "Are you sure you want to change to this new date?";
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
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
                                        inlineKeyboardButton2.setText("No, I want to choose another date");
                                        inlineKeyboardButton1.setCallbackData("Login:EditBook_Date_Y");
                                        inlineKeyboardButton2.setCallbackData("Login:EditBook_Date");
                                        inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                        inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                        inlineButtons.add(inlineKeyboardButtonList1);
                                        inlineButtons.add(inlineKeyboardButtonList2);
                                        inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                                    } else {
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText("The booking date cannot before the current date");
                                    }


                                } else {
                                    sendMessage = new SendMessage();
                                    sendMessage.setChatId(message.getChatId());
                                    sendMessage.setText("The booking date needs to be at least 1 month prior and also can't be made if that day is over 1 year away.\n" +
                                            "As an example, to book a day in April 1st, booking needs to be made on March 1st or before.\n\n" +
                                            "Please enter the day you would like to book\n\nExample: 27/04/2023");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter the date in correct format.\n\n" +
                                    "Example: 27/04/2023");
                        }

                        break;

                    case "Login:EditBook_Time_End":
                        if (inputFormatChecker.timeFormat(message.getText())) {

                            if (inputFormatChecker.timeOpen(message.getText())) {
                                int bookID = bookingMap.get(message.getChatId()).getBookID();
                                String dateTemp = databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);
                                //System.out.println(dateTemp);

                                //if that day got booking
                                if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)) {
                                    System.out.println("this date got time booked in this room");

                                    //if the time chosen does not contradict with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, message.getText())) {

                                        SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                        try {
                                            userState.put(message.getChatId(), "Login:EditBook_Time_Update");
                                            Date date2 = combine.parse(dateTemp + " " + message.getText());
                                            //System.out.println(date2);
                                            sendMessage = new SendMessage();
                                            sendMessage.setChatId(message.getChatId());
                                            //save starting time
                                            bookingMap.get(message.getChatId()).setStartDate(date2);
                                            sendMessage.setText("How about your booking end time?\n\n" +
                                                    "Example: 17:30");

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            System.out.println("At Book:EndTime");
                                        }

                                    } else {
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText("Booked time:\n" + databaseManager.checkbookedRoomTime(bookingMap.get(message.getChatId()).getRoomID(), usersMap.get(message.getChatId()).getICNO(), bookID)
                                                + "Please choose a time that is not booked.");
                                    }
                                } else {

                                    SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    try {
                                        System.out.println("this date doesn't have time booked");
                                        userState.put(message.getChatId(), "Login:EditBook_Time_Update");
                                        Date date2 = combine.parse(dateTemp + " " + message.getText());
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        bookingMap.get(message.getChatId()).setStartDate(date2);
                                        sendMessage.setText("How about your booking end time?\n\n" +
                                                "Example: 17:30");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        System.out.println("At Book:EndTime");
                                    }

                                }


                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }

                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
                                    "Example: 08:30");
                        }
                        break;

                    case "Login:EditBook_Time_Update":
                        Date date2 = new Date();

                        if (inputFormatChecker.timeFormat(message.getText())) {
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            String dateTemp = databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);

                            //check whether the time is within 8AM to 8PM
                            if (inputFormatChecker.timeOpen(message.getText())) {
                                SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                try {
                                    date2 = combine.parse(dateTemp + " " + message.getText());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //check whether the end time is before start time
                                if (date2.after(bookingMap.get(message.getChatId()).getStartDate())) {

                                    //check whether the time contradicts with other booked time
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, message.getText())
                                            && !databaseManager.checkTimeDatabase2(bookingMap.get(message.getChatId()).getRoomID(),dateTemp,bookingMap.get(message.getChatId()).getStartDate(),message.getText())) {
                                        bookingMap.get(message.getChatId()).setEndDate(date2);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String startDate = sdf.format(bookingMap.get(message.getChatId()).getStartDate());
                                        String endDate = sdf.format(bookingMap.get(message.getChatId()).getEndDate());
                                        System.out.println(startDate);
                                        System.out.println(endDate);
                                        databaseManager.editBookingDateTime(usersMap.get(message.getChatId()).getICNO(), startDate, endDate, bookID);
                                        String list3 = databaseManager.getBookList(bookID);
                                        list3 += "Excellent! Your new Booking information has been updated";
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText(list3);

                                        InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                                        List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                                        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                                        InlineKeyboardButton inlineKeyboardButtonBack = new InlineKeyboardButton();
                                        inlineKeyboardButtonBack.setText("Back to main menu");
                                        inlineKeyboardButtonBack.setCallbackData("Login:Main");
                                        inlineKeyboardButtonList.add(inlineKeyboardButtonBack);
                                        inlineButtons2.add(inlineKeyboardButtonList);
                                        inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                                        sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                                    } else {
                                        sendMessage = new SendMessage();
                                        sendMessage.setChatId(message.getChatId());
                                        sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)
                                                + "Please choose a time that is not booked.");
                                    }

                                } else {
                                    sendMessage = new SendMessage();
                                    sendMessage.setChatId(message.getChatId());
                                    sendMessage.setText("The booking end time cannot be before start time. Please enter your end time.\n\n" +
                                            "Example: 16:30");

                                }

                            } else {
                                sendMessage = new SendMessage();
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }
                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
                                    "Example: 08:30");
                        }

                        break;

                }

                try {
                    sendMessage.setChatId(message.getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //Go to check state if no command found AND State have the first word as 'Register'
            else if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Register:")) {
                switch (userState.get(message.getChatId())) {

                    case "Register:IC":
                        if (inputFormatChecker.NameFormat(message.getText())) {
                            // save name
                            usersMap.get(message.getChatId()).setName(message.getText());

                            //set新的State
                            userState.put(message.getChatId(), "Register:Email");
                            sendMessage.setText("How about your IC number: \nExample: 001211080731");

                        } else {
                            sendMessage.setText("Please re-enter your name.");
                        }

                        break;

                    case "Register:Email":
                        if (inputFormatChecker.checkICFormat(message.getText())) {
                            usersMap.get(message.getChatId()).setICNO(message.getText());
                            //if database has user
                            if (databaseManager.checkUser(message.getText())) {
                                String text = databaseManager.displayUserInfo(usersMap.get(message.getChatId()).getICNO());

                                sendMessage = new SendMessage();
                                sendMessage.setText(text);
                                sendMessage.setParseMode(ParseMode.MARKDOWN);

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                inlineKeyboardButton1.setText("Yes");
                                inlineKeyboardButton2.setText("No");
                                inlineKeyboardButton1.setCallbackData("Register:Verify");
                                inlineKeyboardButton2.setCallbackData("Register:IC");
                                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                                inlineButtons.add(inlineKeyboardButtonList1);
                                inlineButtons.add(inlineKeyboardButtonList2);
                                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                            } else {

                                usersMap.get(message.getChatId()).setICNO(message.getText());
                                userState.put(message.getChatId(), "Register:StaffID");
                                sendMessage.setText("How about your Email?");
                            }
                        } else {
                            sendMessage.setText("Please enter your IC in correct format thank you :).");
                        }

                        break;

                    case "Register:StaffID":
                        if (inputFormatChecker.EmailFormat(message.getText())) {
                            usersMap.get(message.getChatId()).setEmail(message.getText());
                            userState.put(message.getChatId(), "Register:Mobile");
                            sendMessage.setText("Almost there! How about your Staff ID?\n\nExample: abc123");

                        } else {
                            sendMessage.setText("Please enter your email in correct format, thank you :)");
                        }

                        break;

                    case "Register:Mobile":
                        usersMap.get(message.getChatId()).setStaffID(message.getText());
                        userState.put(message.getChatId(), "Register:Check1");

                        sendMessage.setText("How about your Telephone Mobile Number? \n\n Example: 0124773579");

                        break;

                    case "Register:Check1":
                    case "Register:Chan_Name":
                    case "Register:Chan_IC":
                    case "Register:Chan_Email":
                    case "Register:Chan_StaffID":
                    case "Register:Chan_Mobile":

                        boolean msg1 = false;
                        if (userState.get(message.getChatId()).equals("Register:Check1") || userState.get(message.getChatId()).equals("Register:Chan_Mobile")) {
                            if (inputFormatChecker.TelNumFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setTelNo(message.getText());
                                msg1 = true;
                                System.out.println(msg1);
                            } else {
                                sendMessage.setText("Please enter your phone number in correct format thank you.\n\n" +
                                        "Example: 0124773579");
                            }
                        } else if (userState.get(message.getChatId()).equals("Register:Chan_Name")) {
                            if (inputFormatChecker.NameFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setName(message.getText());
                                msg1 = true;
                            } else {
                                sendMessage.setText("Please re-enter your name.\n\n" +
                                        "Example: Ang Toon Phng");
                            }
                        } else if (userState.get(message.getChatId()).equals("Register:Chan_IC")) {
                            if (inputFormatChecker.checkICFormat(message.getText())) {

                                //check if user exist in the database
                                if (!databaseManager.checkUser(message.getText())) {
                                    usersMap.get(message.getChatId()).setICNO(message.getText());
                                    msg1 = true;
                                } else {
                                    //if IC already used, then it is invalid
                                    sendMessage.setText("Sorry, this IC had been used by someone else, please enter another IC number\n\n" +
                                            "Example: 001211080731");
                                }
                            } else {
                                sendMessage.setText("Please re-enter your IC in correct format thank you.\n\n" +
                                        "Example: 001211080731");
                            }

                        } else if (userState.get(message.getChatId()).equals("Register:Chan_Email")) {
                            if (inputFormatChecker.EmailFormat(message.getText())) {
                                usersMap.get(message.getChatId()).setEmail(message.getText());
                                msg1 = true;
                            } else {
                                sendMessage.setText("Please re-enter the email in correct format thank you.\n\n" +
                                        "Example: MyEmail@hotmail.com");
                            }


                        } else if (userState.get(message.getChatId()).equals("Register:Chan_StaffID")) {
                            usersMap.get(message.getChatId()).setStaffID(message.getText());
                            msg1 = true;

                        }

                        if (msg1) {
                            String UserInfo = "\nName: " + usersMap.get(message.getChatId()).getName() +
                                    "\nIC Number: " + usersMap.get(message.getChatId()).getICNO() +
                                    "\nEmail: " + usersMap.get(message.getChatId()).getEmail() +
                                    "\nStaffID: " + usersMap.get(message.getChatId()).getStaffID() +
                                    "\nMobile Number: " + usersMap.get(message.getChatId()).getTelNo() +
                                    "\nAre these the correct information?\n";


                            sendMessage = new SendMessage();
                            sendMessage.setText(UserInfo);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList12 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList13 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton12 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton13 = new InlineKeyboardButton();
                            inlineKeyboardButton12.setText("Yes");
                            inlineKeyboardButton13.setText("No I would like to change something");
                            inlineKeyboardButton12.setCallbackData("Register:Success");
                            inlineKeyboardButton13.setCallbackData("Register:ChangeUserData");
                            inlineKeyboardButtonList12.add(inlineKeyboardButton12);
                            inlineKeyboardButtonList13.add(inlineKeyboardButton13);
                            inlineButtons.add(inlineKeyboardButtonList12);
                            inlineButtons.add(inlineKeyboardButtonList13);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        }

                        break;

                }
                try {
                    sendMessage.setChatId(message.getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            ////Go to check state if no command found AND State have the first word as 'RoomList'
            else if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("RoomList:")) {
                switch (userState.get(message.getChatId())){
                    case "RoomList:Date_School":
                        if(inputFormatChecker.DateFormat(message.getText())){
                            roomListMap.get(message.getChatId()).setDate(message.getText());
                            userState.put(message.getChatId(),"RoomList:Date_Room");

                            sendMessage.setText(databaseManager.schoolBookList() + "\nWhat about the school?\nExample Reply: 1");

                        }else{
                            sendMessage.setText("Please re-enter the date in correct format.");
                        }

                    break;

                    case "RoomList:Date_Room":
                        if(!inputFormatChecker.NameFormat(message.getText())){
                            if(databaseManager.SchoolHaveRoom(Integer.parseInt(message.getText()))){
                                userState.put(message.getChatId(),"RoomList:Date_AvailableTime");
                                roomListMap.get(message.getChatId()).setSchoolID(Integer.parseInt(message.getText()));
                                sendMessage.setText(databaseManager.RoomListwDate(Integer.parseInt(message.getText()),roomListMap.get(message.getChatId()).getDate()) +
                                        "You can enter room id if you want to learn more about specific room.\n" +
                                        "Example reply: 2");
                            }else{
                                sendMessage.setText(databaseManager.schoolBookList() + "\nThis school does not exist. Please re-enter the school\n Example reply: 1");
                            }
                        }else{
                            sendMessage.setText(databaseManager.schoolBookList() + "\nPlease enter a number.\nExample Reply: 1");
                        }

                    break;

                    case "RoomList:Date_AvailableTime":
                        if(!inputFormatChecker.NameFormat(message.getText())){
                            if(databaseManager.checkRoom(message.getText(),roomListMap.get(message.getChatId()).getSchoolID())){
                                String output ="";
                                if(databaseManager.checkBook(Integer.parseInt(message.getText()),roomListMap.get(message.getChatId()).getDate())){
                                    output += "Booked Time:\n";
                                    output += databaseManager.bookedTime(Integer.parseInt(message.getText()),roomListMap.get(message.getChatId()).getDate()) + "\n";
                                } else{
                                    output += "There are no booked time\n\n";
                                }

                                output += databaseManager.RoomInfo(Integer.parseInt(message.getText()));

                                output += "\n" + "If you want to check for more rooms, you can press the buttons below.";

                                sendMessage = new SendMessage();
                                sendMessage.setText(output);
                                sendMessage.setChatId(message.getChatId());
                                sendMessage.setParseMode(ParseMode.MARKDOWN);

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList5 = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList6 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                                inlineKeyboardButton5.setText("Re-enter date");
                                inlineKeyboardButton6.setText("Use the same date again");
                                inlineKeyboardButton5.setCallbackData("RoomList:Date");
                                inlineKeyboardButton6.setCallbackData("RoomList:Date_School");
                                inlineKeyboardButtonList5.add(inlineKeyboardButton5);
                                inlineKeyboardButtonList6.add(inlineKeyboardButton6);
                                inlineButtons2.add(inlineKeyboardButtonList5);
                                inlineButtons2.add(inlineKeyboardButtonList6);
                                inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                            }else{
                                sendMessage.setText("This room does not exist, please re-enter the room.\nExample reply: 2");
                            }
                        }else{
                            sendMessage.setText("Please enter a number.");
                        }

                    break;
                }

                try {
                    sendMessage.setChatId(message.getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }


            }

            //Go to check state if State have the first word as 'System'
            else if (!String.valueOf(message.getText().charAt(0)).equals("/") && userState.get(message.getChatId()).contains("System:")){
                switch (userState.get(message.getChatId())){
                    case "System:Main":
                        if(message.getText().equals("A221 PNG BOT")){
                            sendMessage.setText("What do you want to do?");
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                            inlineKeyboardButton1.setText("View Booking List");
                            inlineKeyboardButton2.setText("View School Admins");
                            inlineKeyboardButton1.setCallbackData("System:BookSchool");
                            inlineKeyboardButton2.setCallbackData("System:Admins");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        }else{
                            sendMessage.setText("Invalid verification code, please re-enter the code again.");
                        }

                    break;

                    case "System:Resign_Confirm":
                        if(!inputFormatChecker.NameFormat(message.getText())){
                            if(databaseManager.checkSchoolAdInput(message.getText())){
                                usersMap.get(message.getChatId()).setICNO(databaseManager.schoolAdminIC(message.getText()));
                                usersMap.get(message.getChatId()).setName(databaseManager.schoolAdminName(message.getText()));

                                sendMessage.setText("Are you sure you want to resign " + databaseManager.schoolAdminName(message.getText()) + "?");

                                sendMessage.setParseMode(ParseMode.MARKDOWN);

                                //Inline Keyboard Button
                                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                                List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                                inlineKeyboardButton1.setText("Yes");
                                inlineKeyboardButton2.setText("No, go back");
                                inlineKeyboardButton1.setCallbackData("System:Resign_Success");
                                inlineKeyboardButton2.setCallbackData("System:MainMenu");
                                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                                inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                                inlineButtons.add(inlineKeyboardButtonList1);
                                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                            }else{
                                sendMessage.setText(databaseManager.schoolAdminList() + "Please enter a valid number thank you. \nExample reply: 1");
                            }
                        }else{
                            sendMessage.setText(databaseManager.schoolAdminList() + "Please enter a number thank you. \nExample reply: 1");
                        }

                    break;
                }

                sendMessage.setChatId(message.getChatId());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        } else if (update.hasCallbackQuery()) {
            //buttonData will be categorized such as Book:Conf_Y, same reason as state
            String[] buttonData = update.getCallbackQuery().getData().split(":", 2);

            Message message = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            sendMessage = new SendMessage();
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setChatId(message.getChatId());

            if (buttonData[0].equals("Book")) {
                if (data.equals("Book:Book_Y")) {
                    //记得在save东西之后换state,可以看Book:IC做example
                    userState.put(message.getChatId(), "Book:IC");
                    sendMessage.setText("May I have the your NAME (as per NRIC/PASSPORT) please?" +
                            "\n\n P.S.:Don't worry, you can edit your information after the information are entered ;)");

                } else if (data.equals("Book:Book_N")) {
                    userState.put(message.getChatId(), "Start");
                    sendMessage.setText("I'll be here whenever you need me :)");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Verification")) {
                    userState.put(message.getChatId(), "Book:School");
                    sendMessage.setText("Please enter your IC and Email for verification purpose.\n\n" +
                            "Example: 990724070661@MyEmail@hotmail.com");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:FillIC")) {
                    userState.put(message.getChatId(), "Book:Email");
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

                } else if (data.equals("Book:Chan_Name") || data.equals("Book_Chan_IC") || data.equals("Book:Chan_Email") || data.equals("Book:Chan_StaffID") || data.equals("Book:Chan_TelNo")) {
                    if (data.equals("Book:Chan_Name")) {
                        userState.put(message.getChatId(), "Book:Chan_Name");
                        sendMessage.setText("What do you want to change your name to?\n\n" +
                                "Example: Ang Toon Phng");
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
                } else if (data.equals("Book:Conf_Y")) {
                    databaseManager.insertUser(usersMap.get(message.getChatId()).getName(), usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getEmail(), usersMap.get(message.getChatId()).getStaffID(), usersMap.get(message.getChatId()).getTelNo());

                    Date date = new Date();
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, usersMap.get(message.getChatId()).getICNO()));

                    userState.put(message.getChatId(), "Book:Room");

                    String list = databaseManager.schoolBookList();
                    list += "Excellent! Which school do you wish to book in?\nExample reply: 1";

                    sendMessage.setText(list);
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Room_Conf_N")) {

                    Date date = new Date();
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, usersMap.get(message.getChatId()).getICNO()));
                    userState.put(message.getChatId(), "Book:Room");

                    String list = databaseManager.schoolBookList();
                    list += "Which school do you wish to book in?\nExample reply: 1";

                    sendMessage.setText(list);
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Room_Conf_Y")) {

                    userState.put(message.getChatId(), "Book:StartTime");
                    sendMessage.setText("Please enter the date that you want to book this room.\n\n" +
                            "Example: 27/04/2023");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Booking_Conf_Y")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date timeStamp = new Date();
                    String startDate = sdf.format(bookingMap.get(message.getChatId()).getStartDate());
                    String endDate = sdf.format(bookingMap.get(message.getChatId()).getEndDate());
                    String bookTime = sdf.format(timeStamp);
                    bookingMap.get(message.getChatId()).setUserIC(usersMap.get(message.getChatId()).getICNO());
                    databaseManager.insertBook(bookingMap.get(message.getChatId()).getBookPurpose(), startDate, endDate, bookingMap.get(message.getChatId()).getRoomID(),bookingMap.get(message.getChatId()).getUserIC(), bookTime);

                    sendMessage.setText("You have successfully booked the room!" +
                            "\n\nYou can review the booked room(s) by using /room");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Booking_Conf_N")) {
                    //Display information to double-check
                    String info = "Which information do you want to change";
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
                    InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("Date");
                    inlineKeyboardButton2.setText("Start Time");
                    inlineKeyboardButton3.setText("End Time");
                    inlineKeyboardButton4.setText("Purpose");
                    inlineKeyboardButton1.setCallbackData("Book:Chan_Date");
                    inlineKeyboardButton2.setCallbackData("Book:Chan_StartTime");
                    inlineKeyboardButton3.setCallbackData("Book:Chan_EndTime");
                    inlineKeyboardButton4.setCallbackData("Book:Chan_Purpose");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton4);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineButtons.add(inlineKeyboardButtonList2);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                } else if (data.equals("Book:Chan_Date") || data.equals("Book:Chan_StartTime") || data.equals("Book:Chan_EndTime") || data.equals("Book:Chan_Purpose")) {
                    if (data.equals("Book:Chan_Date")) {
                        userState.put(message.getChatId(), "Book:Chan_Date");
                        sendMessage.setText("When do you want to change your date to?\n\n" +
                                "Example: 27/04/2023");
                    } else if (data.equals("Book:Chan_StartTime")) {
                        userState.put(message.getChatId(), "Book:Chan_StartTime");
                        sendMessage.setText("When do you want to change your start time to?\n\n" +
                                "Example: 08:30");
                    } else if (data.equals("Book:Chan_EndTime")) {
                        userState.put(message.getChatId(), "Book:Chan_EndTime");
                        sendMessage.setText("When do you want to change your end time to?\n\n" +
                                "Example: 18:30");
                    } else if (data.equals("Book:Chan_Purpose")) {
                        userState.put(message.getChatId(), "Book:Chan_Purpose");
                        sendMessage.setText("What do you want to change your booking purpose to?\n\n" +
                                "Example: Club meeting.");
                    }


                    sendMessage.setChatId(message.getChatId());
                }


            }

            else if(buttonData[0].equals("Login")){
                if(data.equals("Login:ViewBook")){
                    if (databaseManager.checkUserIC(usersMap.get(message.getChatId()).getICNO())) {

                        String bookDetails = databaseManager.viewBooked(usersMap.get(message.getChatId()).getICNO(), "viewDetails");
                        sendMessage.setText(bookDetails);
                        sendMessage.setParseMode(ParseMode.MARKDOWN);
                        sendMessage.setChatId(message.getChatId());

                        //Inline Keyboard Button
                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                        inlineKeyboardButton1.setText("Go back");
                        inlineKeyboardButton1.setCallbackData("Login:Main");
                        inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                        inlineButtons.add(inlineKeyboardButtonList1);
                        inlineKeyboardMarkup.setKeyboard(inlineButtons);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    }else {
                        String list = databaseManager.viewBooked(usersMap.get(message.getChatId()).getICNO(), "");
                        sendMessage = new SendMessage();
                        sendMessage.setChatId(message.getChatId());
                        sendMessage.setText(list);
                    }

                } else if (data.equals("Login:Main")){
                    String bookedRooms = databaseManager.viewBooked(usersMap.get(message.getChatId()).getICNO(), "view");

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
                    if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                        //if user have booking{
                        inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                        inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                    }
                    inlineKeyboardButtonList3.add(inlineKeyboardButton4);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    if (databaseManager.checkBook(usersMap.get(message.getChatId()).getICNO())) {
                        inlineButtons.add(inlineKeyboardButtonList2);
                    }
                    inlineButtons.add(inlineKeyboardButtonList3);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                } else if(data.equals("Login:EditProfile")){
                    String edit = databaseManager.userProfile(usersMap.get(message.getChatId()).getICNO(), "view");
                    sendMessage.setText(edit);
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList3 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList4 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("Name");
                    inlineKeyboardButton2.setText("Email");
                    inlineKeyboardButton3.setText("Staff ID");
                    inlineKeyboardButton4.setText("Mobile Number");
                    inlineKeyboardButton5.setText("No, I had changed my mine");
                    inlineKeyboardButton1.setCallbackData("Login:EditProfile_Name");
                    inlineKeyboardButton2.setCallbackData("Login:EditProfile_Email");
                    inlineKeyboardButton3.setCallbackData("Login:EditProfile_StaffID");
                    inlineKeyboardButton4.setCallbackData("Login:EditProfile_Mobile");
                    inlineKeyboardButton5.setCallbackData("Login:Main");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton4);
                    inlineKeyboardButtonList3.add(inlineKeyboardButton5);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineButtons.add(inlineKeyboardButtonList2);
                    inlineButtons.add(inlineKeyboardButtonList3);
                    inlineButtons.add(inlineKeyboardButtonList4);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                } else if(data.equals("Login:EditProfile_Name") ||data.equals("Login:EditProfile_Email") ||data.equals("Login:EditProfile_StaffID") ||data.equals("Login:EditProfile_Mobile")) {
                    if (data.equals("Login:EditProfile_Name")) {
                        userState.put(message.getChatId(), "Login:EditProfile_Name");
                        sendMessage.setText("What do you want to change your Name to?");
                    } else if (data.equals("Login:EditProfile_Email")) {
                        userState.put(message.getChatId(), "Login:EditProfile_Email");
                        sendMessage.setText("What do you want to change your Email to?");
                    } else if (data.equals("Login:EditProfile_StaffID")) {
                        userState.put(message.getChatId(), "Login:EditProfile_StaffID");
                        sendMessage.setText("What do you want to change your StaffID to?");
                    } else if (data.equals("Login:EditProfile_Mobile")) {
                        userState.put(message.getChatId(), "Login:EditProfile_Mobile");
                        sendMessage.setText("What do you want to change your Mobile Number to?");
                    }


                    sendMessage.setChatId(message.getChatId());
                } else if (data.equals("Login:CancelBook")){
                    String bookDetails = databaseManager.viewBookedList(usersMap.get(message.getChatId()).getICNO(), "delete");
                    sendMessage.setText(bookDetails);
                    userState.put(message.getChatId(), "Login:CancelBook");
                } else if (data.equals("Login:CancelBook_confirm")){
                    String delDetails = databaseManager.deleteBook(usersMap.get(message.getChatId()).getICNO(), bookingMap.get(message.getChatId()).getBookID());
                    sendMessage = new SendMessage();
                    sendMessage.setText(delDetails);
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
                    inlineKeyboardButton2.setText("No, return to Main Menu");
                    inlineKeyboardButton1.setCallbackData("Login:CancelBook");
                    inlineKeyboardButton2.setCallbackData("Login:Main");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineButtons.add(inlineKeyboardButtonList2);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                }else if (data.equals("Login:EditBook")) {
                    Date date = new Date();
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, usersMap.get(message.getChatId()).getICNO()));
                    userState.put(message.getChatId(), "Login:EditBook_Menu");
                    String list = databaseManager.viewBookedList(usersMap.get(message.getChatId()).getICNO(), "Start");
                    list +="Which booking id do you wish to edit?\n\n" +
                            "Example reply: 1";
                    sendMessage.setText(list);

                } else if (data.equals("Login:EditBook_Location")) {
                    userState.put(message.getChatId(), "Login:EditBook_Location_Room");
                    String list = databaseManager.schoolList();
                    list += "\n\n" + "Which School do you want to book from?";
                    sendMessage.setText(list);
                } else if (data.equals("Login:EditBook_Location_Update3")) {
                    //System.out.println(bookingMap.get(message.getChatId()).getRoomID());
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    databaseManager.editBookingLocation(usersMap.get(message.getChatId()).getICNO(), bookingMap.get(message.getChatId()).getRoomID(), bookID);
                    String list = databaseManager.getBookList(bookID);
                    list += "Excellent! Your new Booking information has been updated";
                    sendMessage.setText(list);

                    InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButtonBack = new InlineKeyboardButton();
                    inlineKeyboardButtonBack.setText("Back to main menu");
                    inlineKeyboardButtonBack.setCallbackData("Login:Main");
                    inlineKeyboardButtonList.add(inlineKeyboardButtonBack);
                    inlineButtons2.add(inlineKeyboardButtonList);
                    inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                } else if (data.equals("Login:EditBook_Location_Time")) {
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    userState.put(message.getChatId(), "Login:EditBook_Location_Start");
                    String list = "When do you want to change your booking time to?\n\n";
                    list += "Booked time:\n";
                    list += databaseManager.checkbookedRoomTime(bookingMap.get(message.getChatId()).getRoomID(), usersMap.get(message.getChatId()).getICNO(), bookID);
                    list += "\nPlease choose your new booking start time that is not booked.\nExample: 15:30";
                    sendMessage.setText(list);
                } else if (data.equals("Login:EditBook_Date")) {
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    userState.put(message.getChatId(), "Login:EditBook_Date_Check");
                    String list = "Current room: ";
                    list += databaseManager.checkBookedRoom(usersMap.get(message.getChatId()).getICNO(), bookID);
                    list += "\nCurrent time: ";
                    list += databaseManager.viewBookedRoomTime(usersMap.get(message.getChatId()).getICNO(), bookID);
                    list += "\n\nPlease input the new date that you want to change it to.";
                    list += "\nExample: 27/04/2023";
                    sendMessage.setText(list);
                } else if (data.equals("Login:EditBook_Date_Y")) {
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String newDate = sdf.format(bookingMap.get(message.getChatId()).getStartDate());
                    String newDate2 = sdf.format(bookingMap.get(message.getChatId()).getEndDate());

                    databaseManager.editBookingDate(newDate, newDate2, usersMap.get(message.getChatId()).getICNO(), bookID);
                    String list = databaseManager.getBookList(bookID);
                    list += "Excellent! Your new Booking information has been updated";
                    sendMessage.setText(list);

                    InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButtonBack = new InlineKeyboardButton();
                    inlineKeyboardButtonBack.setText("Back to main menu");
                    inlineKeyboardButtonBack.setCallbackData("Login:Main");
                    inlineKeyboardButtonList.add(inlineKeyboardButtonBack);
                    inlineButtons2.add(inlineKeyboardButtonList);
                    inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                } else if (data.equals("Login:EditBook_Time")) {
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    userState.put(message.getChatId(), "Login:EditBook_Time_End");
                    String list = "Booked room: ";
                    list += databaseManager.checkBookedRoom(usersMap.get(message.getChatId()).getICNO(), bookID);
                    list += "\nBooked date: ";
                    list += databaseManager.getBookedRoomDate(usersMap.get(message.getChatId()).getICNO(), bookID);
                    list += "\n\nPlease enter your new booking start time";
                    list += "\nExample: 14:45";
                    sendMessage.setText(list);
                }
            }

            else if (buttonData[0].equals("Register")) {
                if (data.equals("Register:Register_Y")) {

                    userState.put(message.getChatId(), "Register:IC");
                    sendMessage.setText("Please Enter your NAME as per NRIC number : \nExample: Ang Toon Phng");

                } else if (data.equals("Register:Register_N")) {
                    sendMessage.setText("I'll be here whenever you need me :)");

                } else if (data.equals("Register:Verify")) {
                    sendMessage.setText("Your information had already exist in the database. \nTo access more function, please use \n\n/login");

                } else if (data.equals("Register:IC")) {
                    userState.put(message.getChatId(), "Register:Email");
                    sendMessage.setText("This IC has been used by someone else, please re-enter your IC again");

                } else if (data.equals("Register:Success")) {
                    databaseManager.insertUser(usersMap.get(message.getChatId()).getName(), usersMap.get(message.getChatId()).getICNO(), usersMap.get(message.getChatId()).getEmail(), usersMap.get(message.getChatId()).getStaffID(), usersMap.get(message.getChatId()).getTelNo());
                    userState.put(message.getChatId(), "Register:SchoolName");
                    sendMessage.setText("Excellent!\nYou have successfully registered your information!\n\n" +
                            "To access more function please use \n\n/login");

                } else if (data.equals("Register:ChangeUserData")) {

                    String UserInfo = "\nName: " + usersMap.get(message.getChatId()).getName() +
                            "\nIC Number: " + usersMap.get(message.getChatId()).getICNO() +
                            "\nEmail: " + usersMap.get(message.getChatId()).getEmail() +
                            "\nStaffID: " + usersMap.get(message.getChatId()).getStaffID() +
                            "\nMobile Number: " + usersMap.get(message.getChatId()).getTelNo() +
                            "\nAre these the correct information?\n";


                    sendMessage = new SendMessage();
                    sendMessage.setText(UserInfo);
                    sendMessage.setParseMode(ParseMode.MARKDOWN);

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
                    inlineKeyboardButton2.setText("IC Number");
                    inlineKeyboardButton3.setText("Email");
                    inlineKeyboardButton4.setText("StaffID");
                    inlineKeyboardButton5.setText("Mobile");
                    inlineKeyboardButton1.setCallbackData("Register:Chan_Name");
                    inlineKeyboardButton2.setCallbackData("Register:Chan_IC");
                    inlineKeyboardButton3.setCallbackData("Register:Chan_Email");
                    inlineKeyboardButton4.setCallbackData("Register:Chan_StaffID");
                    inlineKeyboardButton5.setCallbackData("Register:Chan_Mobile");
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

                } else if (data.equals("Register:Chan_Name") || data.equals("Register:Chan_IC") ||
                        data.equals("Register:Chan_Email") || data.equals("Register:Chan_StaffID") ||
                        data.equals("Register:Chan_Mobile")) {
                    if (data.equals("Register:Chan_Name")) {
                        userState.put(message.getChatId(), "Register:Chan_Name");
                        sendMessage.setText("What do you want to change the name to?\n\n" +
                                "Example: Ang Toon Phng");
                    } else if (data.equals("Register:Chan_IC")) {
                        userState.put(message.getChatId(), "Register:Chan_IC");
                        sendMessage.setText("What do you want to change the IC number to?\n\n" +
                                "Example: 001211080731");
                    } else if (data.equals("Register:Chan_Email")) {
                        userState.put(message.getChatId(), "Register:Chan_Email");
                        sendMessage.setText("What do you want to change the email to?\n\n" +
                                "Example: MyEmail@hotmail.com");

                    } else if (data.equals("Register:Chan_StaffID")) {
                        userState.put(message.getChatId(), "Register:Chan_StaffID");
                        sendMessage.setText("What do you want to change the staff ID to?\n\n");

                    } else if (data.equals("Register:Chan_Mobile")) {
                        userState.put(message.getChatId(), "Register:Chan_Mobile");
                        sendMessage.setText("What do you want to change the mobile number to?\n\n" +
                                "Example: 0124773579");
                    }
                }
            }

            else if(buttonData[0].equals("RoomList")){
                if(data.equals("RoomList:Date")){
                    userState.put(message.getChatId(),"RoomList:Date_School");
                    sendMessage.setText("Please input the day that you want to view available rooms.\n\n" +
                            "Example: 27/04/2023");
                }

                else if(data.equals("RoomList:Date_School")){
                        userState.put(message.getChatId(),"RoomList:Date_Room");
                        sendMessage.setText(databaseManager.schoolBookList() + "\nWhich school do you want to know more about\nExample Reply: 1");
                }

            }

            else if(buttonData[0].equals("System")){
                if(data.equals("System:MainMenu")){
                    sendMessage.setText("Is there something that you would like to do?");

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("View Booking List");
                    inlineKeyboardButton2.setText("View School Admins");
                    inlineKeyboardButton1.setCallbackData("System:BookSchool");
                    inlineKeyboardButton2.setCallbackData("System:Admins");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineButtons.add(inlineKeyboardButtonList2);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                }
                else if(data.equals("System:Resign")){
                    String list=databaseManager.schoolAdminList();
                    if(!list.equals("")){
                        userState.put(message.getChatId(),"System:Resign_Confirm");
                        list+= "Who do you want to resign?\n" +
                                "Example  reply: 1";
                        sendMessage.setText(list);
                    }else{
                        list = "Sorry, there are no school admin.";
                        sendMessage.setText(list);

                        //Inline Keyboard Button
                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                        inlineKeyboardButton1.setText("Go back");
                        inlineKeyboardButton1.setCallbackData("System:MainMenu");
                        inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                        inlineButtons.add(inlineKeyboardButtonList1);
                        inlineKeyboardMarkup.setKeyboard(inlineButtons);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    }
                }else if(data.equals("System:Resign_Success")){
                    databaseManager.resignSchoolAd(usersMap.get(message.getChatId()).getICNO());
                    databaseManager.updateUserRole("User", usersMap.get(message.getChatId()).getICNO());

                    sendMessage.setText("The school admin has been resigned");

                    sendMessage.setParseMode(ParseMode.MARKDOWN);

                    //Inline Keyboard Button
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList1 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList2 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                    inlineKeyboardButton1.setText("Resign another school admin");
                    inlineKeyboardButton2.setText("Go back");
                    inlineKeyboardButton1.setCallbackData("System:Resign");
                    inlineKeyboardButton2.setCallbackData("System:MainMenu");
                    inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                    inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                    inlineButtons.add(inlineKeyboardButtonList1);
                    inlineKeyboardMarkup.setKeyboard(inlineButtons);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                }
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }


}
