package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Addition {
	BufferedReader codeReader;
	Map<Integer, String> assemblyCommands = new HashMap<>();
	public Addition(String file) {
		setupReader(file);
		
	}

	private void setupReader(String files) {
		try {
			File addFile = new File(files);
			codeReader = new BufferedReader(new FileReader(addFile));
			String line;
			try {
				while((line = codeReader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				codeReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
