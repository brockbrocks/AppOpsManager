package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OpEntry implements Parcelable {
    private final int mOp;
    private final int mMode;
    private final long mTime;
    private final long mRejectTime;
//    private final int mDuration;
//    private final int mProxyUid;
//    private final String mProxyPackageName;

    public OpEntry(AppOpsManager$OpEntry opEntry) {
        mOp = opEntry.getOp();
        mMode = opEntry.getMode();
        mTime = opEntry.getTime();
        mRejectTime = opEntry.getRejectTime();
    }

    protected OpEntry(Parcel in) {
        mOp = in.readInt();
        mMode = in.readInt();
        mTime = in.readLong();
        mRejectTime = in.readLong();
    }

    public static final Creator<OpEntry> CREATOR = new Creator<OpEntry>() {
        @Override
        public OpEntry createFromParcel(Parcel in) {
            return new OpEntry(in);
        }

        @Override
        public OpEntry[] newArray(int size) {
            return new OpEntry[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(mOp);
        dest.writeInt(mMode);
        dest.writeLong(mTime);
        dest.writeLong(mRejectTime);
    }
}
