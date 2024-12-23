package org.cresplanex.api.state.userpreferenceservice.exception;

import build.buf.gen.userpreference.v1.*;
import build.buf.gen.userprofile.v1.*;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

     @GrpcExceptionHandler(UserPreferenceNotFoundException.class)
     public Status handleUserPreferenceNotFoundException(UserPreferenceNotFoundException e) {
        UserPreferenceServiceUserPreferenceNotFoundError.Builder descriptionBuilder =
                UserPreferenceServiceUserPreferenceNotFoundError.newBuilder()
                .setMeta(buildErrorMeta(e));

        switch (e.getFindType()) {
            case BY_ID:
                descriptionBuilder
                        .setFindFieldType(UserPreferenceUniqueFieldType.USER_PREFERENCE_UNIQUE_FIELD_TYPE_USER_PREFERENCE_ID)
                        .setUserPreferenceId(e.getAggregateId());
                break;
            case BY_USER_ID:
                descriptionBuilder
                        .setFindFieldType(UserPreferenceUniqueFieldType.USER_PREFERENCE_UNIQUE_FIELD_TYPE_USER_ID)
                        .setUserId(e.getAggregateId());
                break;
        }

         return Status.NOT_FOUND
                 .withDescription(descriptionBuilder.build().toString())
                 .withCause(e);
     }

     private UserPreferenceServiceErrorMeta buildErrorMeta(ServiceException e) {
         return UserPreferenceServiceErrorMeta.newBuilder()
                 .setCode(e.getServiceErrorCode())
                 .setMessage(e.getErrorCaption())
                 .build();
     }

    @GrpcExceptionHandler
    public Status handleInternal(Throwable e) {
        log.error("Internal error", e);

        String message = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";

         UserPreferenceServiceInternalError.Builder descriptionBuilder =
                 UserPreferenceServiceInternalError.newBuilder()
                         .setMeta(UserPreferenceServiceErrorMeta.newBuilder()
                                 .setCode(UserPreferenceServiceErrorCode.USER_PREFERENCE_SERVICE_ERROR_CODE_INTERNAL)
                                 .setMessage(message)
                                 .build());

         return Status.INTERNAL
                 .withDescription(descriptionBuilder.build().toString())
                 .withCause(e);
    }
}
