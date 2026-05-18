package com.programdoo.transport.data.models.requests.memberships;
import com.programdoo.transport.data.models.enums.ShortcutContext;
import com.programdoo.transport.data.models.requests.ISearchParams;

public class SearchMembershipCardsParams implements ISearchParams {
    private ShortcutContext sourceId;
    private Integer filterId;
    private Integer currentId;
    private Integer id;
    private Integer typeId;
    private String typeCode;

    public SearchMembershipCardsParams() {
    }

    public SearchMembershipCardsParams( ShortcutContext sourceId, Integer filterId) {
        if (sourceId != null) {
            this.sourceId = sourceId;
            this.filterId = filterId;

            switch (sourceId) {
                case ByType:
                    if (filterId != null) {
                        this.typeId = filterId;
                    }
                    break;
            }
        }
    }

}
