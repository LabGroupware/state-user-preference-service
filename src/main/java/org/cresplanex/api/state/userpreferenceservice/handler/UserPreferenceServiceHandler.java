package org.cresplanex.api.state.userpreferenceservice.handler;

import build.buf.gen.cresplanex.nova.v1.Count;
import build.buf.gen.cresplanex.nova.v1.SortOrder;
import build.buf.gen.userpreference.v1.*;
import org.cresplanex.api.state.common.entity.ListEntityWithCount;
import org.cresplanex.api.state.common.enums.PaginationType;
import org.cresplanex.api.state.userpreferenceservice.entity.UserPreferenceEntity;
import org.cresplanex.api.state.userpreferenceservice.enums.UserPreferenceSortType;
import org.cresplanex.api.state.userpreferenceservice.filter.userpreference.LanguageFilter;
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

    @Override
    public void getUserPreferences(GetUserPreferencesRequest request, StreamObserver<GetUserPreferencesResponse> responseObserver) {
        UserPreferenceSortType sortType = switch (request.getSort().getOrderField()) {
            default -> (request.getSort().getOrder() == SortOrder.SORT_ORDER_ASC) ?
                    UserPreferenceSortType.CREATED_AT_ASC : UserPreferenceSortType.CREATED_AT_DESC;
        };
        PaginationType paginationType;
        switch (request.getPagination().getType()) {
            case PAGINATION_TYPE_CURSOR -> paginationType = PaginationType.CURSOR;
            case PAGINATION_TYPE_OFFSET -> paginationType = PaginationType.OFFSET;
            default -> paginationType = PaginationType.NONE;
        }

        LanguageFilter languageFilter = new LanguageFilter(
                request.getFilterLanguage().getHasValue(), request.getFilterLanguage().getLanguagesList()
        );

        ListEntityWithCount<UserPreferenceEntity> userPreferences = userPreferenceService.get(
                paginationType, request.getPagination().getLimit(), request.getPagination().getOffset(),
                request.getPagination().getCursor(), sortType, request.getWithCount(), languageFilter);

        List<UserPreference> userPreferenceProtos = userPreferences.getData().stream()
                .map(ProtoMapper::convert).toList();
        GetUserPreferencesResponse response = GetUserPreferencesResponse.newBuilder()
                .addAllUserPreferences(userPreferenceProtos)
                .setCount(
                        Count.newBuilder().setIsValid(request.getWithCount())
                                .setCount(userPreferences.getCount()).build()
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPluralUserPreferences(GetPluralUserPreferencesRequest request, StreamObserver<GetPluralUserPreferencesResponse> responseObserver) {
        UserPreferenceSortType sortType = switch (request.getSort().getOrderField()) {
            default -> (request.getSort().getOrder() == SortOrder.SORT_ORDER_ASC) ?
                    UserPreferenceSortType.CREATED_AT_ASC : UserPreferenceSortType.CREATED_AT_DESC;
        };
        List<UserPreference> userPreferenceProtos = this.userPreferenceService.getByUserPreferenceIds(
                        request.getUserPreferenceIdsList(), sortType).stream()
                .map(ProtoMapper::convert).toList();
        GetPluralUserPreferencesResponse response = GetPluralUserPreferencesResponse.newBuilder()
                .addAllUserPreferences(userPreferenceProtos)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPluralUserPreferencesByUserId(GetPluralUserPreferencesByUserIdRequest request, StreamObserver<GetPluralUserPreferencesByUserIdResponse> responseObserver) {
        UserPreferenceSortType sortType = switch (request.getSort().getOrderField()) {
            default -> (request.getSort().getOrder() == SortOrder.SORT_ORDER_ASC) ?
                    UserPreferenceSortType.CREATED_AT_ASC : UserPreferenceSortType.CREATED_AT_DESC;
        };
        List<UserPreference> userPreferenceProtos = this.userPreferenceService.getByUserIds(
                        request.getUserIdsList(), sortType).stream()
                .map(ProtoMapper::convert).toList();
        GetPluralUserPreferencesByUserIdResponse response = GetPluralUserPreferencesByUserIdResponse.newBuilder()
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
