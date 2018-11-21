package com.homework.filesearch;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchCurrent implements Runnable {
	private File source; 
	private String fileName;
	private FileSearch directory;
	public SearchCurrent(File source, String fileName, FileSearch directory) {
		super();
		this.source = source;
		this.fileName = fileName;
		this.directory = directory;
	}
	public SearchCurrent() {
		super();
	}
	
	private File getSource() {
		return source;
	}
	
	private String getFileName() {
		return fileName;
	}
	
	private FileSearch getDirectory() {
		return directory;
	}
	private synchronized void search (File source, String fileName) {
		File[] paths;   	  
		paths = source.listFiles();
		for (File path : paths) {
			if (path.isDirectory()) {
				AtomicInteger dircount = getDirectory().getDircount();
				while (dircount.get() > 2) {
					try {
						wait();
					} catch (InterruptedException e) {
						
					}
				}
				dircount.incrementAndGet();
				getDirectory().setDircount(dircount);
				getDirectory().search(path, fileName);
				notify();
			} else {
				if (path.getName().equals(fileName)) {
					AtomicInteger dircount = getDirectory().getDircount();
					dircount.decrementAndGet();
					getDirectory().setDircount(dircount);
					System.out.println(path.getAbsolutePath() + " " + " " + Thread.currentThread());
					notify();
				}
			}
		}
		
	}
	@Override
	public void run() {
		search(getSource(),getFileName());
	}
}
