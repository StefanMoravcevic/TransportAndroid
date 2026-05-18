package com.programdoo.transport.ui.viewmodels.appointments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentDto;
import com.programdoo.transport.data.models.dtos.appointments.AppointmentRecurrencePatternDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.repositories.AppointmentsRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;

@HiltViewModel
public class AppointmentInfoViewModel extends BaseViewModel {
    @Getter
    private final AppointmentsRepository appointmentsRepository;
    @Getter
    private LiveData<AppointmentDto> appointment;
    @Getter
    private LiveData<AppointmentRecurrencePatternDto> recurrencePattern;

    @Inject
    public AppointmentInfoViewModel(
            AppointmentsRepository appointmentsRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.appointmentsRepository = appointmentsRepository;
        appointment = LiveDataReactiveStreams.fromPublisher(
                appointmentsRepository.getAppointment()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        recurrencePattern = LiveDataReactiveStreams.fromPublisher(
                appointmentsRepository.getRecurrence()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }
}
