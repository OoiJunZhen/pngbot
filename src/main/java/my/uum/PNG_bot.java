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

public class PNG_Bot extends TelegramLongPollingBot {

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
    Map<Long, SchoolAdmin> schoolAdminMap = new HashMap<Long, SchoolAdmin>();

    Map<Long, Room> RegisterRoomMap = new HashMap<Long, Room>();

    @Override
    public String getBotUsername() {
        return "PNG_bot";
    }

    @Override
    public String getBotToken() {
        return "5813032321:AAFWCPiKtpUVrPa5mTzu6ZhZhXGVP7Va_vc";
    }

    /**
     * Hashmap for adding booking information
     */
    //Map<Long, Booking> bookingMap = new HashMap<Long, Booking>();
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        DatabaseManager databaseManager = new DatabaseManager();
        InputFormatChecker inputFormatChecker = new InputFormatChecker();

        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();

            System.out.println(message.getChatId().toString() + " " + message.getText());

            if (!userState.containsKey(message.getChatId())) {
                userState.put(message.getChatId(), "Start");
            }

            //seperate command/input from user to recognize the command better and go to better switch case
            String[] command = message.getText().split(" ");

            //state will be categorized such as Book:Book1, the word at the front will be recognized and labeled based on the command
            //String[] state = userState.get(message.getChatId()).split(":",2);


            /**
             * Check the command inputted by the user
             */
            switch (command[0]) {
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
                case "/registerschoolad":
                    schoolAdminMap.put(message.getChatId(), new SchoolAdmin("", "", "", "", "", "", "", ""));// String name, String ICNO, String email, String staffID, String telNo, String schoolName
                    RegisterRoomMap.put(message.getChatId(), new Room("", "", "", ""));
                    userState.put(message.getChatId(), "Register");
                    String msg10 = "Do you want register to become the school admin";
                    sendMessage = new SendMessage();
                    sendMessage.setText(msg10);
                    sendMessage.setChatId(message.getChatId().toString());
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
            }

            //Go to check state if no command found AND State have the first word as 'Book'
            if (!String.valueOf(command[0].charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Book:")) {
                switch (userState.get(message.getChatId())) {
                    case "Book:IC":
                        //save user 在 Book:Book_Y 之后input的内容起来，进object
                        usersMap.get(message.getChatId()).setName(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Book:Email");
                        sendMessage.setText("How about your NRIC number?\nExample: 001211080731");
                        sendMessage.setChatId(message.getChatId());

                        break;
                }

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (!String.valueOf(command[0].charAt(0)).equals("/") && userState.get(message.getChatId()).contains("Register:")) {
                switch (userState.get(message.getChatId())) {
                    /*case "Register:Name":

                        //set新的State
                        userState.put(message.getChatId(), "Register:IC");
                        sendMessage.setText("Please Enter your NAME as per NRIC number : \nExample: Ang Toon Phng");
                        sendMessage.setChatId(message.getChatId());

                        break;*/

                    case "Register:IC":
                        if (inputFormatChecker.NameFormat(message.getText())) {
                            // save name
                            schoolAdminMap.get(message.getChatId()).setName(message.getText());

                            //set新的State
                            userState.put(message.getChatId(), "Register:Email");
                            sendMessage.setText("How about your IC number: \nExample: 001211080731");
                            sendMessage.setChatId(message.getChatId());
                        } else {
                            sendMessage.setText("Please re-enter your name.");
                        }
                        break;

                    case "Register:Email":
                        if (inputFormatChecker.checkICFormat(message.getText())) {
                            schoolAdminMap.get(message.getChatId()).setICNO(message.getText());
                            //if database has user
                            if (databaseManager.checkUser(message.getText())) {
                                String text = databaseManager.displayUserInfo(schoolAdminMap.get(message.getChatId()).getICNO());

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
                                //save user 在 Book:Book_Y 之后input的内容起来，进object
                                schoolAdminMap.get(message.getChatId()).setICNO(message.getText());
                                userState.put(message.getChatId(), "Register:StaffID");
                                sendMessage.setText("How about your Email?");
                            }
                        } else {
                            sendMessage.setText("Please enter your IC in correct format thank you :).");
                        }

                        break;


                    case "Register:Register:OfficeNum1":
                        if (inputFormatChecker.EmailFormat(message.getText())) {
                            schoolAdminMap.get(message.getChatId()).setEmail(message.getText());
                            userState.put(message.getChatId(), "Register:StaffID");
                            sendMessage.setText("Hi " + schoolAdminMap.get(message.getChatId()).getName() + "! What is the best Office contact number to reach you?");
                            sendMessage.setChatId(message.getChatId());
                        } else {
                            sendMessage.setText("Please enter your email in correct format, thank you :)");
                        }

                        break;

                    case "Register:StaffID":
                        if (inputFormatChecker.EmailFormat(message.getText())) {
                            schoolAdminMap.get(message.getChatId()).setEmail(message.getText());
                            userState.put(message.getChatId(), "Register:Mobile");
                            sendMessage.setText("Almost there! How about your Staff ID?\n\nExample: abc123");
                            sendMessage.setChatId(message.getChatId());
                        } else {
                            sendMessage.setText("Please enter your email in correct format, thank you :)");
                        }

                        break;

                    case "Register:Mobile":
                        schoolAdminMap.get(message.getChatId()).setStaffID(message.getText());
                        userState.put(message.getChatId(), ": Register:Check1");

                        sendMessage.setText("How about your Telephone Mobile Number? \n\n Example: 0124773579");

                        break;

                    case "Register:Check1":
                    case "Register:Chan_Name":
                    case "Register:Chan_IC":
                    case "Register:Chan_Email":
                    case "Register:Chan_StaffID":
                    case "Register:Chan_Mobile":

                        boolean msg1 = false;
                        if (userState.get(message.getChatId()).equals("Register:Check1")) {
                            if (userState.get(message.getChatId()).equals("Register:Chan_Name")) {
                                //if (userState.get(message.getChatId()).equals("Register:Chan_Name")) {
                                schoolAdminMap.get(message.getChatId()).setName(message.getText());
                                msg1 = true;
                            } else {
                                sendMessage.setText("Please re-enter your name.\n\n" +
                                        "Example: Ang Toon Phng");
                            }
                        } else if (userState.get(message.getChatId()).equals("Register:Chan_IC")) {
                            if (inputFormatChecker.checkICFormat(message.getText())) {

                                //check if user exist in the database
                                if (!databaseManager.checkUser(message.getText())) {
                                    schoolAdminMap.get(message.getChatId()).setICNO(message.getText());
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
                                schoolAdminMap.get(message.getChatId()).setEmail(message.getText());
                                msg1 = true;
                            } else {
                                sendMessage.setText("Please re-enter the email in correct format thank you.\n\n" +
                                        "Example: MyEmail@hotmail.com");
                            }


                        } else if (userState.get(message.getChatId()).equals("Register:Chan_StaffID")) {
                            schoolAdminMap.get(message.getChatId()).setStaffID(message.getText());
                            msg1 = true;

                        } else if (userState.get(message.getChatId()).equals("Register:Chan_Mobile")) {
                            schoolAdminMap.get(message.getChatId()).setOfficeTelNo(message.getText());
                            msg1 = true;
                        }


                        if (msg1) {
                            String UserInfo = "\nName: " + schoolAdminMap.get(message.getChatId()).getName() +
                                    "\nIC Number: " + schoolAdminMap.get(message.getChatId()).getICNO() +
                                    "\nEmail: " + schoolAdminMap.get(message.getChatId()).getEmail() +
                                    "\nStaffID: " + schoolAdminMap.get(message.getChatId()).getStaffID() +
                                    "\nMobile Number: " + schoolAdminMap.get(message.getChatId()).getOfficeTelNo() +
                                    "\nAre these the correct information?\n";


                            sendMessage = new SendMessage();
                            sendMessage.setText(UserInfo);
                            sendMessage.setParseMode(ParseMode.MARKDOWN);
                            sendMessage.setChatId(message.getChatId());

                            //Inline Keyboard Button
                            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                            List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList12 = new ArrayList<>();
                            List<InlineKeyboardButton> inlineKeyboardButtonList13 = new ArrayList<>();
                            InlineKeyboardButton inlineKeyboardButton12 = new InlineKeyboardButton();
                            InlineKeyboardButton inlineKeyboardButton13 = new InlineKeyboardButton();
                            inlineKeyboardButton12.setText("Yes");
                            inlineKeyboardButton13.setText("No, I would like to change something");
                            inlineKeyboardButton12.setCallbackData("Register:OfficeNum2");
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
            }


        } else if (update.hasCallbackQuery()) {
            //buttonData will be categorized such as Book:Conf_Y, same reason as state
            String[] buttonData = update.getCallbackQuery().getData().split(":", 2);

            message = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            sendMessage = new SendMessage();
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setChatId(message.getChatId());

            if (buttonData[0].equals("Register")) {
                System.out.println("I'm here");
                if (data.equals("Register:Register_Y")) {
                    userState.put(message.getChatId(), "Register:IC");
                    sendMessage.setText("Please Enter your NAME as per NRIC number : \nExample: Ang Toon Phng");
                    sendMessage.setChatId(message.getChatId());

                } else if (data.equals("Register:Register_N")) {
                    sendMessage.setText("I'll be here whenever you need me :)");
                    sendMessage.setChatId(message.getChatId());


                }

            }

            else if (data.equals("Register:OfficeNum2")) {

                userState.put(message.getChatId(), "Register:SchoolName");
                sendMessage.setText("Excellent! However, since you are going to apply for School Admin, there are additional \n" +
                        "information that you need to enter here. Thank you for your patience!\n +" +
                        "\nWhat is the best Office contact number to reach you?");
                sendMessage.setChatId(message.getChatId());
            }

            else if(data.equals("Register:ChangeUserData")){
                String UserInfo = "\nName: " + schoolAdminMap.get(message.getChatId()).getName() +
                        "\nIC Number: " + schoolAdminMap.get(message.getChatId()).getICNO() +
                        "\nEmail: " + schoolAdminMap.get(message.getChatId()).getEmail() +
                        "\nStaffID: " + schoolAdminMap.get(message.getChatId()).getStaffID() +
                        "\nMobile Number: " + schoolAdminMap.get(message.getChatId()).getOfficeTelNo() +
                        "\nAre these the correct information?\n";


                sendMessage = new SendMessage();
                sendMessage.setText(UserInfo);
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
                inlineButtons.add(inlineKeyboardButtonList4);
                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            }
            else if(data.equals("Register:Chan_Name") || data.equals("Register:Chan_IC") ||
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

                sendMessage.setChatId(message.getChatId());

            }

            try{
                execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }


        }
    }
}