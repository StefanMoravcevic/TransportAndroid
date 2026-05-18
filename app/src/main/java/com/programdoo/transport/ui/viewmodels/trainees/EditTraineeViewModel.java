package com.programdoo.transport.ui.viewmodels.trainees;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.programdoo.transport.R;
import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelBase;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import com.programdoo.transport.data.repositories.CompaniesRepository;
import com.programdoo.transport.data.repositories.EmployeesRepository;
import com.programdoo.transport.data.repositories.MasterDataRepository;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.PromotionsRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.data.repositories.TraineesRepository;
import com.programdoo.transport.data.models.dtos.trainees.SaveTraineeRequestModel;
import com.programdoo.transport.databinding.FragmentEditTraineeAdditionalInfoBinding;
import com.programdoo.transport.databinding.FragmentEditTraineeBasicInfoBinding;
import com.programdoo.transport.databinding.FragmentEditTraineeFitnessInfoBinding;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.StringUtil;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;
import com.programdoo.transport.ui.viewmodels.EditViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *     view model za edit trainee fragmente. <br>
 * </p>
 */
@HiltViewModel
public class EditTraineeViewModel extends BaseViewModel
    implements EditViewModel {
    @Getter
    private final LiveData<TraineeDto> trainee;
    @Getter @Setter
    private SaveTraineeRequestModel traineeDraft = new SaveTraineeRequestModel();
    @Getter
    private final LiveData<List<PromotionDto>> promotions;
    @Getter
    private final LiveData<List<EmployeeDto>> employees;
    @Getter
    private final LiveData<List<OrgUnitDto>> orgUnits;
    @Getter
    private final LiveData<List<MasterDataDto>> limitations;
    @Getter
    private final LiveData<List<MasterDataDto>> goals;
    @Getter
    private final LiveData<List<MasterDataDto>> foundUsMethods;
    @Getter
    private final MutableLiveData<Integer> saveResponse;
    @Getter
    private final TraineesRepository traineesRepository;
    @Getter
    private final PromotionsRepository promotionsRepository;
    @Getter
    private final EmployeesRepository employeesRepository;
    @Getter
    private final CompaniesRepository companiesRepository;
    @Getter
    private final MasterDataRepository masterDataRepository;
    @Getter @Setter
    private boolean isEditMode;
    @Getter @Setter
    private String returnDestinationTag = Constants.FRAG_TRAINEES_LIST;
    @Getter
    private boolean isAppointmentShortcut;
    @Getter @Setter
    private boolean isTrialForm = false;

    @Inject
    public EditTraineeViewModel(
            TraineesRepository traineesRepository,
            PromotionsRepository promotionsRepository,
            EmployeesRepository employeesRepository,
            CompaniesRepository companiesRepository,
            MasterDataRepository masterDataRepository,
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvents) {
        super(preferences, session, authEvents);
        this.traineesRepository = traineesRepository;
        this.promotionsRepository = promotionsRepository;
        this.employeesRepository = employeesRepository;
        this.companiesRepository = companiesRepository;
        this.masterDataRepository = masterDataRepository;

        this.saveResponse = new MutableLiveData<>();
        this.limitations = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("Limitations", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.goals = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("Goals", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.foundUsMethods = LiveDataReactiveStreams.fromPublisher(
                masterDataRepository.GetSelectOptionsByTable("FoundUsMethods", "Name")
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
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
        this.orgUnits = LiveDataReactiveStreams.fromPublisher(
                companiesRepository.getOrgUnits()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModelList::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
        this.trainee = LiveDataReactiveStreams.fromPublisher(
                traineesRepository.getTrainee()
                        .filter(ResponseModelBase::isValid)
                        .map(ResponseModel::getPayload)
                        .toFlowable(BackpressureStrategy.LATEST));
    }

    public void setAppointmentShortcut(boolean value) {
        isAppointmentShortcut = value;
        if (isAppointmentShortcut) {
            returnDestinationTag = Constants.FRAG_EDIT_APPOINTMENT;
        }
    }

    public void saveTrialTrainee(SaveTraineeRequestModel saveData) {
        handleSingle(traineesRepository.saveTrialTrainee(saveData),
                traineeId -> {
                        if (isAppointmentShortcut) {
                            saveResponse.setValue(traineeId);
                        }
                        clearEditData();
                        toastEvent.setValue(R.string.msg_saved);
                        navigationEvent.setValue(1);
                },
                error -> {
                    Log.d("error", error.getMessage());
                    toastEvent.setValue(R.string.msg_error_api);
                });
    }
    public void saveTrainee(SaveTraineeRequestModel saveData) {
        handleSingle(traineesRepository.saveTrainee(saveData),
                traineeId -> {
                        clearEditData();
                        toastEvent.setValue(R.string.msg_saved);
                        navigationEvent.setValue(1);
                },
                error -> {
                    toastEvent.setValue(R.string.msg_error_api);
                    Log.d("error", error.getMessage());
                });
    }
    /**
     * popunjava trainee objekat u view modelu podacima iz bindinga. koristi se
     * kad treba apply-ovati podatke prilikom prelaska na narednu stranicu ili
     * pred slanje save zahteva na api.
     * ova metoda se koristi samo na AdditionalInfo edit stranici.
     */
    public void applyBasicInfoFromBinding(FragmentEditTraineeBasicInfoBinding binding) {
        if (!StringUtil.isNullOrEmpty(binding.ifFirstName.getText()))
            traineeDraft.setName(binding.ifFirstName.getText());
        if (!StringUtil.isNullOrEmpty(binding.ifSurname.getText()))
            traineeDraft.setSurname(binding.ifSurname.getText());
        if (!StringUtil.isNullOrEmpty(binding.ifPhoneNumber.getText()))
            traineeDraft.setPhoneNumber(binding.ifPhoneNumber.getText());
        if (!StringUtil.isNullOrEmpty(binding.ifNickname.getText()))
            traineeDraft.setNickname(binding.ifNickname.getText());
        traineeDraft.setInTrialPeriod(binding.sTrialPeriod.isChecked());
        if (binding.selPromotion.isSelected())
            traineeDraft.setPromotionId(binding.selPromotion.getSelectedId());
    }
    public void applyAdditionalInfoFromBinding(FragmentEditTraineeAdditionalInfoBinding binding) {
        if (!StringUtil.isNullOrEmpty(binding.ifBirthdate.getText())) {
            traineeDraft.setBirthdate(DateUtil.parse(binding.ifBirthdate.getText()));
        }
        if (!StringUtil.isNullOrEmpty(binding.ifEmail.getText().toString())) {
            traineeDraft.setEmail(binding.ifEmail.getText());
        }
        traineeDraft.setReceiveEmailNotifications(binding.sReceiveEmailNotifications.isChecked());
        traineeDraft.setActive(binding.sActive.isChecked());
        if (binding.selCurrentTrainer.isSelected()) {
            traineeDraft.setCurrentTrainerId(binding.selCurrentTrainer.getSelectedId());
        }
        if (binding.selOrgUnit.isSelected()) {
            traineeDraft.setOrgUnitId(binding.selOrgUnit.getSelectedId());
        }
        if (binding.selGender.isSelected()) {
            traineeDraft.setGenderId(binding.selGender.getSelectedId());
        }
        if (binding.selLanguage.isSelected()) {
            traineeDraft.setUserCulture(binding.selLanguage.getSelectedDescription());
        }
        if (binding.mselTraineeOrgUnits.isSelected()) {
            traineeDraft.setTraineeOrgUnitsIds(binding.mselTraineeOrgUnits.getSelectedIds());
        }
    }
    /**
     * popunjava trainee objekat u view modelu podacima iz bindinga. koristi se
     * kad treba apply-ovati podatke prilikom prelaska na narednu stranicu ili
     * pred slanje save zahteva na api.
     * ova metoda se koristi samo na FitnessInfo edit stranici.
     * @param binding
     */
    public void applyFitnessInfoFromBinding(FragmentEditTraineeFitnessInfoBinding binding) {
        if (binding.mselLimitations.isSelected()) {
            traineeDraft.setLimitationsIds(binding.mselLimitations.getSelectedIds());
        }
        if (binding.mselGoals.isSelected()) {
            traineeDraft.setGoalsIds(binding.mselGoals.getSelectedIds());
        }
        if (binding.selFoundUsMethod.isSelected()) {
            traineeDraft.setFoundUsMethodId(binding.selFoundUsMethod.getSelectedId());
        }
        if (!StringUtil.isNullOrEmpty(binding.ifPreviousFitnessExperience.getText())) {
            traineeDraft.setPreviousFitnessExperience(binding.ifPreviousFitnessExperience.getText());
        }
    }

    @Override
    public void clearEditData() {
        traineeDraft = new SaveTraineeRequestModel();
    }
}
