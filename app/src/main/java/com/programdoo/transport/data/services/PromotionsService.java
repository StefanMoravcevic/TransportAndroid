package com.programdoo.transport.data.services;

import com.programdoo.transport.data.models.dtos.promotions.PromotionDto;
import com.programdoo.transport.data.models.requests.promotions.SearchPromotionsParams;
import com.programdoo.transport.data.models.responses.ResponseModelList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PromotionsService {
    @POST("promotions/search")
    Observable<ResponseModelList<PromotionDto>> searchPromotions(@Body SearchPromotionsParams searchParams);
}
