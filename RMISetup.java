import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMISetup {
    public static void main(String[] args) {
        try {
            // Setup RMI registry
            LocateRegistry.createRegistry(1099);

            // Create Token Manager and bind it to the registry
            TokenManager tokenManager = new TokenManager();
            Naming.rebind("rmi://localhost/TokenManager", tokenManager);
            System.out.println("Token Manager is ready.");

            // Create Processes and bind them to the registry
            for (int i = 1; i <= 3; i++) { // Example with 3 processes
                Process process = new Process(i, tokenManager);
                Naming.rebind("rmi://localhost/Process" + i, process);
                System.out.println("Process " + i + " is ready.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
