package com.homework.filesearch;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;


public class FileSearch implements Runnable {
	private File source;
	private String fileName;
	private AtomicInteger dircount = new AtomicInteger(0);
	public FileSearch(File source, String fileName) {
		super();
		this.source = source;
		this.fileName = fileName;
	}
	public FileSearch() {
		super();
	}
	
	private File getSource() {
		return source;
	}

	public AtomicInteger getDircount() {
		return dircount;
	}
	public void setDircount(AtomicInteger dircount) {
		this.dircount = dircount;
		
	}
	public synchronized void search (File dir, String fileName) {
		File[] paths = dir.listFiles();
		for (File path : paths) {
			if (path.isFile()) {
				if (path.getName().equals(fileName)) {
					System.out.println(path.getAbsolutePath() + " " + Thread.currentThread());
					dircount.decrementAndGet();
					setDircount(dircount);
					notify();
				}
			} else { 
			while (dircount.get() > 2) {
				try {
					wait();
				} catch (InterruptedException e) {
					
				}
			}
			new Thread(new SearchCurrent(path,fileName,this)).start();
			}
		}
		dircount.decrementAndGet();
	}
	@Override
	public void run() {
			search(getSource() ,fileName);							
		}
}
