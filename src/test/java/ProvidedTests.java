import org.testng.annotations.*;

import java.util.Random;

public class ProvidedTests {
    static int testNumber = 0;

    @DataProvider(name = "dataProvider", parallel = true)
    public Object[][] dataProviderMethod() {
        Random random = new Random();
        Object[][] objects = new Object[100][1];
        for (int i = 0; i < 100; i++) {
            objects[i][0] = random.nextInt(10000) + 10000;
        }
        return objects;
//        return new Object[][] { {"data1"}, {"data2"}, {"data3"} };
    }
}
