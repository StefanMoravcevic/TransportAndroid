package com.programdoo.transport.data.models.requests.employees;

import com.programdoo.transport.data.models.requests.ISearchParams;

import java.util.ArrayList;
import java.util.List;

public class SearchEmployeesParams implements ISearchParams {
    public Integer id;
    public String keyword;
    public Boolean active;
    public Integer companyId;
    public Integer orgUnitId;
    public List<Integer> jobTypes;
    public Integer jobTypeId;

    public SearchEmployeesParams() {
        jobTypes = new ArrayList<>();
    }
}
