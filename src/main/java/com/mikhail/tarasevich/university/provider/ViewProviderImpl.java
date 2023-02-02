package com.mikhail.tarasevich.university.provider;

import com.mikhail.tarasevich.university.provider.ViewProvider;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ViewProviderImpl implements ViewProvider {

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    @Override
    public int readInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

}
