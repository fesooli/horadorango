package br.com.fellipe.horadorango.model;

/**
 * Created by fellipe on 29/05/18.
 */

public class Payment {
    private PaymentForm paymentForm;
    private String deliveryAddress;

    public PaymentForm getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(PaymentForm paymentForm) {
        this.paymentForm = paymentForm;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
