import java.io.File;

public class main {
    public static void main(String[] args) {
        String filePath = "src/CvixLer-MarkerSubset-LG1.txt";
        File input = new File(filePath);

        GLMProject inst = new GLMProject();
        inst.execute(input);
    }
}
