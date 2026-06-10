package com.programdoo.transport.data.models.requests.scannedPackages;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;
@Data
public class SearchScannedPackagesParams implements ISearchParams {
    public Integer id;
    public String PackageNo;

}
