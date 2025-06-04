package services;

import org.springframework.stereotype.Component;

public class MyService implements IMyService{
    @Override
    public void consume() {
        System.out.println("consume of IMyService implemented");
    }
}
