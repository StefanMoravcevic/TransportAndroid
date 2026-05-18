package com.programdoo.transport.utils;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @param <T> tip podatka koji predstavlja event
 *     <p>
 *         wrapper klasa za LiveData za event-ove koji treba da se dese samo jednom, poput prikazivanja toast-a
 *         ili navigacije. najcesce se koristi u view modelu prilikom rukovanja odgovorom. <br>
 *         neophodna je jer prilikom configuration change-a dodje do ponovnog postavljanja vrednosti
 *         u LiveData i pokretanja observer-a postavljenih nad tim promenljivim. na primer, ako se
 *         postavi indikator da se prikaze toast prilikom rukovanja API odgovora, taj toast treba
 *         da se prikaze tacno jednom. ako se potom promeni orijentacija uredjaja, taj indikator bi se
 *         ponovo postavio i ponovo bi se prikazao isti toast. ova klasa to sprecava.<br><br>
 *         primeri stvari koje okidaju configuration change:
 *         <ul>
 *             <li>promena orijentacije</li>
 *             <li>promena velicine ekrana (foldable uredjaji, multi-window mode...)</li>
 *             <li>switch izmedju dark i light moda</li>
 *             <li>promena velicine fonta</li>
 *         </ul>
 *     </p>
 */
public class SingleLiveEvent<T> extends LiveData<T> {
    private final AtomicBoolean pending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void setValue(@Nullable T t) {
        pending.set(true);
        super.setValue(t);
    }

    @Override
    public void observe(LifecycleOwner owner, Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t);
                super.setValue(null);
            }
        });
    }
}
