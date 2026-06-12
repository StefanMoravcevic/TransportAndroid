package com.programdoo.transport.data.models.requests.travelOrders;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SearchTravelOrdersParams implements ISearchParams {

    public Integer id;

    public Integer employeeId;
    public Integer travelOrderStatusId;
    public LocalDateTime dateFrom;
    public LocalDateTime dateTo;
}
