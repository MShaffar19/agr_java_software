package org.alliancegenome.es.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProcessDisplayHelper {

    private Runtime runtime = Runtime.getRuntime();
    private DecimalFormat df = new DecimalFormat("#");
    
    private Date startTime = new Date();
    private Date lastTime = new Date();
    private String message;
    private int lastSize;
    private int totalSize;
    private int sizeCounter = 0;
    
    public void startProcess(String message, int totalSize) {
        this.message = message + ": ";
        this.totalSize = totalSize;
        sizeCounter = 0;
        log.info(message + "Starting Processing: total: " + getBigNumber(totalSize) + " at: " + startTime);
        lastTime = new Date();
    }

    public void progress() {
        sizeCounter++;
        double percent = ((double) (totalSize - sizeCounter) / (double) totalSize);
        Date now = new Date();
        long diff = now.getTime() - startTime.getTime();
        
        if(diff < 30000) return; // report every 30 seconds
        checkMemory();
        
        long time = (now.getTime() - lastTime.getTime());
        int processedAmount = (lastSize - sizeCounter);
        String message = "" + getBigNumber(totalSize - sizeCounter) + " records [" + getBigNumber(totalSize) + "] ";
        message += (int) (percent * 100) + "% took: " + (time / 1000) + "s to process " + processedAmount;

        message += " rate: " + ((processedAmount * 1000) / time) + "r/s";

        if (percent > 0) {
            int perms = (int) (diff / percent);
            Date end = new Date(startTime.getTime() + perms);
            String expectedDuration = getHumanReadableTimeDisplay(end.getTime() - (new Date()).getTime());
            message += ", Memory: " + df.format(memoryPercent() * 100) + "%, ETA: " + expectedDuration + " [" + end + "]";
        }
        log.info(this.message + message);
        lastSize = sizeCounter;
        lastTime = now;
    }

    public void finishProcess() {
        Date now = new Date();
        long duration = now.getTime() - startTime.getTime();
        String result = getHumanReadableTimeDisplay(duration);
        log.info(message + "Finished: took: " + result + " to process " + getBigNumber(totalSize) + " records at a rate of: " + ((totalSize * 1000) / duration) + "r/s");
    }

    private static String getBigNumber(int number) {
        return String.format("%,d", number);
    }

    public static String getHumanReadableTimeDisplay(long duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    private void checkMemory() {
        if (memoryPercent() > 0.95) {
            log.warn(message + "Memory Warning: " + df.format(memoryPercent() * 100) + "%");
            log.warn(message + "Used Mem: " + (runtime.totalMemory() - runtime.freeMemory()));
            log.warn(message + "Free Mem: " + runtime.freeMemory());
            log.warn(message + "Total Mem: " + runtime.totalMemory());
            log.warn(message + "Max Memory: " + runtime.maxMemory());
        }
    }

    private double memoryPercent() {
        return ((double) runtime.totalMemory() - (double) runtime.freeMemory()) / (double) runtime.maxMemory();
    }
}
