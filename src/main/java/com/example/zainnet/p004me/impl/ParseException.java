package com.example.zainnet.p004me.impl;

import com.example.zainnet.p004me.legrange.mikrotit.MikrotikApiException;

/**
 * Exception thrown if the parser encounters an error while parsing a command line.
 * @author GideonLeGrange
 */
public class ParseException extends MikrotikApiException {

    ParseException(String msg) {
        super(msg);
    }

    ParseException(String msg, Throwable err) {
        super(msg, err);
    }
    
    
    
}
