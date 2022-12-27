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

/**
 * This class is the implementation of Telegram Long Polling Bot.
 * It handles messages and buttons that are inserted by the user.
 * Extends TelegramLongPollingBot provided by org.telegram.telegrambots
 *
 * @author Ang Toon Ph'ng
 */
public class DaBooking_bot extends TelegramLongPollingBot {


    /**
     * Get the DaBooking Bot's username
     */
    @Override
    public String getBotUsername() {return "s277364_A221_bot";}

    /**
     * Get the DaBooking Bot's token
     */
    @Override
    public String getBotToken() {
        return "5862794314:AAHPi_gS0w7rHRa4RmpEWJPv0MwaWsvACRw";
    }


     //Objects below are used to save user information based on their chatID


    /**
     * HashMap for saving a list of users Data temporarily
     */
    Map<String, ArrayList<String>> multiValueMap = new HashMap<String, ArrayList<String>>();

    /**
     * HashMap to save a list of users' Booking Date temporarily
     */
    Map<String, ArrayList<java.util.Date>> bookMapDate = new HashMap<String, ArrayList<Date>>();

    /**
     * HashMap to save a list of users' Booking Room ID temporarily
     */
    Map<String, Integer> bookMapRoomID = new HashMap<String, Integer>();

    /**
     * HashMap to save a list of users' Booking Information temporarily
     */
    Map<String, String> bookMapTemp = new HashMap<String, String>();

    /**
     * HashMap to save a list of users' Booking Purpose temporarily
     */
    Map<String, String> bookMapPurpose = new HashMap<String, String>();

    /**
     * HashMap to save a list of users' ID temporarily
     */
    Map<String, Integer> bookMapUserID = new HashMap<String, Integer>();

    /**
     * HashMap for login
     */
    Map<String, String> loginMapUserIC = new HashMap<String, String>();

    /**
     * ArrayList for updating on User State
     */
    ArrayList<ArrayList<String>> comTrace = new ArrayList<>();

    Integer userID;

    /**
     * This Telegram Bot will run whenever a new update is available for the bot to handle
     * @param update The update that was received from Telegram's API
     */
    @Override
    public void onUpdateReceived(Update update) {

        SendMessage sendMessage = new SendMessage();
        ArrayList<String> chatStateList = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();


        Message message;
        if(update.hasMessage()){
            message = update.getMessage();
            String[] command = message.getText().split(" ");
            String[] login = message.getText().split("@",2);

            //check whether the user input is correct (For Date format)
            boolean parseDateCheck=false;

            //Check whether the chatID is saved into the system or not
            boolean check = checkChatID(message.getChatId().toString());

            //if no, add chatID into the system and assign it's state as "Start"
            if(!check) {
                //Save ChatID and it's state to ArrayList as a set
                chatStateList.add(message.getChatId().toString());
                chatStateList.add("Start");
                comTrace.add(chatStateList);

                //Preparation to add User Data into multimap
                multiValueMap.put(message.getChatId().toString(),new ArrayList<String>());

                //Preparation to add Booking info into multimap
                bookMapDate.put(message.getChatId().toString(),new ArrayList<Date>());
            }

            /**
             * Check the command inputted by the user
             */
            switch (command[0]) {
                case "/start":
                    if (command.length == 1) {
                        String info = "Hi there! I'm Turtle, your School of Computing (SOC) Room Booking Assistant.\n\n" +
                                "Enter /book to book a room! The rooms are encouraged to be booked between 8am to 8pm";
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
                    setChatState(message.getChatId().toString(),"Book");
                    String info = "Do you intend to book a room in SOC?";
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
                case "/room":
                    String info2 = "Please enter your IC and Email to access the rooms booked by you\n\n" +
                            "Example: 990724070661@MyEmail@hotmail.com";
                    sendMessage = new SendMessage();
                    sendMessage.setText(info2);
                    sendMessage.setChatId(message.getChatId().toString());
                    setChatState(message.getChatId().toString(),"Login");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                /*case "/schoollogin":
                    sendMessage.setText(getChatState(message.getChatId().toString()) + " " + userID );
                    sendMessage.setChatId(message.getChatId());

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break; */

            }


            if(!String.valueOf(command[0].charAt(0)).equals("/")){
                switch (getChatState(message.getChatId().toString())) {
                    case "Book_IC":
                        //obtain and save user input into hashmap as name based on chatID
                        if(multiValueMap.get(message.getChatId().toString()).size() == 0)
                            multiValueMap.get(message.getChatId().toString()).add(message.getText());
                        else
                            multiValueMap.get(message.getChatId().toString()).set(0,message.getText());
                        //ask for IC from user
                        sendMessage.setText("How about your NRIC number?\nExample: 001211080731");
                        sendMessage.setChatId(message.getChatId());

                        //Update user state to Book_Email so that the user will receive input Email request from the system
                        setChatState(message.getChatId().toString(),"Book_Email");


                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Book_Email":
                        //obtain and save user input into hashmap as IC based on chatID
                        if(multiValueMap.get(message.getChatId().toString()).size() <= 1)
                            multiValueMap.get(message.getChatId().toString()).add(message.getText());
                        else
                            multiValueMap.get(message.getChatId().toString()).set(1,message.getText());

                        //Check whether the user IC exist in the database, if the user exist we'll ask them to confirm their identity
                        if(databaseManager.checkUser(multiValueMap.get(message.getChatId().toString()).get(1))){
                            String text = databaseManager.displayUserInfo(multiValueMap.get(message.getChatId().toString()).get(1));


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
                            inlineKeyboardButton1.setCallbackData("CheckConf_Y");
                            inlineKeyboardButton2.setCallbackData("CheckConf_N");
                            inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                            inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                            inlineButtons.add(inlineKeyboardButtonList1);
                            inlineButtons.add(inlineKeyboardButtonList2);
                            inlineKeyboardMarkup.setKeyboard(inlineButtons);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                        }
                        else{
                        //ask for Email from user
                        sendMessage.setText("How about your email?");
                        sendMessage.setChatId(message.getChatId());

                        //Update user state to Book_Email so that the user will receive input Staff ID request from the system
                        setChatState(message.getChatId().toString(), "Book_StaffID");
                    }
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Book_StaffID":
                        //obtain and save user input into hashmap as Email based on chatID
                        if(multiValueMap.get(message.getChatId().toString()).size() <= 2)
                            multiValueMap.get(message.getChatId().toString()).add(message.getText());
                        else
                            multiValueMap.get(message.getChatId().toString()).set(2,message.getText());

                        //ask for Staff ID from user
                        sendMessage.setText("Almost There! How about your Staff ID?\n\nExample: abc123");
                        sendMessage.setChatId(message.getChatId());

                        //Update user state to Book_Confirmation so that the user will receive input Staff ID request from the system
                        setChatState(message.getChatId().toString(),"Book_TellNo");

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Book_TellNo":
                        //obtain and save user input into hashmap as Email based on chatID
                        if(multiValueMap.get(message.getChatId().toString()).size() <= 3)
                            multiValueMap.get(message.getChatId().toString()).add(message.getText());
                        else
                            multiValueMap.get(message.getChatId().toString()).set(3,message.getText());
                        //ask for Telephone Number from user
                        sendMessage.setText("What is the best contact number to reach you? \n\n Example: 0124773579");
                        sendMessage.setChatId(message.getChatId());

                        //Update user state to Book_Confirmation so that the user will receive input Staff ID request from the system
                        setChatState(message.getChatId().toString(),"Book_Confirmation");

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Book_Confirmation":
                    case "Chan_Name":
                    case "Chan_IC":
                    case "Chan_Email":
                    case "Chan_StaffID":
                    case "Chan_TelNo":
                        String info="";
                        if(getChatState(message.getChatId().toString()).equals("Book_Confirmation")){
                            //save User Telephone Number into hashmap
                            if(multiValueMap.get(message.getChatId().toString()).size() <= 4)
                                multiValueMap.get(message.getChatId().toString()).add(message.getText());
                            else
                                multiValueMap.get(message.getChatId().toString()).set(4,message.getText());
                        }
                        else if(getChatState(message.getChatId().toString()).equals("Chan_Name"))
                            multiValueMap.get(message.getChatId().toString()).set(0, message.getText());

                        else if(getChatState(message.getChatId().toString()).equals("Chan_IC")){
                            if(!databaseManager.checkUser(message.getText()))
                                multiValueMap.get(message.getChatId().toString()).set(1, message.getText());
                            else
                                info+= "Sorry the IC you written is already used by someone else.\n\n";
                        }


                        else if(getChatState(message.getChatId().toString()).equals("Chan_Email"))
                            multiValueMap.get(message.getChatId().toString()).set(2, message.getText());

                        else if(getChatState(message.getChatId().toString()).equals("Chan_StaffID"))
                            multiValueMap.get(message.getChatId().toString()).set(3, message.getText());

                        else if(getChatState(message.getChatId().toString()).equals("Chan_TelNo"))
                            multiValueMap.get(message.getChatId().toString()).set(4, message.getText());

                        //Display information to double check
                        info += "Are these the correct information?\n" +
                                "\nName: " + multiValueMap.get(message.getChatId().toString()).get(0) +
                                "\nIC: " + multiValueMap.get(message.getChatId().toString()).get(1) +
                                "\nEmail: " + multiValueMap.get(message.getChatId().toString()).get(2) +
                                "\nStaff ID: " + multiValueMap.get(message.getChatId().toString()).get(3) +
                                "\nTel No.: " + multiValueMap.get(message.getChatId().toString()).get(4);


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
                        inlineKeyboardButton1.setCallbackData("Conf_Y");
                        inlineKeyboardButton2.setCallbackData("Conf_N");
                        inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                        inlineKeyboardButtonList2.add(inlineKeyboardButton2);
                        inlineButtons.add(inlineKeyboardButtonList1);
                        inlineButtons.add(inlineKeyboardButtonList2);
                        inlineKeyboardMarkup.setKeyboard(inlineButtons);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case("Book_Room1"):
                        String info1 = databaseManager.getRoomInfo(message.getText());
                        sendMessage = new SendMessage();
                        sendMessage.setText(info1);
                        sendMessage.setParseMode(ParseMode.MARKDOWN);
                        sendMessage.setChatId(message.getChatId());

                        if(!info1.equals("Sorry, this room does not exist. Please try to reply another number :)")) {
                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup1 = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons1 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                            inlineKeyboardButton3.setText("Yes");
                            inlineKeyboardButton4.setText("No");
                            inlineKeyboardButton3.setCallbackData("Room1_Y");
                            inlineKeyboardButton4.setCallbackData("Room1_N");
                            inlineKeyboardButtonList.add(inlineKeyboardButton3);
                            inlineKeyboardButtonList.add(inlineKeyboardButton4);
                            inlineButtons1.add(inlineKeyboardButtonList);
                            inlineKeyboardMarkup1.setKeyboard(inlineButtons1);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup1);

                            //Save room ID as a booking room
                            Integer roomID = Integer.parseInt(message.getText());
                            bookMapRoomID.put(message.getChatId().toString(), roomID);
                        }



                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case("Room_StartTime"):
                        String info2 = message.getText();

                        try{
                            Date startDate=new SimpleDateFormat("dd/MM/yyyy").parse(info2);
                            parseDateCheck = true;

                            //Save in bookMapTemp as temporary String so that will be added into bookMapDate later
                            bookMapTemp.put(message.getChatId().toString(),info2);

                        }catch (ParseException e) {
                            e.printStackTrace();
                            parseDateCheck = false;
                        }

                        if(!parseDateCheck)
                            info2 = "Please Re-enter the time thank you.";
                        else{
                            info2 = "How about the Time?\n\nExample: 14:30";
                            setChatState(message.getChatId().toString(),"Room_EndTime");
                        }

                        sendMessage = new SendMessage();
                        sendMessage.setText(info2);
                        sendMessage.setChatId(message.getChatId());

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case"Room_EndTime":
                        String info3 = bookMapTemp.get(message.getChatId().toString())+ " " + message.getText();

                        try{
                            Date startDate=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(info3);
                            parseDateCheck = true;

                            //Save the date and time from bookMapPurpose into bookMapDate
                            if(bookMapDate.get(message.getChatId().toString()).size() == 0)
                                bookMapDate.get(message.getChatId().toString()).add(startDate);
                            else
                                bookMapDate.get(message.getChatId().toString()).set(0,startDate);

                        }catch (ParseException e) {
                            e.printStackTrace();
                            parseDateCheck = false;
                        }

                        if(!parseDateCheck)
                            info3 = "Please Re-enter the time thank you.";
                        else{
                            info3 = "When will the booking end?\n\nExample: 17:45";
                            setChatState(message.getChatId().toString(),"Room_Purpose");
                        }

                        sendMessage = new SendMessage();
                        sendMessage.setText(info3);
                        sendMessage.setChatId(message.getChatId());

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case"Room_Purpose":
                        String info4 = bookMapTemp.get(message.getChatId().toString())+ " " + message.getText();
                        try{
                            Date endDate=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(info4);

                            //Check whether the end date inputted is after the start date
                            if(endDate.after(bookMapDate.get(message.getChatId().toString()).get(0))){
                                //Save the date and time from bookMapPurpose into bookMapDate
                                if(bookMapDate.get(message.getChatId().toString()).size() <= 1)
                                    bookMapDate.get(message.getChatId().toString()).add(endDate);
                                else
                                    bookMapDate.get(message.getChatId().toString()).set(1,endDate);
                                parseDateCheck = true;
                            }else{
                                info4 = "Your End Time is earlier than Start Time :(\n";
                            }

                        }catch (ParseException e) {
                            e.printStackTrace();
                            parseDateCheck = false;
                        }

                        if(!parseDateCheck)
                            info4 += "Please Re-enter the Time thank you.";
                        else{
                            info4 = "What is the purpose of this booking?";
                            setChatState(message.getChatId().toString(),"Room_Confirm");
                        }

                        sendMessage = new SendMessage();
                        sendMessage.setText(info4);
                        sendMessage.setChatId(message.getChatId());

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case"Room_Confirm":
                    case "Chan_Date":
                    case "Chan_StartTime":
                    case "Chan_EndTime":
                    case "Chan_Purpose":

                        String info5 = message.getText();

                        if(getChatState(message.getChatId().toString()).equals("Chan_Date"))
                        {
                            //Variables to backup the time
                            String startTime;
                            String endTime;

                            //Variables to assign the date
                            Date dateTemp = new Date();

                            try{
                                //This is for combining String input which is Date and String time which is backed up by Start Time and End Time
                                SimpleDateFormat startDate=new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                //This is for separating and assign the initial time of Date in hashmap Date object into startTime and endTime
                                DateFormat dateFormat = new SimpleDateFormat("HH:mm");


                                //Save the original Time as String into both variables
                                startTime = dateFormat.format(bookMapDate.get(message.getChatId().toString()).get(0));
                                endTime = dateFormat.format(bookMapDate.get(message.getChatId().toString()).get(1));

                                //Combining date from input and startTime into dateTemp
                                dateTemp = startDate.parse(info5 + " " + startTime);
                                //Save dateTemp into bookMapDate startDate to update the newest Date
                                bookMapDate.get(message.getChatId().toString()).set(0,dateTemp);

                                //Combining date from input and startTime into dateTemp
                                dateTemp = startDate.parse(info5 + " " + endTime);
                                //Save dateTemp into bookMapDate startDate to update the newest Date
                                bookMapDate.get(message.getChatId().toString()).set(1,dateTemp);

                                parseDateCheck = true;

                            }catch (ParseException e) {
                                e.printStackTrace();
                                parseDateCheck = false;
                            }


                        } else if(getChatState(message.getChatId().toString()).equals("Chan_StartTime")){
                            info5 = message.getText();
                            Date dateTemp = new Date();

                            //This is for separating and assign the initial date of Starting Date in hashmap Date object into dateTemp
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                            //This is for combining String input which is Date and String time which is backed up by Start Time and End Time
                            SimpleDateFormat startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                            try{
                                //Save dd/MM/yyyy into newDate
                                String newDate = dateFormat.format(bookMapDate.get(message.getChatId().toString()).get(0));

                                //combine newDate and info5
                                dateTemp = startDate.parse(newDate + " " + info5);


                                //Check whether the end date is after the new start date
                                if(bookMapDate.get(message.getChatId().toString()).get(1).after(dateTemp)) {
                                    //assign dateTemp as new Date
                                    bookMapDate.get(message.getChatId().toString()).set(0, dateTemp);
                                    parseDateCheck = true;
                                }else{
                                    info5 = "The new start time cannot be after the initial end time :(\n";
                                    info5 += "Please Re-enter the Time thank you." +
                                            "\n\nExample: 14:30";
                                }


                            }catch (ParseException e) {
                                e.printStackTrace();
                                parseDateCheck = false;
                            }
                        } else if (getChatState(message.getChatId().toString()).equals("Chan_EndTime")) {
                            info5 = message.getText();
                            Date dateTemp = new Date();

                            //This is for separating and assign the initial date of Ending Date in hashmap Date object into dateTemp
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                            //This is for combining String input which is Date and String time
                            SimpleDateFormat startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                            try{
                                //Save dd/MM/yyyy into newDate
                                String newDate = dateFormat.format(bookMapDate.get(message.getChatId().toString()).get(1));

                                //combine newDate and info5
                                dateTemp = startDate.parse(newDate + " " + info5);


                                //Check whether the end date is after the new start date
                                if(bookMapDate.get(message.getChatId().toString()).get(0).before(dateTemp)) {
                                    //assign dateTemp as new Date
                                    bookMapDate.get(message.getChatId().toString()).set(1, dateTemp);
                                    parseDateCheck = true;
                                }else{
                                    info5 = "The new end time cannot be before the initial start time :(\n";
                                    info5 += "Please Re-enter the Time thank you." +
                                            "\n\nExample: 14:30";
                                }


                            }catch (ParseException e) {
                                e.printStackTrace();
                                parseDateCheck = false;
                            }
                        } else if (getChatState(message.getChatId().toString()).equals("Chan_Purpose") || getChatState(message.getChatId().toString()).equals("Room_Confirm")) {
                            bookMapPurpose.put(message.getChatId().toString(), message.getText());
                            parseDateCheck = true;
                        }
                        if(!parseDateCheck){

                            sendMessage = new SendMessage();
                            sendMessage.setText(info5);
                            sendMessage.setChatId(message.getChatId());
                        }
                        else{
                            //Display Booking Information for confirmation
                            SimpleDateFormat ForDay = new SimpleDateFormat("EEEE");
                            SimpleDateFormat ForDate = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat ForStartTime = new SimpleDateFormat("hh:mm a");

                            //find booking duration
                            long difference_In_Time = bookMapDate.get(message.getChatId().toString()).get(1).getTime() - bookMapDate.get(message.getChatId().toString()).get(0).getTime();
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

                            info5 = "DotW: " + ForDay.format(bookMapDate.get(message.getChatId().toString()).get(0));
                            info5 += "\nRoom: " + databaseManager.getRoomName(bookMapRoomID.get(message.getChatId().toString()));
                            info5 += "\nDate: " + ForDate.format(bookMapDate.get(message.getChatId().toString()).get(0));
                            info5 += "\nStart Time: " + ForStartTime.format(bookMapDate.get(message.getChatId().toString()).get(0));
                            info5 += "\nEnd Time: " + ForStartTime.format(bookMapDate.get(message.getChatId().toString()).get(1)) + "\nDuration: ";
                            if(difference_In_Hours>=1){
                                info5 += difference_In_Hours + "hour(s) ";
                            }
                            if(difference_In_Minutes>=1){
                                info5 += difference_In_Minutes + "minute(s)";
                            }
                            info5 += "\nBooking Purpose: " + bookMapPurpose.get(message.getChatId().toString());
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
                            inlineKeyboardButton5.setCallbackData("BookConf_Y");
                            inlineKeyboardButton6.setCallbackData("BookConf_N");
                            inlineKeyboardButtonList5.add(inlineKeyboardButton5);
                            inlineKeyboardButtonList6.add(inlineKeyboardButton6);
                            inlineButtons2.add(inlineKeyboardButtonList5);
                            inlineButtons2.add(inlineKeyboardButtonList6);
                            inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup2);
                        }


                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Login":
                        if(databaseManager.loginCheck(login[0],login[1])){

                            sendMessage = new SendMessage();
                            sendMessage.setText(databaseManager.loginStart(login[0]));
                            sendMessage.setChatId(message.getChatId());
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

                            //Save User's IC into hashmap for later use
                            loginMapUserIC.put(message.getChatId().toString(),login[0]);

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList5 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList6 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                            inlineKeyboardButton5.setText("View Booked Rooms");
                            inlineKeyboardButton6.setText("Cancel Booking");
                            inlineKeyboardButton5.setCallbackData("View_Booked");
                            inlineKeyboardButton6.setCallbackData("Cancel_Booked");
                            inlineKeyboardButtonList5.add(inlineKeyboardButton5);
                            inlineKeyboardButtonList6.add(inlineKeyboardButton6);
                            inlineButtons2.add(inlineKeyboardButtonList5);
                            inlineButtons2.add(inlineKeyboardButtonList6);
                            inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup2);
                        } else {
                            sendMessage = new SendMessage();
                            sendMessage.setText("Sorry we cant find your information :(\n\nPlease enter again");
                            sendMessage.setChatId(message.getChatId());
                        }

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Booked_Details":
                        int bookID;
                        sendMessage.setChatId(message.getChatId());
                        sendMessage.setParseMode(ParseMode.MARKDOWN);

                        try{
                            bookID=Integer.parseInt(message.getText());

                            sendMessage.setText(databaseManager.viewBookedDetails(bookID));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }

                        //Inline Keyboard Button
                        InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
                        inlineKeyboardButton7.setText("Go back");
                        inlineKeyboardButton7.setCallbackData("Go_Back");
                        inlineKeyboardButtonList.add(inlineKeyboardButton7);
                        inlineButtons2.add(inlineKeyboardButtonList);
                        inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                        sendMessage.setReplyMarkup(inlineKeyboardMarkup2);


                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Booked_Delete":
                        //This checkDelete is to ensure the mis-input from user won't cause the system to delete others' booked room
                        boolean checkDelete = false;


                        int bookID2 = 0;
                        sendMessage.setChatId(message.getChatId());


                        try{
                            bookID2=Integer.parseInt(message.getText());

                            sendMessage.setText(databaseManager.deleteBooked(bookID2,databaseManager.getUserID(loginMapUserIC.get(message.getChatId().toString()))));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                            sendMessage.setParseMode(ParseMode.MARKDOWN);

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup3 = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons3 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList3 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton();
                            inlineKeyboardButton8.setText("Back");
                            inlineKeyboardButton8.setCallbackData("Go_Back");
                            inlineKeyboardButtonList3.add(inlineKeyboardButton8);
                            inlineButtons3.add(inlineKeyboardButtonList3);
                            inlineKeyboardMarkup3.setKeyboard(inlineButtons3);
                            sendMessage.setReplyMarkup(inlineKeyboardMarkup3);

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

        }
        /**
         * Check CallbackQuery's data when buttons are pressed
         */
        else if(update.hasCallbackQuery()){
            message = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            sendMessage = new SendMessage();
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setChatId(message.getChatId());

            if (data.equals("Book1_Y")){
                    setChatState(message.getChatId().toString(),"Book_IC");
                    sendMessage.setText("May I have the your NAME (as per NRIC/PASSPORT) please?" +
                            "\n\n P.S.:Don't worry, you can edit your information after the informations are entered ;)");
            }
            else if (data.equals("Book1_N")){
                sendMessage.setText("I'll be here whenever you need me :)");
                setChatState(message.getChatId().toString(),"Start");
                sendMessage.setChatId(message.getChatId());

            } else if (data.equals("Conf_Y") || data.equals("Room1_N") || data.equals("CheckConf_Y")) {
                //Insert user information into database when there are no same IC in the database
                if(data.equals("Conf_Y") || !databaseManager.checkUser(multiValueMap.get(message.getChatId().toString()).get(1)))
                    databaseManager.insertUser(multiValueMap.get(message.getChatId().toString()).get(0),multiValueMap.get(message.getChatId().toString()).get(1),multiValueMap.get(message.getChatId().toString()).get(2),multiValueMap.get(message.getChatId().toString()).get(3),multiValueMap.get(message.getChatId().toString()).get(4));

                //Get user id from the database just saved using IC
                bookMapUserID.put(message.getChatId().toString(), databaseManager.getUserID(multiValueMap.get(message.getChatId().toString()).get(1)));

                sendMessage=new SendMessage();
                sendMessage.setText("Excellent! Which room do you want to book?\n\n" + databaseManager.getRoomList());
                sendMessage.setChatId(message.getChatId());
                setChatState(message.getChatId().toString(),"Book_Room1");

            } else if(data.equals("Conf_N") ){
                //save User Telephone Number into hashmap
                multiValueMap.get(message.getChatId().toString()).add(message.getText());

                //Display information to double check
                String info = "Which information do you want to change\n" +
                        "\nName: " + multiValueMap.get(message.getChatId().toString()).get(0) +
                        "\nIC: " + multiValueMap.get(message.getChatId().toString()).get(1) +
                        "\nEmail: " + multiValueMap.get(message.getChatId().toString()).get(2) +
                        "\nStaff ID: " + multiValueMap.get(message.getChatId().toString()).get(3) +
                        "\nTel No.: " + multiValueMap.get(message.getChatId().toString()).get(4);


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
                inlineKeyboardButton1.setCallbackData("Chan_Name");
                inlineKeyboardButton2.setCallbackData("Chan_IC");
                inlineKeyboardButton3.setCallbackData("Chan_Email");
                inlineKeyboardButton4.setCallbackData("Chan_StaffID");
                inlineKeyboardButton5.setCallbackData("Chan_TelNo");
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

            }else if(data.equals("Chan_Name") || data.equals("Chan_IC") ||data.equals("Chan_Email") ||data.equals("Chan_StaffID") ||data.equals("Chan_TelNo")) {
                if (data.equals("Chan_Name")) {
                    setChatState(message.getChatId().toString(), "Chan_Name");
                } else if (data.equals("Chan_IC")) {
                    setChatState(message.getChatId().toString(), "Chan_IC");
                } else if (data.equals("Chan_Email")) {
                    setChatState(message.getChatId().toString(), "Chan_Email");
                } else if (data.equals("Chan_StaffID")) {
                    setChatState(message.getChatId().toString(), "Chan_StaffID");
                } else if (data.equals("Chan_TelNo")) {
                    setChatState(message.getChatId().toString(), "Chan_TelNo");
                }
                sendMessage.setText("What do you want to change it to?");
                sendMessage.setChatId(message.getChatId());


            } else if ((data.equals("CheckConf_N"))) {
                String info = "Sorry, the NRIC number you inserted might be wrong, please insert your IC again\n\nExample: 000317070761";
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText(info);

                setChatState(message.getChatId().toString(),"Book_Email");

            } else if (data.equals("Room1_Y")) {
                String info = "When do you want to book this room? \nDon't worry, you can edit your booking info once the details are completed :)\n\nExample: 14/08/2021";
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText(info);

                setChatState(message.getChatId().toString(), "Room_StartTime");
            } else if (data.equals("BookConf_N")){
                //Display information to double check
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
                inlineKeyboardButton1.setCallbackData("Chan_Date");
                inlineKeyboardButton2.setCallbackData("Chan_StartTime");
                inlineKeyboardButton3.setCallbackData("Chan_EndTime");
                inlineKeyboardButton4.setCallbackData("Chan_Purpose");
                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                inlineKeyboardButtonList2.add(inlineKeyboardButton4);
                inlineButtons.add(inlineKeyboardButtonList1);
                inlineButtons.add(inlineKeyboardButtonList2);
                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

            }else if(data.equals("Chan_Date") || data.equals("Chan_StartTime") ||data.equals("Chan_EndTime") ||data.equals("Chan_Purpose")) {
                if (data.equals("Chan_Date")) {
                    setChatState(message.getChatId().toString(), "Chan_Date");
                } else if (data.equals("Chan_StartTime")) {
                    setChatState(message.getChatId().toString(), "Chan_StartTime");
                } else if (data.equals("Chan_EndTime")) {
                    setChatState(message.getChatId().toString(), "Chan_EndTime");
                } else if (data.equals("Chan_Purpose")) {
                    setChatState(message.getChatId().toString(), "Chan_Purpose");
                }

                sendMessage.setText("What do you want to change it to?");
                sendMessage.setChatId(message.getChatId());
            }else if(data.equals("BookConf_Y")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String startDate = sdf.format(bookMapDate.get(message.getChatId().toString()).get(0));
                String endDate = sdf.format(bookMapDate.get(message.getChatId().toString()).get(1));
                databaseManager.insertBook(bookMapPurpose.get(message.getChatId().toString()), startDate, endDate, bookMapRoomID.get(message.getChatId().toString()),bookMapUserID.get(message.getChatId().toString()));

                sendMessage.setText("You have successfully booked the room!" +
                        "\n\nYou can review the booked room(s) by using /room");
                sendMessage.setChatId(message.getChatId());

            }else if(data.equals("View_Booked")){
                //Use user IC to find User ID AND Use it to find and Display Booked Rooms
                userID = databaseManager.getUserID(loginMapUserIC.get(message.getChatId().toString()));
                String bookedRooms = databaseManager.viewBookedList(userID, "view");

                setChatState(message.getChatId().toString(), "Booked_Details");

                sendMessage = new SendMessage();
                sendMessage.setText(bookedRooms);
                sendMessage.setChatId(message.getChatId());

            } else if (data.equals("Go_Back")) {
                sendMessage = new SendMessage();
                sendMessage.setText(databaseManager.loginStart(loginMapUserIC.get(message.getChatId().toString())));
                sendMessage.setChatId(message.getChatId());
                sendMessage.setParseMode(ParseMode.MARKDOWN);

                //Inline Keyboard Button
                InlineKeyboardMarkup inlineKeyboardMarkup2 = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> inlineButtons2 = new ArrayList<>();
                List<InlineKeyboardButton> inlineKeyboardButtonList5 = new ArrayList<>();
                List<InlineKeyboardButton> inlineKeyboardButtonList6 = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                inlineKeyboardButton5.setText("View Booked Rooms");
                inlineKeyboardButton6.setText("Cancel Booking");
                inlineKeyboardButton5.setCallbackData("View_Booked");
                inlineKeyboardButton6.setCallbackData("Cancel_Booked");
                inlineKeyboardButtonList5.add(inlineKeyboardButton5);
                inlineKeyboardButtonList6.add(inlineKeyboardButton6);
                inlineButtons2.add(inlineKeyboardButtonList5);
                inlineButtons2.add(inlineKeyboardButtonList6);
                inlineKeyboardMarkup2.setKeyboard(inlineButtons2);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup2);


            } else if (data.equals("Cancel_Booked")) {
                //Use user IC to find User ID AND Use it to find and Display Booked Rooms
                userID = databaseManager.getUserID(loginMapUserIC.get(message.getChatId().toString()));
                String bookedRooms = databaseManager.viewBookedList(userID, "delete");

                setChatState(message.getChatId().toString(), "Booked_Delete");

                sendMessage = new SendMessage();
                sendMessage.setText(bookedRooms);
                sendMessage.setChatId(message.getChatId());
            }

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     *
     * @param chatID This method takes user's chatID for searching purposes
     * @return This method will *return* user's state after the chat ID from a specific user is found
     */
    public String getChatState(String chatID){
        for (int counter = 0; counter < comTrace.size(); counter++) {

            if(comTrace.get(counter).get(0).equals(chatID)){
                return comTrace.get(counter).get(1);
            }
        }
        return null;
    }

    /**
     *
     * @param chatID This method takes user's chatID for searching purposes
     * @param state This method will *update* user's state after the chat ID from a specific user is found
     */
    public void setChatState(String chatID, String state){
        for (int counter = 0; counter < comTrace.size(); counter++) {

            if(comTrace.get(counter).get(0).equals(chatID)){
                comTrace.get(counter).set(1,state);
            }
        }
    }

    /**
     *
     * @param chatID This method takes user's chatID for searching purposes
     * @return This method will return true if chatID is found and vise versa
     */
    public boolean checkChatID(String chatID){
        for (int counter = 0; counter < comTrace.size(); counter++) {

            if(comTrace.get(counter).get(0).equals(chatID)){
                return true;
            }
        }
        return false;
    }

}