package com.programdoo.transport.data.repositories;

import com.auth0.android.jwt.JWT;
import com.programdoo.transport.data.models.dtos.LoggedUserModel;
import com.programdoo.transport.data.models.enums.Roles;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.StringUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;

@Singleton
public class SessionRepository {
    @Getter @Setter
    private LoggedUserModel user;
    private final PreferencesRepository preferences;

    @Inject
    public SessionRepository(
            PreferencesRepository preferences) {
        this.preferences = preferences;
    }

    public void initialize() {
        if (user == null) {
            String accessToken = preferences.getString(Constants.KEY_ACCESS_TOKEN);
            if (StringUtil.isNullOrEmpty(accessToken))
                return;

            JWT jwt = new JWT(accessToken);
            user = LoggedUserModel.createFromJWT(jwt);
        }
    }
    public boolean isUserTrainer() {
        return user != null && user.getRole() == Roles.TRAINER;
    }
    public boolean isUserTrainee() {
        return user != null && user.getRole() == Roles.TRAINEE;
    }
    public boolean isUserAdmin() {
        return user != null
                && (user.getRole() == Roles.ADMIN
                    || user.getRole() == Roles.SUPERVISOR);
    }

    public boolean isUserStaff() {
        return isUserTrainer() || isUserAdmin();
    }
    public boolean isUserClient() {
        return isUserTrainee();
    }

    public Integer getEntityId() {
        return user.getEntityId();
    }
    public int getUserId() {
        return user.getUserId();
    }
    public String getFullName() {
        return user.getFullName();
    }
    public String getFullNameShort() {
        return user.getFullNameShort();
    }
}
