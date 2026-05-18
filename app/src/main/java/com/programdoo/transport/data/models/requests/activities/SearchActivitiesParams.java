package com.programdoo.transport.data.models.requests.activities;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.util.Date;

public class SearchActivitiesParams implements ISearchParams {
    public Integer trainerId;
    public Integer traineeId;
    public Integer Id;
    public Boolean dateAsc;
    public Boolean dateDesc;
    public Integer activityTypeId;
    public Integer rating;
    public Boolean unrated;
    public Boolean rated;
    public Date currentDate;
    public Date dateTo;
    public Date dateFrom;

}
