package test;
import java.util.Scanner;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTest1 {
	public static final void main(String[] args) {
        try {
            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
            Scanner in = new Scanner(System.in);
            System.out.println("请输入需要评级的分数：（输入0退出循环）");
            int data = in.nextInt();
            while(data!=0) {
                Score s = new Score(data);
                kSession.insert(s);
                kSession.fireAllRules();
                System.out.println("请输入需要评级的分数：（输入0退出循环）");
                data = in.nextInt();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
