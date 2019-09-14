package com.github.richardjwild.blather;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.application.ApplicationBuilder;

public class Blather {

    public static void main(String[] args) {
        Application application = ApplicationBuilder.anApplication().build();
        application.run();
    }
}
