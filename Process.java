import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Process extends UnicastRemoteObject implements ProcessInterface {
    private int localSequenceNumber;
    private boolean inCriticalSection;
    private TokenManagerInterface tokenManager;
    private int processId;

    public Process(int processId, TokenManagerInterface tokenManager) throws RemoteException {
        this.processId = processId;
        this.tokenManager = tokenManager;
        this.localSequenceNumber = 0;
        this.inCriticalSection = false;
    }

    @Override
    public synchronized void requestCriticalSection() throws RemoteException {
        localSequenceNumber++;
        System.out.println("Process " + processId + " requests entry to the critical section.");
        tokenManager.requestEntry(processId, localSequenceNumber);
    }

    @Override
    public synchronized void releaseCriticalSection() throws RemoteException {
        inCriticalSection = false;
        System.out.println("Process " + processId + " releases the critical section.");
        tokenManager.releaseToken(processId, localSequenceNumber);
    }

    @Override
    public int getSequenceNumber() throws RemoteException {
        return localSequenceNumber;
    }

    @Override
    public synchronized void receiveGrant() throws RemoteException {
        inCriticalSection = true;
        System.out.println("Process " + processId + " enters the critical section.");
    }
}
