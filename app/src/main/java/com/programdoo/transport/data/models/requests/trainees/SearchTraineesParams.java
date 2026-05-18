package com.programdoo.transport.data.models.requests.trainees;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.util.Date;
import java.util.List;

public class SearchTraineesParams implements ISearchParams {
    public Integer pageNumber;
    public Integer pageSize;
    public Integer trainerId;
    public Integer orgUnitId;
    public Integer Id;
    public Boolean active;
    public Integer promotionId;
    /** ovo mozda mora da bude obican niz, int[], jos nije testirano */
    public List<Integer> promotionIds;
    public String phoneNumber;
    public Date ActiveFrom;
    public Date ActiveTo;
    public Date inactiveFrom;
    public Date inactiveTo;
    public Date validFrom;
    public Date validTo;
    public Date invalidFrom;
    public Date invalidTo;
    public Date startFrom;
    public Date startTo;
}
