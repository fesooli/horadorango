package br.com.fellipe.horadorango.model;

import java.util.List;

/**
 * Created by fellipe on 29/05/18.
 */

public class PaymentForm {
    private List<String> cardBrand;

    public List<String> getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(List<String> cardBrand) {
        this.cardBrand = cardBrand;
    }
}
