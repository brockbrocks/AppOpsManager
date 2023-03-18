package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import app.jhau.framework.util.ReflectUtil;

public class OpEntry implements Parcelable {
    private int mOp = -1;
    private String mOpStr = "";
    private int mMode = -1;
    private long mTime = -1;
    private long mLastAccessTime = -1;
    private long mLastAccessForegroundTime = -1;
    private long mLastAccessBackgroundTime = -1;
    private long mRejectTime = -1;
    private long mLastRejectTime = -1;
    private long mLastRejectForegroundTime = -1;
    private long mLastRejectBackgroundTime = -1;
    private long mDuration = -1;
    private long mLastDuration = -1;
    private long mLastForegroundDuration = -1;
    private long mLastBackgroundDuration = -1;
    private int mProxyUid = -1;
    private String mProxyPackageName = "";


    public OpEntry(AppOpsManager$OpEntry opEntry) {
        //mOp
        mOp = opEntry.getOp();
        //mOpStr
        mOpStr = safeGetOpStr(opEntry);
        //mMode
        mMode = opEntry.getMode();
        //mTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        } else {
            mTime = opEntry.getTime();
        }
        //mLastAccessTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastAccessTime = opEntry.getLastAccessTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastAccessTime = opEntry.getLastAccessTime();
        }
        //mLastAccessForegroundTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastAccessForegroundTime = opEntry.getLastAccessForegroundTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastAccessForegroundTime = opEntry.getLastAccessForegroundTime();
        }
        //mLastAccessBackgroundTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastAccessBackgroundTime = opEntry.getLastAccessBackgroundTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastAccessBackgroundTime = opEntry.getLastAccessBackgroundTime();
        }
        //mRejectTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        } else {
            mRejectTime = opEntry.getRejectTime();
        }
        //mLastRejectTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastRejectTime = opEntry.getLastRejectTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastRejectTime = opEntry.getLastRejectTime();
        }
        //mLastRejectForegroundTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastRejectForegroundTime = opEntry.getLastRejectForegroundTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastRejectForegroundTime = opEntry.getLastRejectForegroundTime();
        }
        //mLastRejectBackgroundTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLastRejectBackgroundTime = opEntry.getLastRejectBackgroundTime(AppOpsManagerHidden.OP_FLAGS_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mLastRejectBackgroundTime = opEntry.getLastRejectBackgroundTime();
        }
        //mDuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mDuration = opEntry.getDuration();
        } else {
            try {
                mDuration = (int) ReflectUtil.invokeMethod(Class.forName("android.app.AppOpsManager$OpEntry"), "getDuration", opEntry);
            } catch (Throwable e) {
                e.printStackTrace();
                mDuration = -1;
            }
        }
        //mLastDuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mLastDuration = opEntry.getLastDuration(AppOpsManagerHidden.OP_FLAGS_ALL);
        }
        //mLastForegroundDuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mLastForegroundDuration = opEntry.getLastForegroundDuration(AppOpsManagerHidden.OP_FLAGS_ALL);
        }
        //mLastBackgroundDuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mLastBackgroundDuration = opEntry.getLastBackgroundDuration(AppOpsManagerHidden.OP_FLAGS_ALL);
        }
        mProxyUid = opEntry.getProxyUid();
        mProxyPackageName = opEntry.getProxyPackageName();
        mProxyPackageName = mProxyPackageName == null ? "" : mProxyPackageName;
    }

    private String safeGetOpStr(AppOpsManager$OpEntry opEntry) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return opEntry.getOpStr();
            }
        } catch (Throwable ignored) {
            return "op_" + mOp + ")";
        }
        return "";
    }

    public int getOp() {
        return mOp;
    }

    public String getOpStr() {
        return mOpStr;
    }

    public int getMode() {
        return mMode;
    }

    public long getTime() {
        return mTime;
    }

    public long getLastAccessTime() {
        return mLastAccessTime;
    }

    public long getLastAccessForegroundTime() {
        return mLastAccessForegroundTime;
    }

    public long getLastAccessBackgroundTime() {
        return mLastAccessBackgroundTime;
    }

    public long getRejectTime() {
        return mRejectTime;
    }

    public long getLastRejectTime() {
        return mLastRejectTime;
    }

    public long getLastRejectForegroundTime() {
        return mLastRejectForegroundTime;
    }

    public long getLastRejectBackgroundTime() {
        return mLastRejectBackgroundTime;
    }

    public long getDuration() {
        return mDuration;
    }

    public long getLastDuration() {
        return mLastDuration;
    }

    public long getLastForegroundDuration() {
        return mLastForegroundDuration;
    }

    public long getLastBackgroundDuration() {
        return mLastBackgroundDuration;
    }

    public int getProxyUid() {
        return mProxyUid;
    }

    public String getProxyPackageName() {
        return mProxyPackageName;
    }

    protected OpEntry(Parcel in) {
        mOp = in.readInt();
        mOpStr = in.readString();
        mMode = in.readInt();
        mTime = in.readLong();
        mLastAccessTime = in.readLong();
        mLastAccessForegroundTime = in.readLong();
        mLastAccessBackgroundTime = in.readLong();
        mRejectTime = in.readLong();
        mLastRejectTime = in.readLong();
        mLastRejectForegroundTime = in.readLong();
        mLastRejectBackgroundTime = in.readLong();
        mDuration = in.readLong();
        mLastDuration = in.readLong();
        mLastForegroundDuration = in.readLong();
        mLastBackgroundDuration = in.readLong();
        mProxyUid = in.readInt();
        mProxyPackageName = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(mOp);
        dest.writeString(mOpStr);
        dest.writeInt(mMode);
        dest.writeLong(mTime);
        dest.writeLong(mLastAccessTime);
        dest.writeLong(mLastAccessForegroundTime);
        dest.writeLong(mLastAccessBackgroundTime);
        dest.writeLong(mRejectTime);
        dest.writeLong(mLastRejectTime);
        dest.writeLong(mLastRejectForegroundTime);
        dest.writeLong(mLastRejectBackgroundTime);
        dest.writeLong(mDuration);
        dest.writeLong(mLastDuration);
        dest.writeLong(mLastForegroundDuration);
        dest.writeLong(mLastBackgroundDuration);
        dest.writeInt(mProxyUid);
        dest.writeString(mProxyPackageName);
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

    @Override
    public int describeContents() {
        return 0;
    }
}
