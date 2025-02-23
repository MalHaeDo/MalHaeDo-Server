package com.backend.malhaedo.global.error.exception.handler;


import com.backend.malhaedo.global.error.code.BaseErrorCode;
import com.backend.malhaedo.global.error.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
