package com.pilotcoupondispatchservice.dao;

import java.util.List;

public interface I18NService {

    public String getMessage(String code);

    public String getMultipleMessage(List<String> codes);

    public byte[] getBytes(String code);

}
