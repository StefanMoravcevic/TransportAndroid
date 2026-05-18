package com.programdoo.transport.ui.viewmodels.trainees;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.ListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;

@HiltViewModel
public class TraineesListViewModel extends BaseViewModel
    implements ListViewModel {
    /**
     * MutableLiveData je mehanizam preko kog view model moze da obavesti fragment
     * ili activity o izmeni podataka.<br>
     * u fragment-u ili activity-ju potrebno je da se registruje observer nad ovom promenljivom.
     * tad se postavlja i lambda funkcija koja se pokrece svaki put kad se "observe"-uje promena
     * nad ovom promenljivom i kojoj se prosledjuju ti podaci. kad se ovoj promenljivi postavi vrednost
     * sa <b>postValue</b>, okine se event koji registrovan observer primeti i pozive funkciju.
     * na ovaj nacin se podaci sa API-ja salju fragmentu.
     */
    @Getter
    private final LiveData<List<TraineeDto>> trainees;
    @Getter
    private final LiveData<List<OrgUnitDto>> orgUnits;
    @Getter
    private final LiveData<List<EmployeeDto>> employees;
    @Getter
    private final LiveData<Object> refreshTraineesCompleted;

    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final CompaniesRepository companiesRepository;
    @Getter
    private final EmployeesRepository employeesRepository;

    @Getter @Setter
    private Integer currentOrgUnit = null;
    @Getter @Setter
    private Integer currentEmployee = null;

    @Inject
    public TraineesListViewModel(
            TraineesRepository traineesRepository,
            CompaniesRepository companiesRepository,
            EmployeesRepository employeesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.traineesRepository = traineesRepository;
        this.companiesRepository = companiesRepository;
        this.employeesRepository = employeesRepository;
        trainees = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getTrainees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        orgUnits = LiveDataReactiveStreams.fromPublisher(
                companiesRepository.getOrgUnits()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        employees = LiveDataReactiveStreams.fromPublisher(
                employeesRepository.getEmployees()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        refreshTraineesCompleted = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getRefreshTraineesCompleted()
                        .toFlowable(BackpressureStrategy.BUFFER));
    }

    public void deleteTrainee(int id, int userId) {
        handleCompletable(traineesRepository.deleteTrainee(id, userId),
                () -> toastEvent.setValue(R.string.msg_delete_success),
                error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("API delete error", error.getMessage());
                });
    }

    @Override
    public void refreshData() {
        this.traineesRepository.refreshTrainees();
    }
}
