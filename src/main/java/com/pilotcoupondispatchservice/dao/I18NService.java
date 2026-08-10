package com.pilotcoupondispatchservice.dao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ১/১২/২২
 * Time: ৮:০১ AM
 * Email: mdshamim723@gmail.com
 */

public interface I18NService {

    public String getMessage(String code);

    public String getMultipleMessage(List<String> codes);

    public byte[] getBytes(String code);

}
