package org.java.training.web.servlet.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServletConstants {

    public final String NAME = "name";
    public final String DEFAULT_NAME = "Buddy";
    public final String CUSTOM_SESSION_HEADER = "X-Custom-Session";
    public final String SESSION_TTL_HEADER = "X-Session-TTL";
    public final String COOKIE_HEADER = "Cookie";
    public final String SESSIONID_PARAM = "SESSIONID=";
    public final String SESSIONID_COOKIE = "SESSIONID";

}
