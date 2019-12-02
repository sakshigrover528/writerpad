package com.xebia.fs101.model;

import java.util.Arrays;

public enum Status {
    DRAFT("draft"),PUBLISHED("published");
    private String value;

    private Status(String value)
    {
        this.value=value;
    }
    public static Status fromValue(String value)
    {
        for(Status status:values()){
            if(status.value.equalsIgnoreCase(value))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Status type"+value+" ,Allowed values are"
        + Arrays.toString(values()));
    }
}
