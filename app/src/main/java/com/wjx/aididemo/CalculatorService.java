package com.wjx.aididemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class CalculatorService extends Service {
    public CalculatorService() {
    }

    private final ICalculator.Stub binder = new ICalculator.Stub() {
        @Override
        public int add(int a, int b) {
            return a + b;
        }

        @Override
        public int subtract(int a, int b) {
            return a - b;
        }

        @Override
        public int multiply(int a, int b) {
            return a * b;
        }

        @Override
        public int divide(int a, int b) throws android.os.RemoteException {
            if (b == 0) throw new ArithmeticException("Cannot divide by zero");
            return a / b;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("返回bind");
        return binder;
    }
}
