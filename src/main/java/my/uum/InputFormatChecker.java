package my.uum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputFormatChecker {

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
            Integer.parseInt(ICNO);
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

        if(day>31||day<1||month>12||month<1||year<1 )
            return false;
        else
            return true;

    }
}
