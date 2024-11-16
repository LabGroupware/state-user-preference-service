package org.cresplanex.api.state.userpreferenceservice.constants;

public class ServerErrorCode {

    public static final String SUCCESS = "0000.0000";
    public static final String INTERNAL_SERVER_ERROR = "0000.1000";
    public static final String VALIDATION_ERROR = "0000.1001";
    public static final String METHOD_NOT_ALLOWED = "0000.1002";
    public static final String NOT_SUPPORT_CONTENT_TYPE = "0000.1003";
    public static final String AUTHENTICATION_FAILED = "0000.1004";
    public static final String AUTHORIZATION_FAILED = "0000.1005";
    public static final String ACCESS_DENIED = "0000.1006";
    public static final String METHOD_ARGUMENT_TYPE_MISMATCH = "0000.1007";
    public static final String MISSING_PATH_VARIABLE = "0000.1008";
    public static final String EXCEED_MAX_UPLOAD_SIZE = "0000.1009";
    public static final String NOT_FOUND_HANDLER = "0000.1010";
    public static final String NOT_READABLE_REQUEST = "0000.1011";

    public static final String JOB_COMPLETED = "0001.0000";
    public static final String JOB_FAILED = "0001.0001";
    public static final String JOB_PROCESSING = "0001.0002";
    public static final String JOB_NOT_FOUND = "0001.0003";
    public static final String JOB_NOT_INITIALIZED = "0001.0004";

    public static final String WS_SUCCESS = "0002.0000";
    public static final String WS_INTERNAL_ERROR = "0002.1000";
    public static final String WS_VALIDATION_ERROR = "0002.1001";
    public static final String WS_METHOD_NOT_ALLOWED = "0002.1002";
    public static final String WS_NOT_SUPPORT_CONTENT_TYPE = "0002.1003";
    public static final String WS_AUTHENTICATION_FAILED = "0002.1004";
    public static final String WS_AUTHORIZATION_FAILED = "0002.1005";
    public static final String WS_ACCESS_DENIED = "0002.1006";
    public static final String WS_METHOD_ARGUMENT_TYPE_MISMATCH = "0002.1007";
    public static final String WS_MISSING_PATH_VARIABLE = "0002.1008";
    public static final String WS_EXCEED_MAX_UPLOAD_SIZE = "0002.1009";
    public static final String WS_NOT_FOUND_HANDLER = "0002.1010";
    public static final String WS_NOT_READABLE_REQUEST = "0002.1011";

    public static final String STOMP_SUCCESS = "0003.0000";
    public static final String STOMP_INTERNAL_ERROR = "0003.1000";
    public static final String STOMP_VALIDATION_ERROR = "0003.1001";
    public static final String STOMP_METHOD_NOT_ALLOWED = "0003.1002";
    public static final String STOMP_NOT_SUPPORT_CONTENT_TYPE = "0003.1003";
    public static final String STOMP_AUTHENTICATION_FAILED = "0003.1004";
    public static final String STOMP_AUTHORIZATION_FAILED = "0003.1005";
    public static final String STOMP_ACCESS_DENIED = "0003.1006";
    public static final String STOMP_METHOD_ARGUMENT_TYPE_MISMATCH = "0003.1007";
    public static final String STOMP_MISSING_PATH_VARIABLE = "0003.1008";
    public static final String STOMP_EXCEED_MAX_UPLOAD_SIZE = "0003.1009";
    public static final String STOMP_NOT_FOUND_HANDLER = "0003.1010";
    public static final String STOMP_NOT_READABLE_REQUEST = "0003.1011";

    public static final String USER_PROFILE_SUCCESS = "1000.0000";
    public static final String USER_PROFILE_INTERNAL_ERROR = "1000.1000";

    public static final String USER_PRESENCE_SUCCESS = "1001.0000";
    public static final String USER_PRESENCE_INTERNAL_ERROR = "1001.1000";

    public static final String USER_PREFERENCE_SUCCESS = "1002.0000";
    public static final String USER_PREFERENCE_INTERNAL_ERROR = "1002.1000";
    public static final String USER_PREFERENCE_NOT_FOUND = "1002.1001";
    public static final String USER_PREFERENCE_DUPLICATE = "1002.1002";
    public static final String USER_PREFERENCE_INVALID_THEME = "1002.1003";
    public static final String USER_PREFERENCE_INVALID_LANGUAGE = "1002.1004";
    public static final String USER_PREFERENCE_INVALID_TIMEZONE = "1002.1005";

    public static final String ORGANIZATION_SUCCESS = "1003.0000";
    public static final String ORGANIZATION_INTERNAL_ERROR = "1003.1000";
    public static final String ORGANIZATION_NOT_FOUND = "1003.1001";
    public static final String ORGANIZATION_DUPLICATE = "1003.1002";
    public static final String ORGANIZATION_RESERVED_NAME = "1003.1003";
    public static final String ORGANIZATION_OWNER_MUST_BE_USER = "1003.1004";
    public static final String ORGANIZATION_INVALID_PLAN = "1003.1005";
    public static final String ORGANIZATION_ALREADY_EXIST_USER = "1003.1006";
    public static final String ORGANIZATION_NOT_EXIST_USER = "1003.1007";

    public static final String TEAM_SUCCESS = "1004.0000";
    public static final String TEAM_INTERNAL_ERROR = "1004.1000";
    public static final String TEAM_NOT_FOUND = "1004.1001";
    public static final String TEAM_DUPLICATE = "1004.1002";
    public static final String TEAM_RESERVED_NAME = "1004.1003";
    public static final String TEAM_OWNER_MUST_BE_USER = "1004.1004";
    public static final String TEAM_ALREADY_EXIST_USER = "1004.1005";
    public static final String TEAM_NOT_EXIST_USER = "1004.1006";
    public static final String TEAM_NOT_ALLOWED_ON_DEFAULT_TEAM = "1004.1007";

    public static final String PLAN_SUCCESS = "1005.0000";
    public static final String PLAN_INTERNAL_ERROR = "1005.1000";
    public static final String PLAN_NOT_FOUND = "1005.1001";
    public static final String PLAN_DUPLICATE = "1005.1002";
    public static final String TASK_NOT_FOUND = "1005.1003";
    public static final String TASK_DUPLICATE = "1005.1004";
    public static final String TASK_INVALID_STATUS = "1005.1005";

    public static final String STORAGE_SUCCESS = "1006.0000";
    public static final String STORAGE_INTERNAL_ERROR = "1006.1000";
    public static final String STORAGE_NOT_FOUND = "1006.1001";
    public static final String STORAGE_DUPLICATE = "1006.1002";
    public static final String STORAGE_FILE_OBJECT_NOT_FOUND = "1006.1003";
    public static final String STORAGE_FILE_OBJECT_DUPLICATE = "1006.1004";
}
