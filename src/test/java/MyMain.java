import com.iih5.goodorm.generator.ModelGenerator;


public class MyMain {

    public static void main(String[] args) throws Exception {
        System.out.println("hello world");
        SpringContext.getInstace();
        System.out.println("开始。。。");
        ModelGenerator.generator("starbuy","com.tthd.model.generator","D:/IdeaProjects/smart-goodorm/src/main/java");
        System.out.println("。。。结束");

    }

}
