import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;
import java.util.Comparator;

public class TokenManager extends UnicastRemoteObject implements TokenManagerInterface {
    private int currentHolder;
    private PriorityQueue<Request> requestQueue;

    private class Request {
        int processId;
        int sequenceNumber;

        Request(int processId, int sequenceNumber) {
            this.processId = processId;
            this.sequenceNumber = sequenceNumber;
        }
    }

    public TokenManager() throws RemoteException {
        currentHolder = -1; // Initially, no one holds the token
        requestQueue = new PriorityQueue<>(Comparator.comparingInt((Request r) -> r.sequenceNumber)
                .thenComparingInt(r -> r.processId));
    }

    @Override
    public synchronized void requestEntry(int processId, int sequenceNumber) throws RemoteException {
        System.out.println("TokenManager receives REQUEST from Process " + processId);
        requestQueue.add(new Request(processId, sequenceNumber));

        if (currentHolder == -1 || (processId == currentHolder)) {
            grantTokenToNextEligibleProcess();
        }
    }

    @Override
    public synchronized void releaseToken(int processId, int sequenceNumber) throws RemoteException {
        System.out.println("TokenManager receives RELEASE from Process " + processId);
        requestQueue.removeIf(r -> r.processId == processId);
        grantTokenToNextEligibleProcess();
    }

    private synchronized void grantTokenToNextEligibleProcess() throws RemoteException {
        if (!requestQueue.isEmpty()) {
            Request nextRequest = requestQueue.poll();
            currentHolder = nextRequest.processId;
            ProcessInterface nextProcess = (ProcessInterface) Naming.lookup("rmi://localhost/Process" + nextRequest.processId);
            nextProcess.receiveGrant();
            System.out.println("TokenManager grants access to Process " + nextRequest.processId);
        } else {
            currentHolder = -1;
        }
    }
}
