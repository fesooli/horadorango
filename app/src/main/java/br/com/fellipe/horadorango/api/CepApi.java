package br.com.fellipe.horadorango.api;

import br.com.fellipe.horadorango.dao.model.Address;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fellipe on 01/06/18.
 */

public interface CepApi {

    @GET("json")
    Call<Address> getCep();
}
