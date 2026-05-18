package com.programdoo.transport.utils;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;

import javax.annotation.Nullable;

public class NavigationUtil {
    /**
     * zameni source fragment dest fragmentom u kontejneru fragContainer i stavi
     * dest na backstack
     * ukoliko {bundle != null}, prenesi bundle.
     *
     * @param source fragment iz kog se izlazi
     * @param fragContainer fragment kontejner
     * @param dest instanca fragmenta u koji se ide
     * @param bundle podaci
     * <p>
     *
     * </p>
     */
    public static void navigate(
            @NonNull BaseFragment source,
            @IdRes int fragContainer,
            @NonNull BaseFragment dest,
            @Nullable Bundle bundle) {
        FragmentManager fm = source.getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (bundle != null) {
            dest.setArguments(bundle);
        }

        ft.replace(fragContainer, dest, dest.TAG());
        /* na backStack moze da se doda fragment transakcija. prilikom vracanja unazad, backstack
         * moze da se iskoristi da se uradi redo neke transakcije i vrati u fragment iz kog se
         * pocelo. dest.TAG() je ime fragmenta koje se postavlja na backstack radi identifikacije
         * transakcije. moze da se prosledi null, ali treba uvek prosledjivati ime kako bismo mogli
         * da pronadjemo tacnu transakciju koja nam je potrebna. stoga je u svakom fragmentu potrebno
         * override-ovati ovaj metod, koji je deklarisan u BaseFragment. */
        ft.addToBackStack(dest.TAG());
        ft.commit();
    }

    /**
     * koristi se kad iz activity-ja treba da se predje u neki fragment.
     *
     * @param source activity iz kog se izlazi
     * @param fragContainer fragment kontejner
     * @param dest instanca fragmenta u koji se ide
     * @param bundle podaci
     */
    public static void navigate(
            @NonNull BaseActivity source,
            @IdRes int fragContainer,
            @NonNull BaseFragment dest,
            @Nullable Bundle bundle) {
        FragmentManager fm = source.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (bundle != null) {
            dest.setArguments(bundle);
        }

        ft.replace(fragContainer, dest, dest.TAG());
        /* na backStack moze da se doda fragment transakcija. prilikom vracanja unazad, backstack
         * moze da se iskoristi da se uradi redo neke transakcije i vrati u fragment iz kog se
         * pocelo. dest.TAG() je ime fragmenta koje se postavlja na backstack radi identifikacije
         * transakcije. moze da se prosledi null, ali treba uvek prosledjivati ime kako bismo mogli
         * da pronadjemo tacnu transakciju koja nam je potrebna. stoga je u svakom fragmentu potrebno
         * override-ovati ovaj metod, koji je deklarisan u BaseFragment. */
        ft.addToBackStack(dest.TAG());
        ft.commit();
    }

    /**
     * vraca u dest fragment. usput brise sve fragmente iz backstack-a, ne racunajuci dest.
     *
     * @param source fragment iz kog izlazimo
     * @param fragContainer kontejner za fragmente
     * @param destTag ime ciljnog fragmenta
     */
    public static void navigateBackTo(
            Fragment source,
            @IdRes int fragContainer,
            @NonNull String destTag) {
        FragmentManager fm = source.getParentFragmentManager();
        /* findFragmentByTag u sustini moze da vrati null. namenjeno je da se za svaku
         * navigaciju koristi funkcija iznad koja uvek stavlja fragment na back stack.
         * takodje, zbog toga svaki fragment mora da ima tag. ako se ovo prati, ovaj
         * metod nece vratiti null */
        Fragment dest = fm.findFragmentByTag(destTag);
        fm.popBackStack(destTag, 0);
        fm.beginTransaction().show(dest).commit();
    }
}
