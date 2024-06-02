import org.userDedication.UserDedication;
import org.testng.annotations.Test;

public class AnotherTest {
    @Test(dataProvider = "dataProvider",dataProviderClass = ProvidedTests.class)
    public void testMethod(int data) throws Exception {
        String user;
        UserDedication userDedication;
        userDedication=new UserDedication();
        String testNumber="test=" + ProvidedTests.testNumber++;
        user=userDedication.alocateTechnician(testNumber);
        System.out.println("K testu "+testNumber+" je přiřazen user:"+user+" sleepTime je="+data);
        try {
            Thread.sleep(data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("Test method with data: " + data + " on thread: " + Thread.currentThread().getId());
        System.out.println("K testu "+testNumber+" uvolňuji technika:"+user);
        userDedication.freeTechnician(user);
    }
}
