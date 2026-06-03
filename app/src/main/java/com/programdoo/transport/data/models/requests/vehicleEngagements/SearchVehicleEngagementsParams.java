package com.programdoo.transport.data.models.requests.vehicleEngagements;

import com.programdoo.transport.data.models.requests.ISearchParams;

import lombok.Data;


@Data
public class SearchVehicleEngagementsParams implements ISearchParams {

    public Integer id;

    public Integer employeeId;

}
