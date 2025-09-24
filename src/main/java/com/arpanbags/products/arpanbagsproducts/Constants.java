package com.arpanbags.products.arpanbagsproducts;

public class Constants {

    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String COMPANY_NAME = "companyName";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String PASSWORD = "password";

    //Validations Message
    public static final String MOBILE_NUMBER_ALREADY_REGISTERED = "Mobile number already registered";
    public static final String EMAIL_ALREADY_REGISTERED = "Email Id already registered";

    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String INVALID_EMAIL = "Invalid email format";

    public static final String MOBILE_REQUIRED = "Mobile number is required";
    public static final String INVALID_MOBILE = "Mobile number must be exactly 10 digits";

    public static final String COMPANY_REQUIRED = "Company name is required";
    public static final String INVALID_COMPANY = "Company name must be between 2 and 100 characters";

    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String INVALID_PASSWORD = "Password must be between 8 and 64 characters";

    public static final String ADDRESS_REQUIRED = "Address is required";
    public static final String INVALID_ADDRESS = "Address must be between 5 and 200 characters";

    // Common words in exception
    public static final String FIELD = "Field '";
    public static final String CONTAINS_VULGAR_JUNKS = "' contains vulgar or junk words.";
    public static final String PROFANITY_VULGAR_WORDS_LIST = "profanity-words.txt";
    public static final String FAILED_TO_LOAD_PROFANITY_WORDS = "Failed to load profanity words";

}
