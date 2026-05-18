package com.programdoo.transport.ui.viewmodels.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.activities.ActivityDto;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;

@HiltViewModel
public class ActivitiesListViewModel extends BaseViewModel {
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
    private TraineesRepository traineesRepository;

    @Getter
    private final LiveData<List<ActivityDto>> activities;

    @Inject
    public ActivitiesListViewModel(
            TraineesRepository traineesRepository,
            CompaniesRepository companiesRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.traineesRepository = traineesRepository;
        activities = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getActivities()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }

    public void deleteActivity(int id, int userId) {
        handleCompletable(traineesRepository.deleteActivity(id, userId),
                () -> {
                    toastEvent.setValue(R.string.msg_delete_success);
                }, error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("error", error.getMessage());
                });
    }

}
