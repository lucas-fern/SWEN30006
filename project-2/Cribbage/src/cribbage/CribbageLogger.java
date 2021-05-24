package cribbage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CribbageLogger implements CribbageObserver {
    private static CribbageLogger singletonInstance;

    private BufferedWriter bw;

    private CribbageLogger() {
        try {
            this.bw = new BufferedWriter(new FileWriter("cribbage.log", false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CribbageLogger getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CribbageLogger();
        }

        return singletonInstance;
    }

    @Override
    public void update(CribbageEvent event) {
        try {
            bw.write(event.toString());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
