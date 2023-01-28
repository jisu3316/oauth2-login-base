package com.example.oauth2loginbase.common.converters;

public interface ProviderUserConverter<T, R> {

    R converter(T t);
}
