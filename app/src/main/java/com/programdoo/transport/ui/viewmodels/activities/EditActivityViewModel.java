package com.programdoo.transport.ui.viewmodels.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.PromotionsRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;

/**
 * <p>
 *     view model za edit trainee fragmente. <br>
 *     <b><i>bitno</i></b>: edit trainee forma je multi-fragment jer je velika. zbog toga se
 *     u njima view model deli (shared view model). to znaci da ako se udje u edit ili novog vezbaca,
 *     potom izadje iz forme nazad u listu vezbaca i onda udje u pravljenje novog vezbaca, on ce da
 *     bude popunjen podacima iz prethodne forme. zbog toga je potrebno da se pozove <b>viewModel.clear()</b>
 *     kad god se izlazi iz forme (na cancel, na poslednji backpress i nakon save).
 * </p>
 */
@HiltViewModel
public class EditActivityViewModel extends BaseViewModel {
    @Getter
    private LiveData<TraineeDto> trainee;
    @Getter
    private LiveData<List<PromotionDto>> promotions;
    @Getter
    private LiveData<List<EmployeeDto>> employees;
    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final PromotionsRepository promotionsRepository;
    @Getter
    private final EmployeesRepository employeesRepository;

    @Inject
    public EditActivityViewModel(
            TraineesRepository traineesRepository,
            PromotionsRepository promotionsRepository,
            EmployeesRepository employeesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.traineesRepository = traineesRepository;
        this.promotionsRepository = promotionsRepository;
        this.employeesRepository = employeesRepository;
        this.promotions = LiveDataReactiveStreams.fromPublisher(
                promotionsRepository.getPromotions()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.employees = LiveDataReactiveStreams.fromPublisher(
                employeesRepository.getEmployees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.trainee = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getTrainee()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }
}
