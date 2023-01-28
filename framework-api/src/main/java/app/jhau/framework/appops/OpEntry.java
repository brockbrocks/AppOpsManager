package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/**
 * OpEntry
 */
public final class OpEntry implements Parcelable {
    private final IOpEntry iOpEntry;

    public OpEntry(AppOpsManager$OpEntry opEntry) {
        iOpEntry = new OpEntryImpl(opEntry);
    }

    protected OpEntry(Parcel in) {
        iOpEntry = IOpEntry.Stub.asInterface(in.readStrongBinder());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(iOpEntry.asBinder());
    }

    @Override
    public int describeContents() {
        return 0;
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

    private static final class OpEntryImpl extends IOpEntry.Stub {
        private AppOpsManager$OpEntry opEntry;

        public OpEntryImpl(AppOpsManager$OpEntry opEntry) {
            this.opEntry = opEntry;
        }

        @Override
        public int getOp() throws RemoteException {
            return opEntry.getOp();
        }

        @Override
        public int getMode() throws RemoteException {
            return opEntry.getMode();
        }
    }

    public int getOp() throws RemoteException {
        return iOpEntry.getOp();
    }

    public int getMode() throws RemoteException {
        return iOpEntry.getMode();
    }
}
