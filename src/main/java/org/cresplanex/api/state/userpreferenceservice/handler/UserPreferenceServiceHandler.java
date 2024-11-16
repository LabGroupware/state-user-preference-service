package org.cresplanex.api.state.userpreferenceservice.handler;

import build.buf.gen.userpreference.v1.*;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.mapper.proto.ProtoMapper;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.cresplanex.api.state.userpreferenceservice.service.UserPreferenceService;

import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class UserPreferenceServiceHandler extends UserPreferenceServiceGrpc.UserPreferenceServiceImplBase {

    private final UserPreferenceService userPreferenceService;

    @Override
    public void findUserPreference(FindUserPreferenceRequest request, StreamObserver<FindUserPreferenceResponse> responseObserver) {
        UserPreferenceEntity userPreference = userPreferenceService.findById(request.getUserPreferenceId());

        UserPreference userPreferenceProto = ProtoMapper.convert(userPreference);
        FindUserPreferenceResponse response = FindUserPreferenceResponse.newBuilder()
                .setUserPreference(userPreferenceProto)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findUserPreferenceByUserId(FindUserPreferenceByUserIdRequest request, StreamObserver<FindUserPreferenceByUserIdResponse> responseObserver) {
        UserPreferenceEntity userPreference = userPreferenceService.findByUserId(request.getUserId());

        UserPreference userPreferenceProto = ProtoMapper.convert(userPreference);
        FindUserPreferenceByUserIdResponse response = FindUserPreferenceByUserIdResponse.newBuilder()
                .setUserPreference(userPreferenceProto)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // TODO: pagination + with count
    @Override
    public void getUserPreferences(GetUserPreferencesRequest request, StreamObserver<GetUserPreferencesResponse> responseObserver) {
        List<UserPreferenceEntity> userPreferences = userPreferenceService.get();

        List<UserPreference> userPreferenceProtos = userPreferences.stream()
                .map(ProtoMapper::convert).toList();
        GetUserPreferencesResponse response = GetUserPreferencesResponse.newBuilder()
                .addAllUserPreferences(userPreferenceProtos)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserPreference(UpdateUserPreferenceRequest request, StreamObserver<UpdateUserPreferenceResponse> responseObserver) {
        UserPreferenceEntity userPreference = userPreferenceService.findById(request.getUserPreferenceId());
        if (request.getLanguage().getHasValue()) userPreference.setLanguage(request.getLanguage().getValue());
        if (request.getTheme().getHasValue()) userPreference.setTheme(request.getTheme().getValue());
        if (request.getTimezone().getHasValue()) userPreference.setTimezone(request.getTimezone().getValue());
        String jobId = userPreferenceService.beginUpdate(request.getOperatorId(), userPreference);

        UpdateUserPreferenceResponse response = UpdateUserPreferenceResponse.newBuilder()
                .setJobId(jobId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
