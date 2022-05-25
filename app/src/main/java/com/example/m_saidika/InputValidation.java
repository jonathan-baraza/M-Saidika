package com.example.m_saidika;

import java.util.regex.Pattern;

public class InputValidation {
    public boolean isValidName(String target) {

        return Pattern.compile("^[A-Za-z]+$").matcher(target).matches();
    }
    public boolean isValidEmail(String target) {
        return Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$").matcher(target).matches();
    }

}
