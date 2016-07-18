package moc.ogop.ahsp.net.io;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

/**
 * Created by jameszheng on 2016/7/10.
 */
public class RPacket implements IPacket, Externalizable {

    private static final long serialVersionUID = 42L;

    private int requestId;

    private String methodName;

    private List<String> argTypes;

    private Object[] args;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(List<String> argTypes) {
        this.argTypes = argTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(requestId);
        out.writeUTF(methodName);
        int argc = args == null ? 0 : args.length;
        out.writeInt(argc);
        out.writeObject(argTypes);
        for (int i = 0 ; i < argc; i++) {
            out.writeObject(args[i]);
        }
    }

    @Override
    public void readExternal(ObjectInput oin) throws IOException, ClassNotFoundException {
        requestId  = oin.readInt();
        methodName = oin.readUTF();
        int argc    = oin.readInt();
        argTypes = (List<String>) oin.readObject();
        args = new Object[argc];
        for (int i = 0 ; i < args.length; i++) {
            args[i] = oin.readObject();
        }
    }
}
