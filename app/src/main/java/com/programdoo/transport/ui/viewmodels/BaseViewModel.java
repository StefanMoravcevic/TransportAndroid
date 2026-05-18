package com.programdoo.transport.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.programdoo.transport.data.eventbus.AuthEventBus;
import com.programdoo.transport.data.repositories.PreferencesRepository;
import com.programdoo.transport.data.repositories.SessionRepository;
import com.programdoo.transport.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import lombok.Getter;

/**
 * base view model sadrzi logiku koju koriste svi view modeli.
 * generalno, view model sluzi za cuvanje i prenos podataka u fragmentu. on inject-uje
 * repository-je koji su potrebni fragmentu i predaje mu pristigle podatke sa API-ja.
 *
 * view model ima lifecycle, isto kao activity i fragment. koliko on traje u odnosu na fragment
 * i activity zavisi od toga ko mu je owner, sto se postavlja prilikom dovlacenja view modela:
 * viewModel = new ViewModelProvider(owner)....<br>
 * ako je owner view modela activity, view model ostaje
 * ziv dokle god je activity ziv. <br>
 * ako je owner fragment, onda samo dok je fragment ziv, s tim
 * sto view model prezivljava configuration change, dok se fragment pravi ispocetka. <br>
 * configuration change nastaje priliom promene orijentacije na primer. drugim recima,
 * kad god je potrebno da se reorganizuje ekran. poseban slucaj vezan
 * za configuration change moze da se nadje u SingleLiveEvent klasi.<br>
 * primeri stvari koje okidaju configuration change:
 * <ul>
 *     <li>promena orijentacije</li>
 *     <li>promena velicine ekrana (foldable uredjaji, multi-window mode...)</li>
 *     <li>switch izmedju dark i light moda</li>
 *     <li>promena velicine fonta</li>
 *     <li>...</li>
 * </ul>
 * <br><br>
 * <ul>
 *     <li> ako si u fragment-u i zelis da lifecycle view modela bude vezan za fragment, owner := this</li>
 *     <li> ako si u fragment-u i zelis da lifecycle view modela bude vezan za parent activity,
 *          owner := requireActivity(). ovo je shared view model - svi fragmenti pod tim activity-jem
 *          ce dovlaciti istu instancu tog view modela.
 *     </li>
 *     <li> ako si u activity-ju, view model moze da bude vezan samo za taj activity, pa se
 *          prosledjuje this. ako kasnije u fragmentu zelis da dovuces isti taj view model,
 *          owner := requireActivity()
 *     </li>
 * </ul>
 *
 * activity i fragment imaju po ViewModelStore gde cuvaju view modele. odatle se dovlace.
 */
@HiltViewModel
public class BaseViewModel extends ViewModel {
    /**
     * preferences repository daje pristup memoriji u kojoj mogu da se cuvaju podesavanja
     * za aplikaciju. u njima se npr cuva API token, mogu da se cuvaju podaci o ulogovanom
     * vezbacu, mozda jezik aplikacije i slicno.
     */
    @Getter
    protected final PreferencesRepository preferences;
    /**
     * composite disposable sluzi da drzi Observable. on automatski koordinise da thread-ovanje
     * i objedinjuje dispose-ovanje tih objekata kad vise nisu potrebni unutar <b>onCleared</b>
     * metode.
     */
    @Getter
    protected final SessionRepository session;
    protected final CompositeDisposable disposables;
    /**
     * pogledati za vise detalja. <br>
     * toastEvent dobija string resource id koji ce da prikaze u toast-u. potrebno je registrovati
     * toastEvent.observe() u fragmentu koji ce da prikaze toast
     */
    @Getter
    protected final SingleLiveEvent<Integer> toastEvent;
    /**
     * isto kao za toastEvent, samo prelazi u drugi fragment. dovoljno je da se za vrednost postavi
     * neki nasumican int.
     */
    @Getter
    protected final SingleLiveEvent<Integer> navigationEvent;
    @Getter
    protected final SingleLiveEvent<Integer> navigateToLoginEvent;

    @Inject
    public BaseViewModel(
            PreferencesRepository preferences,
            SessionRepository session,
            AuthEventBus authEvent) {
        super();
        this.disposables = new CompositeDisposable();
        this.toastEvent = new SingleLiveEvent<>();
        this.navigationEvent = new SingleLiveEvent<>();
        this.navigateToLoginEvent = new SingleLiveEvent<>();
        this.preferences = preferences;
        this.session = session;
        disposables.add(authEvent.events()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event == AuthEventBus.AuthEvent.UNAUTHORIZED)
                        navigateToLoginEvent.setValue(1);
                }));
    }

    /**
     *
     * @param repositoryCall repository metod koji treba obraditi
     * @param onComplete funkcija koja obraduje uspeh
     * @param onError funkcija koja obradjuje neuspesnu komunikaciju sa API-jem
     * @param <T> tip odgovora, obicno neka ResponseModel klasa
     */
    protected <T> void handleCompletable(
            Completable repositoryCall,
            Action onComplete,
            Consumer<? super Throwable> onError) {
        disposables.add(
                repositoryCall
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onComplete, onError));
    }

    protected void handleSingle(
            Single<Integer> repositoryCall,
            Consumer<Integer> onSuccess,
            Consumer<? super Throwable> onError) {
        disposables.add(
                repositoryCall
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess, onError));
    }
    protected <T> void consumeApi(
            Observable<T> repositoryCall,
            Consumer<? super T> onSuccess,
            Consumer<? super Throwable> onError) {
                disposables.add(repositoryCall
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess, onError));
    }
//    protected <T> LiveData<T> getLiveDataFromPublisher(
//            Observable<T> observable) {
//        return LiveDataReactiveStreams.fromPublisher(
//                observable
//                        .filter(ResponseModelBase::isValid)
//                        .map(ResponseModel::getPayload)
//                        .toFlowable(BackpressureStrategy.LATEST));
//    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
