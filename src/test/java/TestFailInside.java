import org.testng.annotations.Test;

public class TestFailInside extends BaseTest {

    @Test(testName = "SpecifiedTechnician")
    public void testMethod() throws Exception {
        String testNumber = getClass().getName();
        System.out.println("K testu " + testNumber + " je přiřazen user:" + user + " sleepTime je=" + 5000);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new Exception("Prostě jsem spadl, aby jsem viděl, jestli afterTest zafunguje.");
    }
}
