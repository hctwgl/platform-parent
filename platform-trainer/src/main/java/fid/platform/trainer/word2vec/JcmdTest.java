package fid.platform.trainer.word2vec;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Parameter(names = {"--length", "-l"})
    int length;
    @Parameter(names = {"--pattern", "-p"})
    int pattern;

    public static void main(String... argv) {
        logger.info("main is running~~~~");
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {
        System.out.printf("%d %d", length, pattern);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished~~~~~~~~~~~");
    }
}
