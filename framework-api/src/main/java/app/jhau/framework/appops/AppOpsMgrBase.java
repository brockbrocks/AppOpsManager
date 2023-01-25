package app.jhau.framework.appops;

import android.app.AppOpsManager;
import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;

import androidx.annotation.NonNull;

import com.android.internal.app.IAppOpsService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AppOpsMgrBase implements AbsAppOpsMgr {

    public static int _NUM_OP;

    private final IAppOpsService mService = IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));

    @Override
    public int getNumOp() {
        return _NUM_OP;
    }

    @Override
    public List<AbsAppOpsMgr.AbsPkgOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        List<AppOpsManager$PackageOps> pkgOps = mService.getOpsForPackage(uid, packageName, ops);
        List<AbsAppOpsMgr.AbsPkgOps> ret = new ArrayList<>();
        for (AppOpsManager$PackageOps op : pkgOps) {
            ret.add(new AppOpsMgrBase.PkgOpsBase(op));
        }
        return ret;
    }

    static {
        try {
            for (Field field : AppOpsManager.class.getDeclaredFields()) {
                String fieldName = field.getName();
                field.setAccessible(true);
                if (fieldName.equals("_NUM_OP")) {
                    _NUM_OP = (int) field.get(null);
                    continue;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static class PkgOpsBase implements AbsPkgOps {

        private final String mPackageName;
        private final int mUid;
        private final List<AbsOpEntry> mEntries;

        public PkgOpsBase(AppOpsManager$PackageOps ops) {
            mPackageName = ops.getPackageName();
            mUid = ops.getUid();
            mEntries = new ArrayList<>();
            for (AppOpsManager$OpEntry op : ops.getOps()) {
                mEntries.add(new OpEntryBase(op));
            }
        }

        public PkgOpsBase(Parcel in) {
            mPackageName = in.readString();
            mUid = in.readInt();
            mEntries = new ArrayList<>();
            in.readTypedList(mEntries, OpEntryBase.CREATOR);
        }

        public @NonNull String getPackageName() {
            return mPackageName;
        }

        public int getUid() {
            return mUid;
        }

        public @NonNull List<AbsOpEntry> getOps() {
            return mEntries;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(mPackageName);
            dest.writeInt(mUid);
            dest.writeTypedList(mEntries);
        }

        public static final @NonNull Parcelable.Creator<AbsAppOpsMgr.AbsPkgOps> CREATOR = new Creator<AbsPkgOps>() {
            @Override
            public AbsPkgOps createFromParcel(Parcel source) {
                return new PkgOpsBase(source);
            }

            @Override
            public PkgOpsBase[] newArray(int size) {
                return new PkgOpsBase[size];
            }
        };
    }

    public static class OpEntryBase implements AbsOpEntry {

        private final int mOp;
        private final int mMode;
        private final long mTime;
        private final long mRejectTime;
        private final int mDuration;
        private final int mProxyUid;
        private final String mProxyPackageName;


        public OpEntryBase(AppOpsManager$OpEntry opEntry) {
            mOp = opEntry.getOp();
            mMode = opEntry.getMode();
            mTime = opEntry.getTime();
            mRejectTime = opEntry.getRejectTime();
            mDuration = opEntry.getDuration();
            mProxyUid = opEntry.getProxyUid();
            mProxyPackageName = opEntry.getProxyPackageName();
        }

        public OpEntryBase(Parcel in) {
            mOp = in.readInt();
            mMode = in.readInt();
            mTime = in.readLong();
            mRejectTime = in.readLong();
            mDuration = in.readInt();
            mProxyUid = in.readInt();
            mProxyPackageName = in.readString();
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
            return mDuration == -1 ? (int) (System.currentTimeMillis() - mTime) : mDuration;
        }

        public int getProxyUid() {
            return mProxyUid;
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
            dest.writeInt(mOp);
            dest.writeInt(mMode);
            dest.writeLong(mTime);
            dest.writeLong(mRejectTime);
            dest.writeInt(mDuration);
            dest.writeInt(mProxyUid);
            dest.writeString(mProxyPackageName);
        }

        public static final @NonNull Parcelable.Creator<AbsOpEntry> CREATOR = new Creator<AbsOpEntry>() {
            @Override
            public AbsOpEntry createFromParcel(Parcel source) {
                return new OpEntryBase(source);
            }

            @Override
            public OpEntryBase[] newArray(int size) {
                return new OpEntryBase[size];
            }
        };
    }

}
