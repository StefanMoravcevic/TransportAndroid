package com.programdoo.transport.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.scannedpackages.ScannedPackageDto;
import com.programdoo.transport.data.models.requests.ISearchParams;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.ui.pages.scannedpackages.PackageDetailsFragment;
import com.programdoo.transport.ui.pages.scannedpackages.ScanPackageFragment;
import com.programdoo.transport.utils.EntityToOptionMapper;
import com.programdoo.transport.utils.ExtendedFilter;

import java.util.ArrayList;
import java.util.List;

public class ScannedPackagesPagerAdapter extends FragmentStateAdapter {
    private List<Integer> packageIds; // Lista ID-jeva paketa

    public ScannedPackagesPagerAdapter(@NonNull Fragment fragment, List<Integer> ids) {
        super(fragment);
        this.packageIds = ids;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Kreiramo donji fragment i šaljemo mu ID paketa
        return PackageDetailsFragment.newInstance(packageIds.get(position));
    }

    @Override
    public int getItemCount() {
        return packageIds.size();
    }
}

