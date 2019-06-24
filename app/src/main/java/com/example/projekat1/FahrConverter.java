package com.example.projekat1;

public class FahrConverter {

    static{
        System.loadLibrary("ConversionLib");
    }

    public native int CelsiusToFahrenheit(int temp);
    public native int FahrenheitToCelsius(int temp);

}
