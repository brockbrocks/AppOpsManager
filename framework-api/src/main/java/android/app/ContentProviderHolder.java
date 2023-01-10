package android.app;

import android.content.IContentProvider;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.O)
public class ContentProviderHolder {
    public IContentProvider provider;
}