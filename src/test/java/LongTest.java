import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LongTest extends BaseTest {
    String testName;
    //Pokud chci použít vlastní before např. pokud používám delší čas dedikace technika, nebo přímo technika, tak musím udělat befores override
    @BeforeTest
    void before() throws Exception {
        testName = getClass().getName();
        user=userDedication.alocateTechnician(testName, 60);
    }

    @Test(testName = "LONGTEST")
    public void testMethod() {
        System.out.println("K testu " + testName + " je přiřazen user:" + user + " sleepTime je=" + 40000);
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("K testu " + testName + " uvolňuji technika:" + user);
    }

}
