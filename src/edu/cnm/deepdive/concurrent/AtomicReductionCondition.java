package edu.cnm.deepdive.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicReductionCondition {

  private static final int UPPER_LIMIT = 20_000_000; // Can use underscore to make large numbers easier to read.
  private static final int NUM_THREADS = 10;
  private static final String START_MESSAGE = "Starting %d threads.%n";
  private static final String THREAD_COMPLETE_MESSAGE = "Thread %d complete.%n";
  private static final String FINISH_MESSAGE = "Sum = %,d.%n";
  
  private AtomicLong sum;
  private CountDownLatch signal;
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    AtomicReductionCondition race = new AtomicReductionCondition();
    race.start();
    race.finish();

  }
  
  private void start() {
    System.out.printf(START_MESSAGE, NUM_THREADS);
    signal = new CountDownLatch(NUM_THREADS);
    sum = new AtomicLong(0);
    for (int i = 0; i < NUM_THREADS; i++) {
      new Thread(() -> {
          long localSum = 0;
          for (int j = 0; j < UPPER_LIMIT; j++) {
            localSum += ThreadLocalRandom.current().nextInt(2);
          }
          sum.addAndGet(localSum);
          System.out.printf(THREAD_COMPLETE_MESSAGE, Thread.currentThread().getId());
          signal.countDown();
      }).start();
    }
  }
    private void finish() {
      try {
        signal.await();
      } catch (InterruptedException ex) {
      // Do nothing    
      } finally {
        System.out.printf(FINISH_MESSAGE, sum.longValue());
      }
  }

}
