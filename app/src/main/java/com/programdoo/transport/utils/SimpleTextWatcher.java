package com.programdoo.transport.utils;

import android.text.TextWatcher;

/**
 * jednostavna implementacija TextWatchera koja reaguje samo nakon sto se ispise tekst.
 * preostaje da se implementira samo afterTextChanged.
 * ukoliko je potrebno obezbediti delay period pre nego sto se ova funkcija pokrene, potrebno
 * je napraviti debouncing. primer se moze videti u ui.views.SelectDialog konstruktoru.
 */
public abstract class SimpleTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
