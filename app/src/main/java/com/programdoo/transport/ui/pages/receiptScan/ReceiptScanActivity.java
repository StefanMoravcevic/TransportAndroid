package com.programdoo.transport.ui.pages.receiptScan;

import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.scannedpackages.ScanPackageFragment;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReceiptScanActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt_scan);

        NavigationUtil.navigate(this, R.id.fragmentFrame, new ReceiptScanFragment(), null);
    }
}
