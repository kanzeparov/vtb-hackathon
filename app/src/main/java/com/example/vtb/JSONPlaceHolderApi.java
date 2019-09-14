package com.example.vtb;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

interface JSONPlaceHolderApi {
    @POST("/api/v1/session")
    Call<SessionId> postUser(@Body Session user);

    @POST("/api/v1/invoice")
    Call<Invoice> invoice(@HeaderMap Map<String, String> headers, @Body InvoiceReq user);

    @GET("/api/v1/invoice/810/{bill}/{pay}")
    Call<Status> getDataFromService(
            @Path("bill") String bill,@Path("pay") String pay
    );

    @GET("/api/v1/transaction/810/balance/{bill}")
    Call<Balance> getBalance(@HeaderMap Map<String, String> headers,
            @Path("bill") String bill
    );
}
