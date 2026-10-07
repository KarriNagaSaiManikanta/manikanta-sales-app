package com.example.manikantasales.dto;


import com.example.manikantasales.enums.PaymentMethod;



public class OrderRequest {



    private Long addressId;



    private Double shippingCharge;



    private PaymentMethod paymentMethod;







    public OrderRequest(){



    }







    public Long getAddressId(){


        return addressId;

    }



    public void setAddressId(Long addressId){


        this.addressId = addressId;

    }







    public Double getShippingCharge(){


        return shippingCharge;

    }



    public void setShippingCharge(Double shippingCharge){


        this.shippingCharge = shippingCharge;

    }







    public PaymentMethod getPaymentMethod(){


        return paymentMethod;

    }



    public void setPaymentMethod(PaymentMethod paymentMethod){


        this.paymentMethod = paymentMethod;

    }


}