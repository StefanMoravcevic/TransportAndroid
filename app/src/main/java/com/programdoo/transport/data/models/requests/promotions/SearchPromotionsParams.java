package com.programdoo.transport.data.models.requests.promotions;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.util.Date;

public class SearchPromotionsParams implements ISearchParams {
    public Integer id;
    public Date dateFrom;
    public Date dateTo;
}
