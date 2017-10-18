

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class RaceCondition {

  private static final int UPPER_LIMIT = 20_000_000; // Can use underscore to make large numbers easier to read.
  private static final int NUM_THREADS = 10;
  private static final String START_MESSAGE = "Starting %d threads.%n";
  private static final String THREAD_COMPLETE_MESSAGE = "Thread %d complete.%n";
  private static final String FINISH_MESSAGE = "Sum = %,d.%n";
  
  private long sum = 0;
  private CountDownLatch signal;
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    RaceCondition race = new RaceCondition();
    race.start();
    race.finish();

  }
  
  private void start() {
    System.out.printf(START_MESSAGE, NUM_THREADS);
    signal = new CountDownLatch(NUM_THREADS);
    for (int i = 0; i < NUM_THREADS; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          // TODO Auto-generated method stub
          for (int i = 0; i < UPPER_LIMIT; i++) {
            sum += ThreadLocalRandom.current().nextInt(2);
          }
          signal.countDown();
        }
        
      }).start();
    }
  }
    private void finish() {
      try {
        signal.await();
      } catch (InterruptedException ex) {
      // Do nothing    
      } finally {
        System.out.printf(FINISH_MESSAGE, sum);
      }
  }

}
