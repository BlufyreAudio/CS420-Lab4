import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessInterface extends Remote {
    void requestCriticalSection() throws RemoteException;
    void releaseCriticalSection() throws RemoteException;
    int getSequenceNumber() throws RemoteException;
    void receiveGrant() throws RemoteException;
}
