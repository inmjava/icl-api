import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class main {
    public static void main(String[] args) {
    	System.out.println("** ---------------------------------------------------------------------------------------- **");
        // Original number
        BigDecimal number = new BigDecimal("00856518930");
        
        // Format the number in CPF format
        String formattedCPF = formatCPF(number.toString());

        // Print the formatted CPF
        System.out.println(formattedCPF);
    }

    private static String formatCPF(String number) {
        String numberString = zeroPadString(number,11);

        // Insert separators and return the formatted CPF
        return numberString.substring(0, 3) + "." + numberString.substring(3, 6) + "." +
               numberString.substring(6, 9) + "-" + numberString.substring(9);
    }
    
    private static String zeroPadString(String input, int length) {
        return StringUtils.leftPad(input, length, '0');
    }
}
