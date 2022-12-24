package my.uum;

public class InputFormatChecker {
    public boolean checkICFormat(String ICNO){
        char c;
        int num;
        int sum = 0;

        //1. Check whether the input is ICNO
        try{
            Integer.parseInt(ICNO);
        } catch (NumberFormatException e){
            e.printStackTrace();
            //if not, return false
            return false;
        }

        //2. Check Whether the ICNO format is correct
        //i. Check whether the month is over 12
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
}
