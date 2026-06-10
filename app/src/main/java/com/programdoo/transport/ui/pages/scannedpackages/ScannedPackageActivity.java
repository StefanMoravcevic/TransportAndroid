package com.programdoo.transport.ui.pages.scannedpackages;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.adapters.ScannedPackagesRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScannedPackageActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanned_package);

        NavigationUtil.navigate(this, R.id.fragmentFrame, new ScanPackageFragment(), null);
    }

 }
