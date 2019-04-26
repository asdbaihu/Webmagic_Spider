//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lenovo.common;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/***
 * 重写该类，将缓存url文件当程序自动停止时自动flush。
 */
public class FileCacheQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, Closeable {
    private String filePath = System.getProperty("java.io.tmpdir");
    private String fileUrlAllName = ".urls.txt";
    private Task task;
    private String fileCursor = ".cursor.txt";
    private PrintWriter fileUrlWriter;
    private PrintWriter fileCursorWriter;
    private AtomicInteger cursor = new AtomicInteger();
    private AtomicBoolean inited = new AtomicBoolean(false);
    private BlockingQueue<Request> queue;
    private Set<String> urls;
    private ScheduledExecutorService flushThreadPool;

    public FileCacheQueueScheduler(String filePath) {
        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath = filePath + "/";
        }

        this.filePath = filePath;
        this.initDuplicateRemover();
    }

    private void flush() {
        this.fileUrlWriter.flush();
        this.fileCursorWriter.flush();
    }

    private void init(Task task) {
        this.task = task;
        File file = new File(this.filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        this.readFile();
        this.initWriter();
        this.initFlushThread();
        this.inited.set(true);
        this.logger.info("init cache scheduler success");
    }

    private void initDuplicateRemover() {
        this.setDuplicateRemover(new DuplicateRemover() {
            public boolean isDuplicate(Request request, Task task) {
                if (!FileCacheQueueScheduler.this.inited.get()) {
                    FileCacheQueueScheduler.this.init(task);
                }

                return !FileCacheQueueScheduler.this.urls.add(request.getUrl());
            }

            public void resetDuplicateCheck(Task task) {
                FileCacheQueueScheduler.this.urls.clear();
            }

            public int getTotalRequestsCount(Task task) {
                return FileCacheQueueScheduler.this.urls.size();
            }
        });
    }

    private void initFlushThread() {
        this.flushThreadPool = Executors.newScheduledThreadPool(1);
        this.flushThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                FileCacheQueueScheduler.this.flush();
            }
        }, 10L, 10L, TimeUnit.SECONDS);
    }

    private void initWriter() {
        try {
            this.fileUrlWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileUrlAllName), false));
            this.fileCursorWriter = new PrintWriter(new FileWriter(this.getFileName(this.fileCursor), false));
        } catch (IOException var2) {
            throw new RuntimeException("init cache scheduler error", var2);
        }
    }

    private void readFile() {
        try {
            this.queue = new LinkedBlockingQueue();
            this.urls = new LinkedHashSet();
            this.readCursorFile();
            this.readUrlFile();
        } catch (FileNotFoundException var2) {
            this.logger.info("init cache file " + this.getFileName(this.fileUrlAllName));
        } catch (IOException var3) {
            this.logger.error("init file error", var3);
        }

    }

    private void readUrlFile() throws IOException {
        BufferedReader fileUrlReader = null;

        try {
            fileUrlReader = new BufferedReader(new FileReader(this.getFileName(this.fileUrlAllName)));
            int lineReaded = 0;

            String line;
            while((line = fileUrlReader.readLine()) != null) {
                this.urls.add(line.trim());
                ++lineReaded;
                if (lineReaded > this.cursor.get()) {
                    this.queue.add(new Request(line));
                }
            }
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeQuietly(fileUrlReader);
            }

        }

    }

    private void readCursorFile() throws IOException {
        BufferedReader fileCursorReader = null;

        String line;
        try {
            for(fileCursorReader = new BufferedReader(new FileReader(this.getFileName(this.fileCursor))); (line = fileCursorReader.readLine()) != null; this.cursor = new AtomicInteger(NumberUtils.toInt(line))) {
                ;
            }
        } finally {
            if (fileCursorReader != null) {
                IOUtils.closeQuietly(fileCursorReader);
            }

        }

    }

    public void close() throws IOException {
        this.flushThreadPool.shutdown();
        this.fileUrlWriter.close();
        this.fileCursorWriter.close();
    }

    private String getFileName(String filename) {
        return this.filePath + this.task.getUUID() + filename;
    }

    protected void pushWhenNoDuplicate(Request request, Task task) {
        if (!this.inited.get()) {
            this.init(task);
        }

        this.queue.add(request);
        this.fileUrlWriter.println(request.getUrl());
    }

    public synchronized Request poll(Task task) {
        if (!this.inited.get()) {
            this.init(task);
        }

        this.fileCursorWriter.println(this.cursor.incrementAndGet());
        return (Request)this.queue.poll();
    }

    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }
}
