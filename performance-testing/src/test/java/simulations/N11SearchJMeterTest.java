package simulations;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;

import java.io.File;

public class N11SearchJMeterTest {

    public static void main(String[] args) {
        File jmeterHome = new File("performance-testing/src/test/resources/jmeter");

        if (!jmeterHome.exists()) {
            jmeterHome.mkdirs();
        }

        JMeterUtils.setJMeterHome(jmeterHome.getAbsolutePath());
        JMeterUtils.loadJMeterProperties(jmeterHome.getAbsolutePath() + "/jmeter.properties");
        JMeterUtils.initLocale();

        TestPlan testPlan = new TestPlan("N11 Search Test Plan");

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Search Users");
        threadGroup.setNumThreads(1);

        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);
        threadGroup.setSamplerController(loopController);

        HTTPSamplerProxy homePage = new HTTPSamplerProxy();
        homePage.setDomain("www.n11.com");
        homePage.setPath("/");
        homePage.setMethod("GET");
        homePage.setName("Open Home Page");

        HTTPSamplerProxy search = new HTTPSamplerProxy();
        search.setDomain("www.n11.com");
        search.setPath("/arama");
        search.setMethod("GET");
        search.addArgument("q", "iphone");
        search.setName("Search for iPhone");

        HTTPSamplerProxy secondPage = new HTTPSamplerProxy();
        secondPage.setDomain("www.n11.com");
        secondPage.setPath("/arama");
        secondPage.setMethod("GET");
        secondPage.addArgument("q", "iphone");
        secondPage.addArgument("pg", "2");
        secondPage.setName("Navigate to Second Page");

        ListedHashTree testPlanTree = new ListedHashTree();
        ListedHashTree threadGroupHashTree = (ListedHashTree) testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(homePage);
        threadGroupHashTree.add(search);
        threadGroupHashTree.add(secondPage);

        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        jmeter.configure(testPlanTree);
        jmeter.run();

        System.out.println("Test completed successfully!");
    }

}
