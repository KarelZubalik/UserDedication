import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SpecifiedTechnicianTest extends BaseTest {
    private String testName;

    //Pokud chci použít vlastní before např. pokud používám delší čas dedikace technika, nebo přímo technika, tak musím udělat befores override
    @BeforeTest
    void before() throws Exception {
        testName = getClass().getName();
        user=userDedication.alocateTechnician(testName, "AD02");
    }

    @Test(testName = "SpecifiedTechnician")
    public void testMethod() {
        System.out.println("K testu " + testName + " je přiřazen user:" + user + " sleepTime je=" + 30000);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("K testu " + testName + " uvolňuji technika:" + user);
    }
}
