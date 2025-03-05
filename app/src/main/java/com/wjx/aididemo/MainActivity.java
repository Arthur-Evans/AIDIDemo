package com.wjx.aididemo;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ICalculator calculatorService;
    private boolean isBound = false;

    private Button btnBind, btnCalculate;
    private TextView tvResult;

    private Context context;

    private final ServiceConnection connection =new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            calculatorService = ICalculator.Stub.asInterface(service);
            isBound = true;
            btnCalculate.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            calculatorService =null;
            isBound = false;
            btnCalculate.setEnabled(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBind = findViewById(R.id.btnBind);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        btnBind.setOnClickListener(v -> bindService());
        btnCalculate.setOnClickListener(v -> performCalculations());

    }


    private void bindService() {
        Intent intent = new Intent(this, CalculatorService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void performCalculations() {
        System.out.println("开始");
        if (!isBound){
            Toast.makeText(this, "绑定失败: ", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String result = "计算结果：\n" +
                    "10 + 5 = " + calculatorService.add(10, 5) + "\n" +
                    "10 - 5 = " + calculatorService.subtract(10, 5) + "\n" +
                    "10 * 5 = " + calculatorService.multiply(10, 5) + "\n" +
                    "10 / 5 = " + calculatorService.divide(10, 5);

            tvResult.setText(result);
        } catch (RemoteException e) {
            Toast.makeText(this, "远程调用失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            Toast.makeText(this, "计算错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

}