package org.cresplanex.api.state.userpreferenceservice.exception;

import build.buf.gen.userpreference.v1.UserPreferenceServiceErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPreferenceNotFoundException extends ServiceException {

    private final FindType findType;
    private final String aggregateId;

    public UserPreferenceNotFoundException(FindType findType, String aggregateId) {
        this(findType, aggregateId, "Model not found: " + findType.name() + " with id " + aggregateId);
    }

    public UserPreferenceNotFoundException(FindType findType, String aggregateId, String message) {
        super(message);
        this.findType = findType;
        this.aggregateId = aggregateId;
    }

    public UserPreferenceNotFoundException(FindType findType, String aggregateId, String message, Throwable cause) {
        super(message, cause);
        this.findType = findType;
        this.aggregateId = aggregateId;
    }

    public enum FindType {
        BY_ID,
        BY_USER_ID
    }

    @Override
    public UserPreferenceServiceErrorCode getServiceErrorCode() {
        return UserPreferenceServiceErrorCode.USER_PREFERENCE_SERVICE_ERROR_CODE_USER_PREFERENCE_NOT_FOUND;
    }

    @Override
    public String getErrorCaption() {
        return switch (findType) {
            case BY_ID -> "User Preference not found (ID = %s)".formatted(aggregateId);
            case BY_USER_ID -> "User Preference not found (User ID = %s)".formatted(aggregateId);
            default -> "User Preference not found";
        };
    }
}
