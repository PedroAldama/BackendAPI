package com.backendapi.exceptions;

public class BagException {
    private BagException(){
        //Esta clase es solo para excepciones de Bag
    }

    public static class BagNotFoundException extends RuntimeException {
        public BagNotFoundException(){
            super("Bag not found");
        }
    }
}
