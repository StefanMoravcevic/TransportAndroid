package com.programdoo.transport.ui.pages;

import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {
    /**
     *
     * @return ime fragmenta koje se koristi prilikom stavljanja na backstack
     */
    public abstract String TAG();
}
