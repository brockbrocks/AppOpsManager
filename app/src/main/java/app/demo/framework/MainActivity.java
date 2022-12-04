package app.demo.framework;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyShellFile();
        initView();
        test();
    }

    private void test() {

    }


    private void initView() {
        TextInputEditText textInputEditText = findViewById(R.id.et_shell_cmd);
        Button button = findViewById(R.id.btn_exec);
        button.setOnClickListener(v -> {
            String content = String.valueOf(textInputEditText.getText());
            Log.i(TAG, "button.setOnClickListener: ");
            Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
        });
    }

    private void copyShellFile() {
        final String fileName = "starter.sh";
        File file = new File(getExternalCacheDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getResources().getAssets().open(fileName);
            fos = new FileOutputStream(new File(getExternalCacheDir(), fileName));
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff, 0, buff.length)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}