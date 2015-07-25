package ai.emot.demo;


import java.io.File;
import java.io.FileFilter;

public class JpegFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().endsWith(".jpg");
	}

}
