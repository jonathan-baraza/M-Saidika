package com.example.m_saidika;

import java.util.regex.Pattern;

public class InputValidation {
    public boolean isValidName(String target) {

        return Pattern.compile("^[A-Za-z]+$").matcher(target).matches();
    }
    public boolean isValidEmail(String target) {
        return Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$").matcher(target).matches();
    }
    public String sanitizePhoneNumber(String phone) {

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if (phone.length() == 13 && phone.startsWith("+")) {
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return phone;
    }

}
