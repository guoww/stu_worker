package test;

import java.io.File;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;

public class BacthResize {
	
	public static String root = "F:\\canon_0228\\B库";
	
	public static void main(String[] args) throws Exception{
		getFiles(root);
	}
	
	public static void getFiles(String root) throws Exception{
		File rootDirectory = new File(root);
		boolean isDirectory = rootDirectory.isDirectory();
		if(isDirectory){
			File[] files = rootDirectory.listFiles();
			for(File file : files){
				if(file.isFile()){
					resize(file.getAbsolutePath());
				}else{
					getFiles(file.getAbsolutePath());
				}
			}
		}
	}
	
	public static void resize(String path) throws Exception{
		String suffix = path.substring(path.lastIndexOf("."), path.length());
		String preffix = path.substring(0,path.lastIndexOf("."));
		String name = path.substring(path.lastIndexOf("\\"),path.lastIndexOf("."));
		String filePath = root+"\\1440x1440"+name+suffix;
		Thumbnails.of(path).size(1440,1440).toFile(new File(filePath));
	}
}
