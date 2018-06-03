package br.com.fellipe.horadorango.api;

import java.util.List;

import br.com.fellipe.horadorango.model.Payment;
import br.com.fellipe.horadorango.model.Place;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fellipe on 26/05/18.
 */

public interface PlacesApi {

    @GET("place/")
    Call<List<Place>> listPlaces();

    @GET("place")
    Call<Place> getPlace(@Query("placeId") Integer placeId);

    @GET("v2/5b0e06153200004f00c198a8")
    Call<Payment> listPaymentAndDeliveryAddress();
}
