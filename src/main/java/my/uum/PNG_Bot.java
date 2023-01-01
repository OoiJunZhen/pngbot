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
                    schoolAdminMap.put(message.getChatId(), new SchoolAdmin("", "", "", "", "", "", "",""));// String name, String ICNO, String email, String staffID, String telNo, String schoolName
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
                    case "Register:SchoolName":
                        //save telNo. into hashmap
                        schoolAdminMap.get(message.getChatId()).setOfficeTelNo(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Register:BuildingLocation");
                        sendMessage.setText("Which school do you want to apply as its admin" +
                                "\n\nReply 1: School Of Computing (SOC)" +
                                "\nReply 2: School Of Education (SOE)" +
                                "\nReply 3: School Of Multimedia Technology & Communication (SMMTC)" +
                                "\nReply 4: School Of Quantitative Sciences (SQS)" +
                                "\nReply 5: School Of Applied Psychology, Social Work and Policy (SAPSP)" +
                                "\nReply 6: School Of Business Management (SBM)" +
                                "\nReply 7: Islamic Business School (IBS)" +
                                "\nReply 8: Tunku Puteri Intan Safinaz School of Accountancy (TISSA)" +
                                "\nReply 9: School of Economics, Finance & Banking (SEFB)" +
                                "\nReply 10: School of Technology Management & Logistics (STML)" +
                                "\nReply 11: UUM National Golf Academy (UUMNGA)" +
                                "\nReply 12: School Of Government (SOG)" +
                                "\nReply 13: School Of Law (SOL)" +
                                "\nReply 14: School Of International Studies (SOIS)" +
                                "\nReply 15: School Of Tourism, Hospitality & Event Management (STHEM)" +
                                "\nReply 16: School Of Creative Industry Management & Performing Arts (SCIMPA)" );
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:BuildingLocation":
                        // save school name
                        if(message.getText().equals("1")){
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Computing (SOC)");
                        } else if(message.getText().equals("2")){
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Education (SOE)");
                        } else if(message.getText().equals("3")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Multimedia Technology & Communication (SMMTC)");
                        } else if(message.getText().equals("4")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Quantitative Sciences (SQS)");
                        } else if(message.getText().equals("5")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Applied Psychology, Social Work and Policy (SAPSP)");
                        } else if(message.getText().equals("6")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Business Management (SBM)");
                        } else if(message.getText().equals("7")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("Islamic Business School (IBS)");
                        } else if(message.getText().equals("8")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("Tunku Puteri Intan Safinaz School of Accountancy (TISSA)");
                        } else if(message.getText().equals("9")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School of Economics, Finance & Banking (SEFB)");
                        } else if(message.getText().equals("10")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School of Technology Management & Logistics (STML)");
                        } else if(message.getText().equals("11")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("UUM National Golf Academy (UUMNGA)");
                        } else if(message.getText().equals("12")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Government (SOG)");
                        } else if(message.getText().equals("13")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Law (SOL)");
                        } else if(message.getText().equals("14")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of International Studies (SOIS)");
                        } else if(message.getText().equals("15")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Tourism, Hospitality & Event Management (STHEM)");
                        } else if(message.getText().equals("16")) {
                            schoolAdminMap.get(message.getChatId()).setSchoolName("School Of Creative Industry Management & Performing Arts (SCIMPA)");
                        }


                        //set新的State
                        userState.put(message.getChatId(), "Register:RoomName");
                        sendMessage.setText("Where is the school building's location?\nExample: ");
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:RoomName":
                        // save building location
                        schoolAdminMap.get(message.getChatId()).setBuildingLoc(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Register:RoomDescription");
                        sendMessage.setText("Please give 1 room name where the students can access and book under <School_Name>\nExample: ");
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:RoomDescription":
                        // save room name
                        RegisterRoomMap.get(message.getChatId()).setRoomName(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Register:MaximumCapacity");
                        sendMessage.setText("Please give you give me a brief description about the room.\nExample: School of Computing(SOC)");
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:MaximumCapacity":
                        // save room desc
                        RegisterRoomMap.get(message.getChatId()).setRoomDesc(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Register:RoomType");
                        sendMessage.setText("Which is the maximum capacity of this room\nExample: 40");
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:RoomType":
                        // save room max capacity
                        RegisterRoomMap.get(message.getChatId()).setRoomMaxCap(message.getText());

                        //set新的State
                        userState.put(message.getChatId(), "Register:Check2");
                        sendMessage.setText("How about the type of room?\nExample: meeting room / conference");
                        sendMessage.setChatId(message.getChatId());

                        break;

                    case "Register:Check2":
                    case "Register:Chan_SchoolName":
                    case "Register:Chan_OfficeTelNo":
                    case "Register:Chan_BuildingLoc":
                    case "Register:Chan_RoomName":
                    case "Register:Chan_RoomDesc":
                    case "Register:Chan_RoomMaxCap":
                    case "Register:Chan_RoomType":

                        boolean msg1 = false;
                        if (userState.get(message.getChatId()).equals("Register:Check2")) {
                            if (userState.get(message.getChatId()).equals("Register:Chan_SchoolName")) {
                                //if (userState.get(message.getChatId()).equals("Register:Chan_SchoolName")) {
                                schoolAdminMap.get(message.getChatId()).setSchoolName(message.getText());
                                msg1 = true;
                            }
                            else if (userState.get(message.getChatId()).equals("Register:Chan_OfficeTelNo")) {
                                schoolAdminMap.get(message.getChatId()).setOfficeTelNo(message.getText());
                                msg1 = true;
                            } else if (userState.get(message.getChatId()).equals("Register:Chan_BuildingLoc")) {
                                schoolAdminMap.get(message.getChatId()).setBuildingLoc(message.getText());
                                msg1 = true;
                            } else if (userState.get(message.getChatId()).equals("Register:Chan_RoomName")) {
                                RegisterRoomMap.get(message.getChatId()).setRoomName(message.getText());
                                msg1 = true;
                            } else if (userState.get(message.getChatId()).equals("Register:Chan_RoomDesc")) {
                                RegisterRoomMap.get(message.getChatId()).setRoomDesc(message.getText());
                                msg1 = true;
                            } else if ( userState.get(message.getChatId()).equals("Register:Chan_RoomMaxCap")) {
                                RegisterRoomMap.get(message.getChatId()).setRoomMaxCap(message.getText());
                                msg1 = true;
                            } else if (userState.get(message.getChatId()).equals("Register:Chan_RoomType")) {
                                RegisterRoomMap.get(message.getChatId()).setRoomType(message.getText());
                                msg1 = true;
                            }
                        }

                        if (msg1) {
                            String RoomInfo = "Are these the correct information to register the room?\n" +
                                    "\nSchool: " + schoolAdminMap.get(message.getChatId()).getSchoolName() +
                                    "\nOffice Number: " + schoolAdminMap.get(message.getChatId()).getOfficeTelNo() +
                                    "\nBuilding Location: " + schoolAdminMap.get(message.getChatId()).getBuildingLoc() +
                                    "\nRoom Name: " + RegisterRoomMap.get(message.getChatId()).getRoomName() +
                                    "\nRoom Description: " + RegisterRoomMap.get(message.getChatId()).getRoomDesc() +
                                    "\nMaximum Capacity: " + RegisterRoomMap.get(message.getChatId()).getRoomMaxCap() +
                                    "\nRoom Type: " + RegisterRoomMap.get(message.getChatId()).getRoomType();


                            sendMessage = new SendMessage();
                            sendMessage.setText(RoomInfo);
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
                            inlineKeyboardButton12.setCallbackData("Register:Apply");
                            inlineKeyboardButton13.setCallbackData("Register:ChangeSchoolData");
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

            if (buttonData[0].equals("Book")) {
                if (data.equals("Book:Book_Y")) {

                    //记得在save东西之后换state,可以看Book:IC做example
                    userState.put(message.getChatId(), "Book:IC");
                    sendMessage.setText("May I have the your NAME (as per NRIC/PASSPORT) please?" +
                            "\n\n P.S.:Don't worry, you can edit your information after the information are entered ;)");
                }
            }

            if (buttonData[0].equals("Register")) {
                if (data.equals("Register:Register_Y")) {

                    userState.put(message.getChatId(), "Register:IC");
                    sendMessage.setText("May I have the your NAME (as per NRIC/PASSPORT) please?");
                } else if (data.equals("Register:Register_N")) {
                    sendMessage.setText("I'll be here whenever you need me :)");
                    sendMessage.setChatId(message.getChatId());
                }

            }

            else if (data.equals("Register:Apply")) {

                databaseManager.insertResRoom(schoolAdminMap.get(message.getChatId()).getSchoolName(), schoolAdminMap.get(message.getChatId()).getOfficeTelNo(),
                        schoolAdminMap.get(message.getChatId()).getBuildingLoc(), RegisterRoomMap.get(message.getChatId()).getRoomName(),
                        RegisterRoomMap.get(message.getChatId()).getRoomDesc(), RegisterRoomMap.get(message.getChatId()).getRoomMaxCap(), RegisterRoomMap.get(message.getChatId()).getRoomType());

            }

            else if(data.equals("Register:ChangeSchoolData")){
                String RoomInfo = "Are these the correct information to register the room?\n" +
                        "\nSchool: " + schoolAdminMap.get(message.getChatId()).getSchoolName() +
                        "\nOffice Number: " + schoolAdminMap.get(message.getChatId()).getOfficeTelNo() +
                        "\nBuilding Location: " + schoolAdminMap.get(message.getChatId()).getBuildingLoc() +
                        "\nRoom Name: " + RegisterRoomMap.get(message.getChatId()).getRoomName() +
                        "\nRoom Description: " + RegisterRoomMap.get(message.getChatId()).getRoomDesc() +
                        "\nMaximum Capacity: " + RegisterRoomMap.get(message.getChatId()).getRoomMaxCap() +
                        "\nRoom Type: " + RegisterRoomMap.get(message.getChatId()).getRoomType();


                sendMessage = new SendMessage();
                sendMessage.setText(RoomInfo);
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
                InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("School");
                inlineKeyboardButton2.setText("Office Number");
                inlineKeyboardButton3.setText("Building Location");
                inlineKeyboardButton4.setText("Room Name");
                inlineKeyboardButton5.setText("Room Description");
                inlineKeyboardButton6.setText("Maximum Capacity");
                inlineKeyboardButton7.setText("Room Type");
                inlineKeyboardButton1.setCallbackData("Register:Chan_SchoolName");
                inlineKeyboardButton2.setCallbackData("Register:Chan_OfficeTelNo");
                inlineKeyboardButton3.setCallbackData("Register:Chan_BuildingLoc");
                inlineKeyboardButton4.setCallbackData("Register:Chan_RoomName");
                inlineKeyboardButton5.setCallbackData("Register:Chan_RoomDesc");
                inlineKeyboardButton6.setCallbackData("Register:Chan_RoomMaxCap");
                inlineKeyboardButton7.setCallbackData("Register:Chan_RoomType");
                inlineKeyboardButtonList1.add(inlineKeyboardButton1);
                inlineKeyboardButtonList1.add(inlineKeyboardButton2);
                inlineKeyboardButtonList2.add(inlineKeyboardButton3);
                inlineKeyboardButtonList2.add(inlineKeyboardButton4);
                inlineKeyboardButtonList3.add(inlineKeyboardButton5);
                inlineKeyboardButtonList3.add(inlineKeyboardButton6);
                inlineKeyboardButtonList4.add(inlineKeyboardButton7);
                inlineButtons.add(inlineKeyboardButtonList1);
                inlineButtons.add(inlineKeyboardButtonList2);
                inlineButtons.add(inlineKeyboardButtonList3);
                inlineButtons.add(inlineKeyboardButtonList4);
                inlineKeyboardMarkup.setKeyboard(inlineButtons);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            }
            else if(data.equals("Register:Chan_SchoolName") || data.equals("Register:Chan_OfficeTelNo") ||
                    data.equals("Register:Chan_BuildingLoc") || data.equals("Register:Chan_RoomName") ||
                    data.equals("Register:Chan_RoomDesc") || data.equals("Register:Chan_RoomMaxCap") ||
                    data.equals("Register:Chan_RoomType")) {
                if (data.equals("Register:Chan_SchoolName")) {
                    userState.put(message.getChatId(), "Register:Chan_SchoolName");
                    sendMessage.setText("What do you want to change the school name to?\n\n" +
                            "Example: School Of Economics, Finance & Banking (SEFB)");
                } else if (data.equals("Register:Chan_OfficeTelNo")) {
                    userState.put(message.getChatId(), "Register:Chan_OfficeTelNo");
                    sendMessage.setText("What do you want to change the office telephone number to?\n\n" +
                            "Example: 044561234");
                } else if (data.equals("Register:Chan_BuildingLoc")) {
                    userState.put(message.getChatId(), "Register:Chan_BuildingLoc");
                    sendMessage.setText("What do you want to change the building location to?\n\n" +
                            "Example: ");
                } else if (data.equals("Register:Chan_RoomName")) {
                    userState.put(message.getChatId(), "Register:Chan_RoomName");
                    sendMessage.setText("What do you want to change the room name to?\n\n" +
                            "Example: ");
                } else if (data.equals("Register:Chan_RoomDesc")) {
                    userState.put(message.getChatId(), "Register:Chan_RoomDesc");
                    sendMessage.setText("What do you want to change the room description to?\n\n" +
                            "Example: ");
                }else if (data.equals("Register:Chan_RoomMaxCap")) {
                    userState.put(message.getChatId(), "Register:Chan_RoomMaxCap");
                    sendMessage.setText("What do you want to change the room maximum capacity to?\n\n" +
                            "Example: 50");
                } else if (data.equals("Register:Chan_RoomType")) {
                    userState.put(message.getChatId(), "Register:Chan_RoomType");
                    sendMessage.setText("What do you want to change the room type to?\n\n" +
                            "Example: laboratory");
                }

                sendMessage.setChatId(message.getChatId());

            }




        }
    }
}
