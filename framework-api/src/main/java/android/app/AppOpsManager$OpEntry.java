package android.app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

@SuppressLint("ParcelCreator")
public class AppOpsManager$OpEntry implements Parcelable {
    private final int mOp;
    private final int mMode;
    private final long mTime;
    private final long mRejectTime;
    private final int mDuration;
    private final int mProxyUid;
    private final String mProxyPackageName;

    public AppOpsManager$OpEntry(int op, int mode, long time, long rejectTime, int duration, int proxyUid, String proxyPackage) {
        mOp = op;
        mMode = mode;
        mTime = time;
        mRejectTime = rejectTime;
        mDuration = duration;
        mProxyUid = proxyUid;
        mProxyPackageName = proxyPackage;
    }

    public int getOp() {
        return mOp;
    }

    public int getMode() {
        return mMode;
    }

    public long getTime() {
        return mTime;
    }

    public long getRejectTime() {
        return mRejectTime;
    }

    public boolean isRunning() {
        return mDuration == -1;
    }

    public int getDuration() {
        return mDuration == -1 ? (int)(System.currentTimeMillis()-mTime) : mDuration;
    }

    public int getProxyUid() {
        return  mProxyUid;
    }

    public String getProxyPackageName() {
        return mProxyPackageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
