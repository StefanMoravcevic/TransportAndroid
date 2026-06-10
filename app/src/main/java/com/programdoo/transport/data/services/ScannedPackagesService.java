package com.programdoo.transport.data.services;


import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.dtos.scannedpackages.ScannedPackageDto;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.data.models.responses.ResponseModel;
import com.programdoo.transport.data.models.responses.ResponseModelList;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ScannedPackagesService {
    @POST("scannedPackages")
    Observable<ResponseModel<Integer>> SaveScannedPackages(@Body SaveScannedPackagesRequestModel token);

    @POST("scannedPackages/search")
    Observable<ResponseModelList<ScannedPackageDto>> SearchScannedPackages(@Body SearchScannedPackagesParams searchParams);
}
