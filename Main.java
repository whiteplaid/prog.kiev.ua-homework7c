package com.homework.filesearch;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		File file = null;
		Thread tmp = new Thread();
		file = new File("src/com/homework/filesearch/Source/");
		String name = "a.txt";
		FileSearch search = new FileSearch (file,name);
		tmp = new Thread(search);
		tmp.start();

	}

}
