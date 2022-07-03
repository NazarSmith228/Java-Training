package org.java.training.web.socket.ssl.response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString(includeFieldNames = false)
public enum ResponseType {

    BODY("Body"), HEADERS("Headers");

    String value;
}
