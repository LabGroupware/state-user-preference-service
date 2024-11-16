package org.cresplanex.api.state.userpreferenceservice.entity;

import org.cresplanex.api.state.userpreferenceservice.utils.OriginalAutoGenerate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_preferences")
public class UserPreferenceEntity extends BaseEntity implements Cloneable {

    @Override
    public void setId(String id) {
        this.userPreferenceId = id;
    }

    @Override
    public String getId() {
        return this.userPreferenceId;
    }

    @Id
    @OriginalAutoGenerate
    @Column(name = "user_preference_id", length = 100, nullable = false, unique = true)
    private String userPreferenceId;

    @Column(name = "user_id", length = 100, nullable = false, unique = true)
    private String userId;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "theme", length = 50)
    private String theme;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "notification_setting_id", length = 100)
    private String notificationSettingId;

    @Override
    public UserPreferenceEntity clone() {
        try {
            return (UserPreferenceEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
