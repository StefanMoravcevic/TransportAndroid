package com.programdoo.transport.utils;

public class Constants {
    public static final String APP_NAME = "Fitnes";
    // bundle and intent arguments
    public static final String ARG_ACTIVITY_ID = "activityId";
    public static final String ARG_APPOINTMENT_ID = "appointmentId";
    public static final String ARG_RECURRENCE_PATTERN_ID = "recurrencePatternId";
    public static final String ARG_MEMBERSHIP_ID = "membershipId";
    public static final String ARG_TRAINEE_ID = "traineeIdId";
    public static final String ARG_TRAINEE_NAME = "traineeName";
    public static final String ARG_EDIT_MODE = "editMode";
    public static final String ARG_APPOINTMENT_SHORTCUT = "appointmentShortcut";

    // shared preferences keys
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_REFRESH_TOKEN = "refreshToken";
    public static final String KEY_EMPLOYEE_ID = "employeeId";
    public static final String KEY_TRAINEE_ID = "traineeId";

    // fragment tags
    public static final String FRAG_TRAINEES_LIST = "frag_traineesList";
    public static final String FRAG_EDIT_TRAINEE_BASIC_INFO = "frag_editTraineeBasicInfo";
    public static final String FRAG_EDIT_TRAINEE_ADDITIONAL_INFO = "frag_editTraineeAdditionalInfo";
    public static final String FRAG_EDIT_TRAINEE_FITNESS_INFO = "frag_editTraineeFitnessInfo";
    public static final String FRAG_TRAINEE_INFO = "frag_traineeInfo";
    public static final String FRAG_APPOINTMENTS_CALENDAR = "frag_appointmentsCalendar";
    public static final String FRAG_APPOINTMENTS_LIST = "frag_appointmentsList";
    public static final String FRAG_APPOINTMENT_INFO = "frag_appointmentInfo";
    public static final String FRAG_EDIT_APPOINTMENT = "frag_editAppointment";
    public static final String FRAG_EDIT_MEMBERSHIP = "frag_editMembership";
    public static final String FRAG_MEMBERSHIP_INFO = "frag_membershipInfo";
    public static final String FRAG_MEMBERSHIP_LIST = "frag_membershipList";
    public static final String FRAG_MENU = "frag_menu";
    public static final String FRAG_CLIENT_MENU = "frag_clientMenu";
    public static final String FRAG_ACTIVITIES_LIST = "frag_activitiesList";
    public static final String FRAG_DATE_PICKER = "frag_datePicker";
    public static final String FRAG_TIME_PICKER = "frag_timePicker";
    public static final String FRAG_EDIT_SETTINGS = "frag_editSettings";
    public static final String FRAG_EDIT_RECURRENCE_PATTERN = "frag_editRecurrencePattern";

    // cross fragment messages
    public static final String MSG_TRAINEE_CREATED = "msg_traineeCreated";

    // jwt claims
    public static final String CLAIM_USER_ID = "nameid";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_EMPLOYEE_ID = "ClaimEmployeeId";
    public static final String CLAIM_TRAINEE_ID = "ClaimTraineeId";
    public static final String CLAIM_NAME = "given_name";
    public static final String CLAIM_SURNAME = "family_name";
}
