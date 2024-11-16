package org.cresplanex.api.state.userpreferenceservice.utils;

import build.buf.gen.cresplanex.nova.v1.NullableString;

import java.time.LocalDate;

public class ValueFromNullable {

    public static NullableString toNullableString (String value) {
        NullableString.Builder builder = NullableString.newBuilder()
                .setHasValue(value != null);

        if (value != null) {
            builder.setValue(value);
        }

        return builder.build();
    }

    public static NullableString toNullableString (LocalDate value) {
        return value != null ? toNullableString(String.valueOf(value)) : NullableString.getDefaultInstance();
    }
}
