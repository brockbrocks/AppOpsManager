package app.jhau.framework.appops;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AppOps {

    public static final AppOps INSTANCE = new AppOps();

    private final AbsAppOpsMgr absAppOpsMgr;

    public AppOps() {
        if (Build.VERSION.SDK_INT >= 26) {
            absAppOpsMgr = new AppOpsMgr26();
        } else {
            absAppOpsMgr = new AppOpsMgr23();
        }
    }

    public int getNumOp() {
        return absAppOpsMgr.getNumOp();
    }

    public List<AppOps.PkgOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        List<AbsAppOpsMgr.AbsPkgOps> pkgOps = absAppOpsMgr.getOpsForPackage(uid, packageName, ops);
        List<AppOps.PkgOps> ret = new ArrayList<>();
        for (AbsAppOpsMgr.AbsPkgOps op : pkgOps) {
            ret.add(new AppOps.PkgOps(op));
        }
        return ret;
    }

    public static final class PkgOps implements Parcelable {
        private final AbsAppOpsMgr.AbsPkgOps absOps;

        public PkgOps(AbsAppOpsMgr.AbsPkgOps absOps){
            this.absOps = absOps;
        }

        public PkgOps(Parcel in) {
            absOps = in.readParcelable(AbsAppOpsMgr.AbsPkgOps.class.getClassLoader());
        }

        public static final Creator<PkgOps> CREATOR = new Creator<PkgOps>() {
            @Override
            public PkgOps createFromParcel(Parcel in) {
                return new PkgOps(in);
            }

            @Override
            public PkgOps[] newArray(int size) {
                return new PkgOps[size];
            }
        };

        public String getPackageName() {
            return absOps.getPackageName();
        }

        public int getUid() {
            return absOps.getUid();
        }

        public List<AppOps.OpEntry> getOps() {
            List<AppOps.OpEntry> ret = new ArrayList<>();
            for (AbsAppOpsMgr.AbsOpEntry op : absOps.getOps()) {
                ret.add(new AppOps.OpEntry(op));
            }
            return ret;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeParcelable(absOps, flags);
        }
    }

    public static final class OpEntry implements Parcelable {

        private final AbsAppOpsMgr.AbsOpEntry absOpEntry;

        public OpEntry(AbsAppOpsMgr.AbsOpEntry absOpEntry) {
            this.absOpEntry = absOpEntry;
        }

        public OpEntry(Parcel in) {
            absOpEntry = in.readParcelable(AbsAppOpsMgr.AbsOpEntry.class.getClassLoader());
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
            return absOpEntry.getOp();
        }

        public int getMode() {
            return absOpEntry.getMode();
        }

        public long getTime() {
            return absOpEntry.getTime();
        }

        public long getRejectTime() {
            return absOpEntry.getRejectTime();
        }

        public boolean isRunning() {
            return absOpEntry.isRunning();
        }

        public int getDuration() {
            return absOpEntry.getDuration();
        }

        public int getProxyUid() {
            return absOpEntry.getProxyUid();
        }

        public String getProxyPackageName() {
            return absOpEntry.getProxyPackageName();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeParcelable(absOpEntry, flags);
        }
    }
}
