package app.jhau.appopsmanager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import app.jhau.server.IAppOpsServer;
import app.jhau.server.provider.ServerProvider;
import app.jhau.server.util.Constants;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyShellFile();
        initView();
    }

    private void initView() {
        TextInputEditText textInputEditText = findViewById(R.id.et_shell_cmd);
        Button button = findViewById(R.id.btn_exec);
        button.setOnClickListener(v -> {
            String content = String.valueOf(textInputEditText.getText());
            Log.i(TAG, "button.setOnClickListener: ");
            ContentResolver resolver = getContentResolver();

            String execRet = null;
            IBinder binder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                Bundle bundle = resolver.call(ServerProvider.AUTHORITY_NAME, ServerProvider.GET_BINDER, "", null);
                binder = bundle.getBinder(Constants.SERVER_BINDER_KEY);
            } else {
                Cursor cursor = resolver.query(Uri.parse(ServerProvider.AUTHORITY_URI), null,null, null,null);
                binder = cursor.getExtras().getBinder(Constants.SERVER_BINDER_KEY);
                cursor.close();
            }
            IAppOpsServer server = IAppOpsServer.Stub.asInterface(binder);
            try {
                execRet = server.execCommand(content);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            TextView textView = findViewById(R.id.tv_shell_result);
            textView.setText(execRet);
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