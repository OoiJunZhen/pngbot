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

public class PNG_Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "testpng_bot";
    }

    @Override
    public String getBotToken() {
        return "5640848888:AAHqaJT0x2bgoQwyj6ATPLFKBnI6pq95lVU";
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

                case "/test":
                    usersMap.put(message.getChatId(), new Users("", "", "", "", ""));
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

                                Integer userID = databaseManager.getUserID(password[0]);
                                Date date = new Date();
                                bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, userID));
                                userState.put(message.getChatId(), "Book:Room");

                                String list = databaseManager.schoolList();
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
                        if (databaseManager.checkSchool(message.getText())) {
                            userState.put(message.getChatId(), "Book:RoomDetails");
                            bookingMap.get(message.getChatId()).setRoomID(Integer.parseInt(message.getText()));
                            String roomList = databaseManager.getRoomList(bookingMap.get(message.getChatId()).getRoomID());

                            roomList += "Which room do you want to book?\nExample reply: 1";
                            sendMessage.setText(roomList);

                        } else {
                            String list2 = databaseManager.schoolList();
                            list2 += "This school does not exist. Please re-enter the school that you wish to book in.\nExample reply: 1";

                            sendMessage.setText(list2);
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
                            String list2 = databaseManager.getRoomList(bookingMap.get(message.getChatId()).getRoomID());
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
                                    if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())) {
                                        text += "Booked time:\n";
                                        //display booked Time
                                        text += databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), message.getText());
                                    }

                                    text += "When do you want to start using the room?\n\nExample: 08:30";
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
                                if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())) {
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
                                                + "Please choose a time that is not booked.");
                                    }
                                } else {

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
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }

                        } else {
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
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
                                    if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp(), message.getText())) {

                                        bookingMap.get(message.getChatId()).setEndDate(dateTemp);
                                        userState.put(message.getChatId(), "Book:Booking_Confirmation");
                                        sendMessage.setText("Excellent! Please state your booking purpose.\n\n" +
                                                "Example: Club meeting.");

                                    } else {
                                        sendMessage.setText("Booked time:\n" + databaseManager.bookedTime(bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getTemp())
                                                + "Please choose a time that is not booked.");
                                    }

                                } else {
                                    sendMessage.setText("The booking end time cannot be before start time. Please enter your end time.\n\n" +
                                            "Example: 14:30");

                                }

                            } else {
                                sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                        "Example: 08:30");
                            }
                        } else {
                            sendMessage.setText("Please enter the time in correct format.\n\n" +
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
                                                    + "Please choose a date that does not contradict the booking time.\n If you still wish to book this day, you can change the time first.");
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
                                sendMessage.setText("Please enter the date in correct format\n\n" +
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
                                                            + "Please choose a time that is not booked.");
                                                }
                                            } else {
                                                //assign dateTemp as new Date
                                                bookingMap.get(message.getChatId()).setStartDate(dateTemp2);
                                                success = true;
                                            }

                                        } else {
                                            sendMessage.setText("The new start time cannot be after the initial end time :(\n" +
                                                    "Please Re-enter the Time thank you." +
                                                    "\n\nExample: 08:30");
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    sendMessage.setText("The available booking time is between 8AM to 8PM. Please enter your booking start time.\n\n" +
                                            "Example: 08:30");
                                }

                            } else {
                                sendMessage.setText("Please enter the time in correct format.\n\n" +
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
                                                        + "Please choose a time that is not booked.");
                                            }
                                        } else {
                                            //assign dateTemp as new Date
                                            bookingMap.get(message.getChatId()).setEndDate(dateTemp2);
                                            success = true;
                                        }

                                    } else {
                                        sendMessage.setText("The new end time cannot be before the initial start time :(\n" +
                                                "Please Re-enter the Time thank you." +
                                                "\n\nExample: 08:30");
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    success = false;
                                }

                            } else {
                                sendMessage.setText("Please enter the time in correct format.\n\n" +
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
                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText("Please enter your IC and Email in correct format.\n\nExample: 990724070661@MyEmail@hotmail.com");
                        }

                        break;

                    case "Login:EditBook_Menu":
                        int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());

                        if (databaseManager.checkBookId(userID, message.getText())) {
                            bookingMap.get(message.getChatId()).setBookID(Integer.parseInt(message.getText()));
                            System.out.println("BOOK ID" + bookingMap.get(message.getChatId()).getBookID());
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

                            String list = databaseManager.viewBookedList(userID, "Start");
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
                            int user_ID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            String roomList = databaseManager.getBookedRoomList(bookingMap.get(message.getChatId()).getRoomID(), user_ID, bookID);
                            roomList += "Which room do you want to change to?\nExample reply: 1";
                            sendMessage = new SendMessage();
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setText(roomList);
                            System.out.println("BOOK ID" + bookingMap.get(message.getChatId()).getBookID());

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
                            int user_ID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());
                            int bookID = bookingMap.get(message.getChatId()).getBookID();
                            int roomID = bookingMap.get(message.getChatId()).getRoomID();
                            System.out.println(user_ID);
                            System.out.println(bookID);
                            String dateTemp = databaseManager.getBookedRoomDate(user_ID, bookID);
                            System.out.println(dateTemp);
                            System.out.println(databaseManager.checkBook(roomID, dateTemp));
                            if (databaseManager.checkBook(bookingMap.get(message.getChatId()).getRoomID(), dateTemp)) {
                                list += "\nDate: " + dateTemp + "\nBooked time:\n";
                                //display booked Time
                                list += databaseManager.checkbookedRoomTime(bookingMap.get(message.getChatId()).getRoomID(), user_ID, bookID);
                                // System.out.println(databaseManager.getBookedRoomDate(user_ID, bookID));

                                String timeStartTemp = databaseManager.getBookedRoomTime(user_ID, bookID);
                                System.out.println(timeStartTemp);
                                //if the time chosen contradict with other booked time
                                if (!databaseManager.checkTimeDatabase(bookingMap.get(message.getChatId()).getRoomID(), dateTemp, timeStartTemp)) {
                                    String list2 = "\nYour current booking time: \n" + databaseManager.viewBookedRoomTime(user_ID, bookID)
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
                                    sendMessage.setText("You cannot book this room with your initial booking time, if you want to book this room you can change your booking time");
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

                }

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

                } else if (data.equals("Book:Chan_Name") || data.equals("Book:Chan_IC") || data.equals("Book:Chan_Email") || data.equals("Book:Chan_StaffID") || data.equals("Book:Chan_TelNo")) {
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
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO())));

                    userState.put(message.getChatId(), "Book:Room");

                    String list = databaseManager.schoolList();
                    list += "Excellent! Which school do you wish to book in?\nExample reply: 1";

                    sendMessage.setText(list);
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Book:Room_Conf_N")) {

                    Date date = new Date();
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO())));
                    userState.put(message.getChatId(), "Book:Room");

                    String list = databaseManager.schoolList();
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
                    bookingMap.get(message.getChatId()).setUserID(databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO()));
                    databaseManager.insertBook(bookingMap.get(message.getChatId()).getBookPurpose(), startDate, endDate, bookingMap.get(message.getChatId()).getRoomID(), bookingMap.get(message.getChatId()).getUserID(), bookTime);

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


            } else if (buttonData[0].equals("Login")) {
                if (data.equals("Login:ViewBook")) {
                    int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());
                    String bookedRooms = databaseManager.viewBookedList(userID, "List");
                    //打招呼和问user要做什么
                    bookedRooms += "\n" + "These are all your booking details.";
                    sendMessage.setText(bookedRooms);

                    InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButtonBack = new InlineKeyboardButton();
                    inlineKeyboardButtonBack.setText("Go back");
                    inlineKeyboardButtonBack.setCallbackData("Login:Main");
                    inlineKeyboardButtonList.add(inlineKeyboardButtonBack);
                    inlineButtons2.add(inlineKeyboardButtonList);
                    inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup2);

                } else if (data.equals("Login:Main")) {
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

                } else if (data.equals("Login:EditBook")) {
                    Date date = new Date();
                    bookingMap.put(message.getChatId(), new Booking(date, date, date, 0, "", "", 0, databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO())));
                    userState.put(message.getChatId(), "Login:EditBook_Menu");
                    int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());
                    String list = databaseManager.viewBookedList(userID, "Start");
                    list += "\n\n" + "Which booking id do you wish to edit?\n\n" +
                            "Example reply: 1";
                    sendMessage.setText(list);

                } else if (data.equals("Login:EditBook_Location")) {
                    userState.put(message.getChatId(), "Login:EditBook_Location_Room");
                    String list = databaseManager.schoolList();
                    list += "\n\n" + "Which School do you want to book from?";
                    sendMessage.setText(list);
                    System.out.println("BOOK ID" + bookingMap.get(message.getChatId()).getBookID());
                } else if (data.equals("Login:EditBook_Location_Update3")) {
                    System.out.println(bookingMap.get(message.getChatId()).getRoomID());
                    int userID = databaseManager.getUserID(usersMap.get(message.getChatId()).getICNO());
                    int bookID = bookingMap.get(message.getChatId()).getBookID();
                    databaseManager.editBookingLocation(userID, bookingMap.get(message.getChatId()).getRoomID(), bookID);
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