package app.jhau.server;

import android.os.RemoteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AppOpsServer extends IAppOpsServer.Stub {

    @Override
    public String execCommand(String cmd) throws RemoteException {
        InputStream is;
        Reader r;
        StringBuilder ret = new StringBuilder();
        try {
            is = Runtime.getRuntime().exec(cmd).getInputStream();
            r = new InputStreamReader(is);
            char[] buff = new char[1024];
            while (r.read(buff, 0, buff.length) != -1) {
                ret.append(buff, 0, buff.length);
            }
            try {
                r.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }
}