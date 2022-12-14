package app.jhau.appopsmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import app.jhau.server.IAppOpsServer;
import app.jhau.server.provider.ServerProvider;
import app.jhau.server.util.ServerStarterUtil;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            copyShellFile();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        initView();
        test();
    }

    private void test() {
        Log.i(TAG, "ServerProvider: " + ServerStarterUtil.getCommand(this));
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
                binder = bundle.getBinder(ServerProvider.SERVER_BINDER_KEY);
            } else {
                Cursor cursor = resolver.query(Uri.parse(ServerProvider.AUTHORITY_URI), null, null, null, null);
                binder = cursor.getExtras().getBinder(ServerProvider.SERVER_BINDER_KEY);
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

        Button button1 = findViewById(R.id.btn_copy_cmd);
        button1.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("cmd", ServerStarterUtil.getCommand(MainActivity.this));
            clipboardManager.setPrimaryClip(data);
            Toast.makeText(MainActivity.this, "Copy success", Toast.LENGTH_SHORT).show();
        });
    }


    private void copyShellFile() throws Throwable {
        final String fileName = "starter.sh";
        File file = new File(getExternalCacheDir(), fileName);
        if (file.exists()) file.delete();
        InputStream is = getResources().getAssets().open(fileName);
        FileOutputStream fos = new FileOutputStream(new File(getExternalCacheDir(), fileName));
        int len = -1;
        byte[] buff = new byte[1024];
        while ((len = is.read(buff, 0, buff.length)) != -1) fos.write(buff, 0, len);
        is.close();
        fos.close();
    }
}