package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MasterDataService
{
    @GET("masterData/options/{tableName}/{descriptionExpression}")
    Observable<ResponseModelList<MasterDataDto>> getSelectOptionsByTable(
            @Path("tableName") String tableName,
            @Path("descriptionExpression") String descriptionExpression
    );

    @GET("masterData/filtered-options/{tableName}/{keyColumnName}/{columnValue}/{descriptionColumnName}")
    Observable<ResponseModelList<MasterDataDto>> getFilteredSelectOptionsByTable(
            @Path("tableName") String tableName,
            @Path("keyColumnName") String keyColumnName,
            @Path("columnValue") int columnValue,
            @Path("descriptionColumnName") String descriptionColumnName
    );

}
