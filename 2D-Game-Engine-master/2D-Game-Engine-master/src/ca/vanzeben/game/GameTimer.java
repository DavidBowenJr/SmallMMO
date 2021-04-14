package ca.vanzeben.game;

public class GameTimer {
    private long lTime;
    private long lmsaAlpha_Timer1;
    private long lmsaAlpha_Timer2;
    private double dnsPerTick;
    private double dReciprocalNS_PerTick;
    private double dDelta;
    public int counter;
    public int counter2;

    public GameTimer(){
        lTime = System.nanoTime();
        dReciprocalNS_PerTick = 60.0E-9;
        dDelta = 0;
        lmsaAlpha_Timer1 = System.currentTimeMillis();
        lmsaAlpha_Timer2 = System.currentTimeMillis();
    }

    public static int inspection = 0;
    public void processTime(Game game){
        game.init();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true) {
            long lNow = System.nanoTime();
            dDelta += (dReciprocalNS_PerTick * (lNow - lTime));
            lTime = lNow;
            boolean bRender = false;
            double dbefore = 0;
            while (dDelta >= 1) {
                dbefore = dDelta;
                dDelta -= 1;
                bRender = true;
            }

            if(bRender) {
                inspection++;
                game.tick();
                game.render();
            }
            long lmsNowBeta = System.currentTimeMillis();
            double PulseT1WaitFor = 1.e3;
            PulseT1WaitFor = 0.5e3;
            if(lmsNowBeta - lmsaAlpha_Timer1 >= PulseT1WaitFor) {
                lmsaAlpha_Timer1 += PulseT1WaitFor;
                counter++;
                inspection = 0;
            }

            TWait();
            double PulseT2WaitFor = 10.e3; // Wait 10 seconds
            if(lmsNowBeta - lmsaAlpha_Timer2 >= PulseT2WaitFor) {
                lmsaAlpha_Timer2 += PulseT2WaitFor;
                counter2++;
            }
        }
    }

    public void TWait()
    {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
