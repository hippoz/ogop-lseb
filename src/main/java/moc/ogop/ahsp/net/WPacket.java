package moc.ogop.ahsp.net;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by jameszheng on 2016/7/10.
 */
public class WPacket implements Externalizable {

    private static final long serialVersionUID = 42L;

    private int requestId;

    private Object result;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(requestId);
        out.writeObject(result);
    }

    @Override
    public void readExternal(ObjectInput oin) throws IOException, ClassNotFoundException {
        requestId = oin.readInt();
        result    = oin.readObject();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
