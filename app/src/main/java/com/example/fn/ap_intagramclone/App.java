package com.example.fn.ap_intagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("VkER5TzLZ8Rug25OMwEU1azhICq6urL4ihbIHfTf")
                // if defined
                .clientKey("fuF8zUDAUyUIH73Kz7QIJf6oB5WxdkKitfVkadu0")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
