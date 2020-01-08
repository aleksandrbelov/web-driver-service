package com.detectify.codingchallenge.webdriverservice.listener;

public interface Listener<T> {

    void listen(String topic, T data);

}
