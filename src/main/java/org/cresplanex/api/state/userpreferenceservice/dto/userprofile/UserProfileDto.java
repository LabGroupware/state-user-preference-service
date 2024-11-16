package org.cresplanex.api.state.userpreferenceservice.dto.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileDto {

    private String userProfileId;

    private String userId;

    private String name;

    private String email;

    private String givenName;

    private String familyName;

    private String middleName;

    private String nickname;

    private String profile;

    private String picture;

    private String website;

    private String phone;

    private String gender;

    private LocalDate birthdate;

    private String zoneinfo;

    private String locale;
}
