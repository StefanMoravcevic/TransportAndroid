package com.programdoo.transport.data.models.dtos;

import com.auth0.android.jwt.JWT;
import com.programdoo.transport.data.models.enums.Roles;
import com.programdoo.transport.utils.Constants;

import java.text.MessageFormat;

import lombok.Getter;

public class LoggedUserModel {
    @Getter
    private Roles role;
    @Getter
    private int userId;
    @Getter
    private Integer entityId = null;
    @Getter
    private String name;
    @Getter
    private String surname;

    public String getFullName() {
        return MessageFormat.format("{0} {1}", name, surname);
    }
    public String getFullNameShort() {
        return MessageFormat.format("{0} {1}.", name, surname.substring(0, 1));
    }

    public static LoggedUserModel createFromJWT(JWT token) {
        LoggedUserModel user = new LoggedUserModel();
        Integer userId = token.getClaim(Constants.CLAIM_USER_ID).asInt();
        Integer employeeId = token.getClaim(Constants.CLAIM_EMPLOYEE_ID).asInt();
        Integer traineeId = token.getClaim(Constants.CLAIM_TRAINEE_ID).asInt();
        String role = token.getClaim(Constants.CLAIM_ROLE).asString();
        String name = token.getClaim(Constants.CLAIM_NAME).asString();
        String surname = token.getClaim(Constants.CLAIM_SURNAME).asString();

        if (userId != null)
            user.userId = userId;

        if (employeeId != null) {
            user.entityId = employeeId;
            user.role = Roles.TRAINER;
        }
        else if (traineeId != null) {
            user.entityId = traineeId;
            user.role = Roles.TRAINEE;
        }

        if (role != null && role.equals(Roles.SUPERVISOR.getDescription()))
            user.role = Roles.SUPERVISOR;
        else if (role != null && role.equals(Roles.ADMIN.getDescription()))
            user.role = Roles.ADMIN;

        user.name = name != null ? name : "";
        user.surname = surname != null ? surname : "";

        return user;
    }
}
