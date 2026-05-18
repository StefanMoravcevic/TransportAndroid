package com.programdoo.transport.data.models.requests.companies;

import com.programdoo.transport.data.models.requests.ISearchParams;

public class SearchOrgUnitsParams implements ISearchParams {
    public Integer id;
    public Integer parentOrgUnitId;
    public Integer companyId;
    public Integer employeeId;
    public Integer traineeId;
}
