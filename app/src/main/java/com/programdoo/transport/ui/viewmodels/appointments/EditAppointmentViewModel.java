package com.programdoo.transport.ui.viewmodels.appointments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentRecurrencePatternDto;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRecurrencePatternRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentRequestModel;
import com.programdoo.transport.data.models.dtos.appointments.SaveAppointmentsByPatternRequestModel;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.enums.Weekdays;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.AppointmentsRepository;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.databinding.FragmentEditAppointmentBinding;
import com.programdoo.transport.databinding.FragmentEditRecurrencePatternBinding;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.utils.TimeUtil;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;

@HiltViewModel
public class EditAppointmentViewModel extends BaseViewModel {
    @Getter
    private final AppointmentsRepository appointmentsRepository;
    @Getter
    private final CompaniesRepository companiesRepository;
    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final EmployeesRepository employeesRepository;

    @Getter
    private final LiveData<List<OrgUnitDto>> orgUnits;
    @Getter
    private final LiveData<List<TraineeDto>> trainees;
    @Getter
    private final LiveData<List<EmployeeDto>> employees;
    @Getter
    private final LiveData<AppointmentDto> appointment;
    @Getter @Setter
    private SaveAppointmentRecurrencePatternRequestModel recurrenceDraft = new SaveAppointmentRecurrencePatternRequestModel();
    @Getter @Setter
    private SaveAppointmentRequestModel appointmentDraft = new SaveAppointmentRequestModel();
    @Getter
    private final LiveData<AppointmentRecurrencePatternDto> recurrencePattern;

    @Getter @Setter
    private Integer currentOrgUnit = null;
    @Getter @Setter
    private Integer trialTraineeId = null;
    @Getter @Setter
    private boolean isEditMode = false;
    @Getter @Setter
    private boolean isByRecurrence = false;

    @Inject
    public EditAppointmentViewModel(
            AppointmentsRepository appointmentsRepository,
            CompaniesRepository companiesRepository,
            TraineesRepository traineesRepository,
            EmployeesRepository employeesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.appointmentsRepository = appointmentsRepository;
        this.companiesRepository = companiesRepository;
        this.employeesRepository = employeesRepository;
        this.traineesRepository = traineesRepository;
        this.orgUnits = LiveDataReactiveStreams.fromPublisher(
                companiesRepository.getOrgUnits()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.trainees = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getTrainees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.employees = LiveDataReactiveStreams.fromPublisher(
                employeesRepository.getEmployees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.appointment = LiveDataReactiveStreams.fromPublisher(
                appointmentsRepository.getAppointment()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.recurrencePattern = LiveDataReactiveStreams.fromPublisher(
                appointmentsRepository.getRecurrence()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }

    public void saveAppointment(SaveAppointmentRequestModel saveData) {
        handleSingle(appointmentsRepository.saveAppointment(saveData),
                id -> {
                        toastEvent.setValue(R.string.msg_saved);
                        navigationEvent.setValue(1);
                }, error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("error", error.getMessage());
                });
    }
    public void deleteAppointment(int id, int userId) {
        handleCompletable(appointmentsRepository.deleteAppointment(id, userId),
                () -> {
                    toastEvent.setValue(R.string.msg_delete_success);
                    navigationEvent.setValue(1);
                }, error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("API error", error.getMessage());
                });
    }
    public void saveRecurrencePattern(SaveAppointmentRecurrencePatternRequestModel saveData) {
        handleSingle(appointmentsRepository.saveRecurrencePattern(saveData),
                id -> {
                    this.appointmentsRepository.getRecurrencePattern(id);
                    navigationEvent.setValue(1);
                },
                error -> toastEvent.setValue(R.string.msg_error_api));
    }
    public void saveAppointmentsByRecurrencePattern(SaveAppointmentsByPatternRequestModel saveData) {
        handleSingle(appointmentsRepository.saveAppointmentsByRecurrencePattern(saveData),
                id -> {
                    toastEvent.setValue(R.string.label_save);
                    navigationEvent.setValue(1);
                }, error -> toastEvent.setValue(R.string.msg_error_api));
    }

    public void saveAction() {
        if (isByRecurrence && !isEditMode) {
            SaveAppointmentsByPatternRequestModel saveData = new SaveAppointmentsByPatternRequestModel();
            saveData.setSource(appointmentDraft);
            saveData.setPattern(recurrencePattern.getValue());
            saveAppointmentsByRecurrencePattern(saveData);
        }
        else {
            saveAppointment(appointmentDraft);
        }
    }

    public void applyAppointmentFromBinding(FragmentEditAppointmentBinding binding) {
        appointmentDraft.setTraineeId(binding.selTrainee.getSelectedId());
        appointmentDraft.setTrainerId(binding.selTrainer.getSelectedId());
        appointmentDraft.setOrgUnitId(binding.selOrgUnit.getSelectedId());
        appointmentDraft.setDate(DateUtil.parse(binding.ifDate.getText()));
        appointmentDraft.setStartTime(TimeUtil.parse(binding.ifStartTime.getText()));
        appointmentDraft.setEndTime(TimeUtil.parse(binding.ifEndTime.getText()));
        appointmentDraft.setFinished(binding.sFinished.isChecked());
        appointmentDraft.setCancelled(binding.sCancelled.isChecked());
        if (StringUtil.isNullOrEmpty(binding.ifDescription.getText())) {
            appointmentDraft.setSessionDescription(binding.ifDescription.getText());
        }
    }
    public void applyRecurrenceFromBinding(FragmentEditRecurrencePatternBinding binding) {
        recurrenceDraft.setDateFrom(DateUtil.parse(binding.ifRecurFrom.getText()));
        recurrenceDraft.setDateTo(DateUtil.parse(binding.ifRecurTo.getText()));
        ArrayList<Integer> weekdayIds = (ArrayList<Integer>) binding.mselWeekdays.getSelectedIds();
        if (weekdayIds.contains(Weekdays.MONDAY.getValue())) recurrenceDraft.setMon(true);
        if (weekdayIds.contains(Weekdays.TUESDAY.getValue())) recurrenceDraft.setTue(true);
        if (weekdayIds.contains(Weekdays.WEDNESDAY.getValue())) recurrenceDraft.setWed(true);
        if (weekdayIds.contains(Weekdays.THURSDAY.getValue())) recurrenceDraft.setThu(true);
        if (weekdayIds.contains(Weekdays.FRIDAY.getValue())) recurrenceDraft.setFri(true);
        if (weekdayIds.contains(Weekdays.SATURDAY.getValue())) recurrenceDraft.setSat(true);
        if (weekdayIds.contains(Weekdays.SUNDAY.getValue())) recurrenceDraft.setSun(true);
    }
}
