package android.app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

@SuppressLint("ParcelCreator")
public class AppOpsManager$PackageOps implements Parcelable {

    private final String mPackageName;
    private final int mUid;
    private final List<AppOpsManager$OpEntry> mEntries;

    public AppOpsManager$PackageOps(String packageName, int uid, List<AppOpsManager$OpEntry> entries) {
        mPackageName = packageName;
        mUid = uid;
        mEntries = entries;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getUid() {
        return mUid;
    }

    public List<AppOpsManager$OpEntry> getOps() {
        return mEntries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
