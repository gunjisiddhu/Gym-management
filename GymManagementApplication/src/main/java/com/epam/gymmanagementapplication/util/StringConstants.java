package com.epam.gymmanagementapplication.util;

public enum StringConstants {

    ENTERED_CONTROLLER_MESSAGE("Entered {} controller of {} class with value {}"),
    EXITING_CONTROLLER_MESSAGE("Exiting {} controller of {} class"),
    ENTERED_SERVICE_MESSAGE("Entered into {} service method of {} class with value {}"),
    EXITING_SERVICE_MESSAGE("Exiting {} service method of {} class"),
    ERROR_MESSAGE("Error in {} service method, exiting the entered Service method"),
    ENTERED_EXCEPTION_HANDLER("Entered {} exception handler to handle {} Exception");


    private final String value;

    StringConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
