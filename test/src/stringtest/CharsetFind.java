package stringtest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import khh.file.util.FileUtil;
import khh.string.util.StringUtil;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetFind{
	public static void main(String[] args) throws IOException{
//		FileUtil.writeFile("c:\\\\\\\\hhh.txt","a김");
//		
//		byte[] buf = new byte[4096]; 
//		//String fileName = args[0]; 
//		java.io.FileInputStream fis = new java.io.FileInputStream("C:\\\\\\\\hhk.txt"); 
//
//		// (1) 
//		UniversalDetector detector = new UniversalDetector(null); 
//
//		// (2) 
//		int nread; 
//		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) { 
//		detector.handleData(buf, 0, nread); 
//		} 
//		// (3) 
//		detector.dataEnd(); 
//
//		// (4) 
//		String encoding = detector.getDetectedCharset(); 
//		if (encoding != null) { 
//		System.out.println("Detected encoding = " + encoding); 
//		} else { 
//		System.out.println("No encoding detected."); 
//		} 
//
//		// (5) 
//		detector.reset(); 
		
		System.out.println( FileUtil.findEncoding("D:\\finger\\code\\java\\Utilkhh\\src\\khh\\std\\realworld\\TPointToPoint.java") );
		
		String a ="%asd%UTKgvgasd";
		System.out.println( a.indexOf("%",5));
		/*
        Pattern p = Pattern.compile("%");
        Matcher m = p.matcher(a);
		System.out.println(m.find());
		*/
	}
}
