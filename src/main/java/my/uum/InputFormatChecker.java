package my.uum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is mainly for control the input format
 */
public class InputFormatChecker {

    /**
     * This method is to check the IC format for the date of birth and the 7th character must be 0
     * @param ICNO
     * @return format true or false
     */
    public boolean checkICFormat(String ICNO){
        char c;
        int num;
        int sum = 0;

        //1. Check Whether the ICNO format is correct
        // Check whether there are 12 digits
        if(ICNO.length() != 12)
            return false;

        //2. Check whether the input is numbers
        try{
            Long.parseLong(ICNO);
        } catch (NumberFormatException e){
            e.printStackTrace();
            //if not, return false
            return false;
        }


        //3 i. Check whether the month is over 12
        for(int i = 2; i < 4; i++){
            c = ICNO.charAt(i);
            num = Character.getNumericValue(c);

            if(i==2)
                sum = sum + num * 10;

            if(i==3)
                sum = sum + num;
        }

        if(sum > 12){
            return false;
        }

        //ii. Check whether the day is over 31
        sum = 0;
        for(int i = 4; i < 6; i++){
            c = ICNO.charAt(i);
            num = Character.getNumericValue(c);

            if(i==2)
                sum = sum + num * 10;

            if(i==3)
                sum = sum + num;
        }

        if(sum > 31){
            return false;
        }


        //iii. Check whether the 7th character is 0
        if(ICNO.charAt(6)=='0')
            return true;
        else
            return false;

    }

    /**
     * This method is to check the phone number format which the input is number and start with 0 as well as the number length
     * @param TelNo
     * @return format true or false
     */
    public boolean TelNumFormat(String TelNo){
        //1. Check whether the input is numbers
        try{
            Integer.parseInt(TelNo);
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println("User input wrong number format");
            //if not, return false
            return false;
        }

        //2. Check whether the first number is 0
        if(TelNo.charAt(0) != '0')
            return false;

        //3. check the length of Telephone number
        if(!(TelNo.length() >= 9) && TelNo.length() <= 12)
            return false;
        else
            return true;

    }

    /**
     * This method is to check the name format which the input is not just bunch or number
     * @param name
     * @return format true or false
     */
    public boolean NameFormat(String name){

        //Check whether the name is null
        if(name.length() == 0)
            return false;

        //Check whether the name if just bunch of numbers
        try{
            Integer.parseInt(name);
        } catch (NumberFormatException e){
            System.out.println("Name input correct");
            //if not, return false
            return true;
        }

        return false;
    }

    /**
     * This method is to check the email format which must have the symbol
     * @param email
     * @return format true or false
     */
    public boolean EmailFormat(String email){
        final String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        //Inernet抄来的，别理那么多哈哈哈哈
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /**
     * This method is to check the date format which only can input number
     * @param date
     * @return format true or false
     */
    public boolean DateFormat(String date){
        //date format reference 15/03/2024

        int day=0;
        int month=0;
        int year=0;


        if(!date.contains("/"))
            return false;

        String[] checker = date.split("/");

        if(checker.length != 3)
            return false;

        try{
            day = Integer.parseInt(checker[0]);
            month = Integer.parseInt(checker[1]);
            year = Integer.parseInt(checker[2]);

        }catch (NumberFormatException e){
            System.out.println("User date consist characters other than numbers");
            e.printStackTrace();
            return false;
        }

        if(month == 2 && day >29){
            return false;
        }

        else if((month==1||month==3||month==5||month==7||month==8||month==10||month==12) && day> 31){
            return false;
        }

        else if((month==4||month==6||month==9||month==11) && day>30){
            return false;
        }

        else if(day<1||month>12||month<1||year<1 )
            return false;
        else
            return true;
    }

    /**
     * THe purpose of this method is to ensure that the booking made is between 1 month to 1 year when booking is made
     * @param bookDate
     * @param bookMadeDate
     * @return format true or false
     */
    public boolean bookDate(Date bookDate, Date bookMadeDate){
        //bookDate = the date that user want to use the room
        //bookMadeDate = the date that user make the booking

        long difference_In_Time = bookDate.getTime() - bookMadeDate.getTime();


        long difference_In_Years
                = (difference_In_Time
                / (1000l * 60 * 60 * 24 * 365));

        long difference_In_Days
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        if(difference_In_Years>1)
            return false;

        else if (difference_In_Days<31)
            return false;

        else
            return true;

    }

    /**
     * the purpose of this method is to ensure that the booking made is not before the current time
     * @param bookDate the new date that user wants to book
     * @return
     */
    public boolean bookDateCurrent(Date bookDate){

        //this represents the current time(like a timestamp)
        Date date = new Date();

        //if the booking time set is before the current time, return false
        if(bookDate.before(date)){
            return false;
        }else
            return true;

    }

    /**
     * This method is to check the time format which the input will only have number
     * @param input
     * @return
     */
    public boolean timeFormat(String input){

        int hour;
        int minutes;


        if(!input.contains(":")){
            return false;
        }

        String[] time = input.split(":",2);

        /*
        if(time[0].length()!=2 || time[1].length()!=2)
            return false;
        */
        try{
            hour = Integer.parseInt(time[0]);
            minutes = Integer.parseInt(time[1]);
        }catch(NumberFormatException e){
            e.printStackTrace();
            System.out.println("User input time in wrong format");
            return false;
        }

        if(hour>23||hour<1||minutes>59||minutes<0)
            return false;
        else
            return true;
    }

    /**
     * This method is to check the time open is from 8am to 8pm
     * @param input
     * @return format true or false
     */
    public boolean timeOpen(String input){

        int hour;
        int minutes;

        String[] time = input.split(":", 2);

        hour = Integer.parseInt(time[0]);
        minutes = Integer.parseInt(time[1]);

        if(hour < 8)
            return false;
        else if (hour == 20 && minutes > 0)
            return false;
        else if (hour > 20)
            return false;
        else
            return true;


    }

    /**
     * This method is to check the user input roomID in correct format
     * @param room_id
     * @return
     */
    public boolean checkRoomIDInput(String room_id) {
        Integer Room_ID = 0;

        try {
            Room_ID = Integer.parseInt(room_id);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("User mis-input room id in incorrect format");
            return false;
        }
    }

}
