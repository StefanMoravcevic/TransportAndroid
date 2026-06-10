package com.programdoo.transport.ui.pages.scannedpackages;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.programdoo.transport.ui.pages.BaseFragment;


public class PackageDetailsFragment extends BaseFragment {
    private static final String ARG_PAKET_ID = "package_id";

    public static PackageDetailsFragment newInstance(int id) {
        PackageDetailsFragment fragment = new PackageDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAKET_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int paketId = getArguments().getInt(ARG_PAKET_ID);
        // Ovde sada koristiš paketId da učitaš podatke iz baze/API-ja
        // TextView tvSifra = view.findViewById(...);
        // tvSifra.setText("Sifra paketa: " + paketId);
    }
    @Override
    public String TAG() {
        return "PackageDetailsFragment";
    }
}