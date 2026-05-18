package com.programdoo.transport.ui.viewmodels.appointments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.AppointmentsRepository;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.ListViewModel;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;

@HiltViewModel
public class AppointmentsCalendarViewModel extends BaseViewModel
    implements ListViewModel {
    @Getter
    private final AppointmentsRepository appointmentsRepository;
    @Getter
    private final CompaniesRepository companiesRepository;
    @Getter
    private final EmployeesRepository employeesRepository;

    @Getter
    private final LiveData<List<AppointmentDto>> appointments;
    @Getter
    private final LiveData<List<OrgUnitDto>> orgUnits;
    @Getter
    private final LiveData<List<EmployeeDto>> employees;
    @Getter
    private final LiveData<Object> refreshAppointmentsCompleted;

    @Getter @Setter
    private boolean refreshReady = true;
    @Getter @Setter
    private Integer selectedOrgUnit;
    @Getter @Setter
    private Integer selectedEmployee;
    @Getter @Setter
    private LocalDate currentlyShowedDate;
    @Getter @Setter
    private boolean orgUnitChanged = false;
    @Getter @Setter
    private boolean employeeChanged = false;
    @Getter
    private Boolean finished = null;
    @Getter
    private Boolean cancelled = null;
    public void setFinished(Boolean value) {
        if (value != null && value && cancelled != null && cancelled)
            cancelled = false;
        finished = value;
    }
    public void setCancelled(Boolean value) {
        if (value != null && value && finished != null && finished)
            finished = false;
        cancelled = value;
    }

    @Inject
    public AppointmentsCalendarViewModel(
            AppointmentsRepository appointmentsRepository,
            CompaniesRepository companiesRepository,
            EmployeesRepository employeesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.appointmentsRepository = appointmentsRepository;
        this.companiesRepository = companiesRepository;
        this.employeesRepository = employeesRepository;
        this.appointments = LiveDataReactiveStreams.fromPublisher(
                appointmentsRepository.getAppointments()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.orgUnits = LiveDataReactiveStreams.fromPublisher(
                companiesRepository.getOrgUnits()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.employees = LiveDataReactiveStreams.fromPublisher(
                employeesRepository.getEmployees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.refreshAppointmentsCompleted = LiveDataReactiveStreams.fromPublisher(
                this.appointmentsRepository
                        .getRefreshAppointmentsCompleted()
                        .toFlowable(BackpressureStrategy.BUFFER));
        this.selectedOrgUnit = null;
        this.currentlyShowedDate = LocalDate.now();
    }

    @Override
    public void refreshData() {
        this.appointmentsRepository.refreshAppointments();
    }
}
