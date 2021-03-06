package khh.file.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import khh.property.util.PropertyUtil;
import khh.string.util.StringUtil;

import org.mozilla.universalchardet.UniversalDetector;

public class FileUtil{
	/** 1 KB */
	public static final int KB_SIZE = 1024;
	/** 1 MB. */
	public static final int MB_SIZE = KB_SIZE * KB_SIZE;
	/** 1 GB */
	public static final int GB_SIZE = KB_SIZE * MB_SIZE;

	// MIME에서 제공하는 content-type
	// http://gissmmo.tistory.com/entry/%ED%8E%8CMIME-Type%EA%B3%BC-Content-Type%EC%9D%98-%EC%9D%B4%ED%95%B4
/*
.*		application/octet-stream
.323		text/h323
.acx		application/internet-property-stream
.ai		application/postscript
.aif		audio/x-aiff
.aifc		audio/aiff
.aiff		audio/aiff
.asf		video/x-ms-asf
.asr		video/x-ms-asf
.asx		video/x-ms-asf
.au		audio/basic
.avi		video/x-msvideo
.axs		application/olescript
.bas		text/plain
.bcpio		application/x-bcpio
.bin		application/octet-stream
.bmp		image/bmp
.c		text/plain
.cat		application/vndms-pkiseccat
.cdf		application/x-cdf
.cer		application/x-x509-ca-cert
.clp		application/x-msclip
.cmx		image/x-cmx
.cod		image/cis-cod
.cpio		application/x-cpio
.crd		application/x-mscardfile
.crl		application/pkix-crl
.crt		application/x-x509-ca-cert
.csh		application/x-csh
.css		text/css
.dcr		application/x-director
.der		application/x-x509-ca-cert
.dib		image/bmp
.dir		application/x-director
.dll		application/x-msdownload
.doc		application/msword
.dot		application/msword
.dvi		application/x-dvi
.dxr		application/x-director
.eml		message/rfc822
.eps		application/postscript
.etx		text/x-setext
.evy		application/envoy
.exe		application/octet-stream
.fif		application/fractals
.flr		x-world/x-vrml
.gif		image/gif
.gtar		application/x-gtar
.gz		application/x-gzip
.h		text/plain
.hdf		application/x-hdf
.hlp		application/winhlp
.hqx		application/mac-binhex40
.hta		application/hta
.htc		text/x-component
.htm		text/html
.html		text/html
.htt		text/webviewhtml
.ico		image/x-icon
.ief		image/ief
.iii		application/x-iphone
.ins		application/x-internet-signup
.isp		application/x-internet-signup
.IVF		video/x-ivf
.jfif		image/pjpeg
.jpe		image/jpeg
.jpeg		image/jpeg
.jpg		image/jpeg
.js		application/x-javascript
.latex		application/x-latex
.lsf		video/x-la-asf
.lsx		video/x-la-asf
.m13		application/x-msmediaview
.m14		application/x-msmediaview
.m1v		video/mpeg
.m3u		audio/x-mpegurl
.man		application/x-troff-man
.mdb		application/x-msaccess
.me		application/x-troff-me
.mht		message/rfc822
.mhtml		message/rfc822
.mid		audio/mid
.mny		application/x-msmoney
.mov		video/quicktime
.movie		video/x-sgi-movie
.mp2		video/mpeg
.mp3		audio/mpeg
.mpa		video/mpeg
.mpe		video/mpeg
.mpeg		video/mpeg
.mpg		video/mpeg
.mpp		application/vnd.ms-project
.mpv2		video/mpeg
.ms		application/x-troff-ms
.mvb		application/x-msmediaview
.nc		application/x-netcdf
.nws		message/rfc822
.oda		application/oda
.ods		application/oleobject
.p10		application/pkcs10
.p12		application/x-pkcs12
.p7b		application/x-pkcs7-certificates
.p7c		application/pkcs7-mime
.p7m		application/pkcs7-mime
.p7r		application/x-pkcs7-certreqresp
.p7s		application/pkcs7-signature
.pbm		image/x-portable-bitmap
.pdf		application/pdf
.pfx		application/x-pkcs12
.pgm		image/x-portable-graymap
.pko		application/vndms-pkipko
.pma		application/x-perfmon
.pmc		application/x-perfmon
.pml		application/x-perfmon
.pmr		application/x-perfmon
.pmw		application/x-perfmon
.pnm		image/x-portable-anymap
.pot		application/vnd.ms-powerpoint
.ppm		image/x-portable-pixmap
.pps		application/vnd.ms-powerpoint
.ppt		application/vnd.ms-powerpoint
.prf		application/pics-rules
.ps		application/postscript
.pub		application/x-mspublisher
.qt		video/quicktime
.ra		audio/x-pn-realaudio
.ram		audio/x-pn-realaudio
.ras		image/x-cmu-raster
.rgb		image/x-rgb
.rmi		audio/mid
.roff		application/x-troff
.rtf		application/rtf
.rtx		text/richtext
.scd		application/x-msschedule
.sct		text/scriptlet
.setpay		application/set-payment-initiation
.setreg		application/set-registration-initiation
.sh		application/x-sh
.shar		application/x-shar
.sit		application/x-stuffit
.snd		audio/basic
.spc		application/x-pkcs7-certificates
.spl		application/futuresplash
.src		application/x-wais-source
.sst		application/vndms-pkicertstore
.stl		application/vndms-pkistl
.stm		text/html
.sv4cpio	application/x-sv4cpio
.sv4crc		application/x-sv4crc
.t		application/x-troff
.tar		application/x-tar
.tcl		application/x-tcl
.tex		application/x-tex
.texi		application/x-texinfo
.texinfo	application/x-texinfo
.tgz		application/x-compressed
.tif		image/tiff
.tiff		image/tiff
.tr		application/x-troff
.trm		application/x-msterminal
.tsv		text/tab-separated-values
.txt		text/plain
.uls		text/iuls
.ustar		application/x-ustar
.vcf		text/x-vcard
.wav		audio/wav
.wcm		application/vnd.ms-works
.wdb		application/vnd.ms-works
.wks		application/vnd.ms-works
.wmf		application/x-msmetafile
.wps		application/vnd.ms-works
.wri		application/x-mswrite
.wrl		x-world/x-vrml
.wrz		x-world/x-vrml
.xaf		x-world/x-vrml
.xbm		image/x-xbitmap
.xla		application/vnd.ms-excel
.xlc		application/vnd.ms-excel
.xlm		application/vnd.ms-excel
.xls		application/vnd.ms-excel
.xlt		application/vnd.ms-excel
.xlw		application/vnd.ms-excel
.xml		text/xml
.xof		x-world/x-vrml
.xpm		image/x-xpixmap
.xsl		text/xml
.xwd		image/x-xwindowdump
.z		application/x-compress
.zip		application/x-zip-compressed
 */
	
	final public static String MIME_TEXT_HTML = "text/html";
	final public static String MIME_TEXT_TEXT = "text/plain";
	final public static String MIME_TEXT_XML = "text/xml";
	final public static String MIME_TEXT_XSL = "text/xsl";
	final public static String MIME_TEXT_JAVASCRIPT = "text/javascript";
	final public static String MIME_APPLICATION_AUTOCAD = "application/acad";
	final public static String MIME_APPLICATION_MSACCESS = "application/msaccess";
	final public static String MIME_APPLICATION_MSWORD = "application/msword";
	final public static String MIME_APPLICATION_BIN = "application/octet-stream";
	final public static String MIME_APPLICATION_PDF = "application/pdf";
	final public static String MIME_APPLICATION_MSEXCEL = "application/vnd.ms-excel";
	final public static String MIME_APPLICATION_MSPPT = "application/vnd.ms-powerpoint";
	final public static String MIME_APPLICATION_JAVASCRIPT = "application/x-javascript";
	final public static String MIME_APPLICATION_ZIP = "application/zip";
	final public static String MIME_MULTIPART_ZIP = "multipart/x-zip";
	final public static String MIME_IMAGE_GIF = "image/gif";
	final public static String MIME_IMAGE_JPG = "image/jpeg";
	final public static String MIME_IMAGE_PNG = "image/png";

	public static void replaceFileName(String path, String oldName, String newName){

		File file = new File(path);
		if(FileUtil.getFileExistence(file) && file.isDirectory()){

			File[] filslist = file.listFiles();
			for(int i = 0; i < filslist.length; i++){
				File atFile = filslist[i];
				boolean isrename = false;
				if(!atFile.isDirectory()){
					HashMap map = new HashMap();
					map.put(oldName, newName);

					String newpath = atFile.getParent() + "\\"
							+ StringUtil.replaceAll(atFile.getName(), map);
					try{
						FileUtil.FileRename(atFile, new File(newpath));
						isrename = true;
					}catch(IOException e){
						e.printStackTrace();
					}
				}
				// textarea.append(atFile.getParent()+"    "+atFile.getName()+"      isDirectory: "+atFile.isDirectory()+"       isrename: "+isrename+"\n");
			}
		}

	}

	public static void writeFile(String path, BufferedImage bufferedImage, String formatname)
			throws IOException{
		writeFile(new File(path), bufferedImage, formatname); // "jpg"
	}

	public static void writeFile(File file, BufferedImage bufferedImage, String formatname)
			throws IOException{
		ImageIO.write(bufferedImage, formatname, file);// "jpg"
	}

	public static void writeFile(String file, String data) throws IOException{
		writeFile(file, data.getBytes());
	}

	public static void writeFile(String file, byte[] data) throws IOException{
		writeFile(new File(file), data);
	}

	public static void writeFile(String file, String data, boolean wantAppend) throws IOException{
		writeFile(file, data.getBytes(), wantAppend);
	}

	public static void writeFile(String file, byte[] data, boolean wantAppend) throws IOException{
		writeFile(new File(file), data, wantAppend);
	}

	public static void writeFile(File file, String data) throws IOException{
		writeFile(file, data.getBytes());
	}

	public static void writeFile(File file, byte[] data) throws IOException{
		writeFile(file, data, false);
	}

	public static void writeFile(File file, String data, boolean wantAppend) throws IOException{
		writeFile(file, data.getBytes(), wantAppend);
	}

	public static void writeFile(File file, byte[] data, boolean wantAppend) throws IOException{
		OutputStream out = new FileOutputStream(file, wantAppend);
		out.write(data);
		out.flush();
		out.close();
	}

	public static FileOutputStream getFileOutputStream(String file) throws FileNotFoundException{
		return getFileOutputStream(file, false);
	}

	public static FileOutputStream getFileOutputStream(String file, boolean wantAppend)
			throws FileNotFoundException{
		return getFileOutputStream(new File(file), wantAppend);
	}

	public static FileOutputStream getFileOutputStream(File file) throws FileNotFoundException{
		return getFileOutputStream(file, false);
	}

	public static FileOutputStream getFileOutputStream(File file, boolean wantAppend)
			throws FileNotFoundException{
		return new FileOutputStream(file, wantAppend);
	}

	public static String readeFileToString(String filepath) throws IOException{
		return readeFileToString(new File(filepath));
	}

	public static String readeFileToString(File file) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuffer returnvalue = new StringBuffer();
		String inputLine;
		int count = 0;
		while((inputLine = in.readLine()) != null){
			if(count != 0)
				returnvalue.append(PropertyUtil.getSeparator()); // kdn바뀜
			returnvalue.append(inputLine);
			count++;
		}
		in.close();
		return returnvalue.toString();
	}

	public static byte[] readFileToByte(String filepath) throws IOException{
		return readFileToByte(new File(filepath));
	}

	public static byte[] readFileToByte(File file) throws IOException{
		InputStream input = new FileInputStream(file);
		byte[] buf = new byte[(int)file.length()];
		input.read(buf);
		// int readdata=-1;
		// int index=0;
		// while((readdata=input.read())!=-1){
		// buf[index++]=(byte)readdata;
		// }
		input.close();
		return buf;
	}

	// implements Serializable 한객체만넣을수있다 안쪽 사용된 객체도 아니면 Exception
	// java.io.NotSerializableException !
	public static void writeFile(String file, Object data) throws IOException{
		writeFile(new File(file), data);
	}

	public static void writeFile(File file, Object data) throws IOException{
		ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(file));
		obs.writeObject(data);
		obs.close();
	}

	// implements Serializable 한객체만넣을수있다 안쪽 사용된 객체도 아니면 Exception
	// java.io.NotSerializableException !
	public static Object readFileToObject(String file) throws IOException, ClassNotFoundException{
		return readFileToObject(new File(file));
	}

	public static Object readFileToObject(File file) throws IOException, ClassNotFoundException{
		ObjectInputStream obs = new ObjectInputStream(new FileInputStream(file));
		Object o = obs.readObject();
		obs.close();
		return o;
	}

	public static void writeFile(String filepath, InputStream is) throws IOException{
		writeFile(new File(filepath), is);
	}

	public static void writeFile(File file, InputStream is) throws IOException{
		OutputStream out = new FileOutputStream(file);
		int c = 0;
		byte[] buf = new byte[1024];
		while((c = is.read(buf)) != -1){
			// System.out.println(file.length());
			out.write(buf, 0, c);
			out.flush();
		}
		out.close();

	}

	public static void FileRename(String beforefile, String afterfile) throws IOException{
		FileRename(new File(beforefile), new File(afterfile));
	}

	public static void FileRename(File beforefile, File afterfile) throws IOException{
		if(!beforefile.renameTo(afterfile)){
			throw new IOException("Fail rename file" + beforefile.toString() + "  ->  "
					+ afterfile.toString());
		}
	}

	// FilenameFilter filenamefilter = new FilenameFilter() {
	// @Override
	// public boolean accept(File arg0, String filename) {
	// return filename.endsWith(".sql");
	// // return true;
	// }
	// };

	public static String[] getFileList(String path) throws IOException{
		return getFileList(new File(path));
	}

	public static String[] getFileList(File directory) throws IOException{
		if(directory.isDirectory() == false){
			throw new IOException("Not Directory" + directory.toString());
		}
		return directory.list();
	}

	public static File[] getFileListToFile(String path) throws IOException{
		return getFileListToFile(new File(path));
	}

	public static File[] getFileListToFile(File directory) throws IOException{
		if(directory.isDirectory() == false){
			throw new IOException("Not Directory" + directory.toString());
		}
		return directory.listFiles();
	}

	// filenamefilter = *good*aa.jsp
	public static File[] getFileList(String directory, String rexFilenamefilter) throws IOException{
		return getFileList(new File(directory), rexFilenamefilter);
	}

	public static File[] getFileList(File directory, final String rexFilenamefilter)
			throws IOException{
		FilenameFilter fnf = new FilenameFilter(){
			public boolean accept(File dir, String name){
				StringUtil.isFind(name,rexFilenamefilter);
				return false;
			}
		};
		return getFileList(directory, fnf);
	}

	public static File[] getFileList(String directory, FilenameFilter filenamefilter)
			throws IOException{
		return getFileList(new File(directory), filenamefilter);
	}

	public static File[] getFileList(File directory, FilenameFilter filenamefilter)
			throws IOException{
		return directory.listFiles(filenamefilter);
	}

	public static File[] getFileList(String directory, FileFilter filefilter) throws IOException{
		return getFileList(new File(directory), filefilter);
	}

	public static File[] getFileList(File directory, FileFilter filefilter) throws IOException{
		return directory.listFiles(filefilter);
	}

	//
	public static File[] getFileListOnlyFile(String directory) throws IOException{
		return getFileListOnlyFile(new File(directory));
	}

	public static File[] getFileListOnlyFile(File file) throws IOException{
		FileFilter ff = new FileFilter(){
			public boolean accept(File arg0){
				return arg0.isFile();
			}
		};
		return getFileList(file, ff);
	}

	public static File[] getFileListOnlyFolder(String directory) throws IOException{
		return getFileListOnlyFolder(new File(directory));
	}

	public static File[] getFileListOnlyFolder(File file) throws IOException{
		FileFilter ff = new FileFilter(){
			public boolean accept(File arg0){
				return arg0.isDirectory();
			}
		};
		return getFileList(file, ff);
	}

	public static ArrayList<File> getFileListOnlyFloderFullDepth(String file){
		return getFileListOnlyFloderFullDepth(new File(file));
	}

	public static ArrayList<File> getFileListOnlyFloderFullDepth(File file){
		return getFileListOnlyFloderFullDepth(file, null);
	}

	public static ArrayList<File> getFileListOnlyFloderFullDepth(String file, FileFilter filter){
		return getFileListOnlyFloderFullDepth(new File(file), filter);
	}

	public static ArrayList<File> getFileListOnlyFloderFullDepth(File file, FileFilter filter){
		ArrayList<File> flist = getFileListFullDepth(file, null);
		flist = getFileList(flist, new FileFilter(){
			public boolean accept(File pathname){
				return pathname.isDirectory();
			}
		});
		return getFileList(flist, filter);
	}

	public static ArrayList<File> getFileListOnlyFileFullDepth(String file){
		return getFileListOnlyFileFullDepth(new File(file));
	}

	public static ArrayList<File> getFileListOnlyFileFullDepth(File file){
		return getFileListOnlyFileFullDepth(file, null);
	}

	public static ArrayList<File> getFileListOnlyFileFullDepth(String file, FileFilter filter){
		return getFileListOnlyFileFullDepth(new File(file), filter);
	}

	public static ArrayList<File> getFileListOnlyFileFullDepth(File file, FileFilter filter){
		ArrayList<File> flist = getFileListFullDepth(file, null);
		flist = getFileList(flist, new FileFilter(){
			public boolean accept(File pathname){
				return pathname.isFile();
			}
		});
		return getFileList(flist, filter);
	}

	// folder , file filter accept;
	public static ArrayList<File> getFileListFullDepth(String file){
		return getFileListFullDepth(new File(file));
	}

	public static ArrayList<File> getFileListFullDepth(File file){
		return getFileListFullDepth(file, null);
	}

	public static ArrayList<File> getFileListFullDepth(String file, FileFilter filter){
		return getFileListFullDepth(new File(file), filter);
	}

	public static ArrayList<File> getFileListFullDepth(File file, FileFilter filter){
		File[] childrens = file.listFiles(filter);
		ArrayList<File> returnlist = new ArrayList<File>();
		for(int i = 0; i < childrens.length; i++){
			File children = childrens[i];

			returnlist.add(children);
			if(children.isDirectory()){
				ArrayList<File> subfiles = getFileListFullDepth(children, filter);
				for(int j = 0; j < subfiles.size(); j++){
					returnlist.add(subfiles.get(j));
				}
			}

			// else{
			// returnlist.add(children);
			// }
		}
		return returnlist;
	}

	public static File[] getFileList(File[] files, FileFilter filter){
		ArrayList<File> list = new ArrayList<File>();
		for(int i = 0; i < files.length; i++){
			if(filter != null && filter.accept(files[i])){
				list.add(files[i]);
			}else if(filter == null){
				list.add(files[i]);
			}
		}
		File[] returnfiles = new File[list.size()];
		for(int i = 0; i < list.size(); i++){
			returnfiles[i] = list.get(i);
		}
		return returnfiles;
	}

	public static ArrayList<File> getFileList(ArrayList<File> files, FileFilter filter){
		ArrayList<File> rv = new ArrayList<File>();
		for(int i = 0; i < files.size(); i++){
			if(filter != null && filter.accept(files.get(i))){
				rv.add(files.get(i));
			}else if(filter == null){
				rv.add(files.get(i));
			}
		}
		return rv;
	}

	public static void copyFile(File sourcelocation, File targetdirectory, boolean cut)
			throws IOException{
		// 디렉토리인 경우
		if(sourcelocation.isDirectory()){
			// 복사될 Directory가 없으면 만듭니다.
			if(!targetdirectory.exists()){
				targetdirectory.mkdir();
			}

			String[] children = sourcelocation.list();
			String fileListName = "";
			// System.out.println("children.length: " + children.length);

			for(int i = 0; i < children.length; i++){
				// fileListName = children[i].toString();
				// System.out.println( fileListName );
				copyFile(new File(sourcelocation, children[i]), new File(targetdirectory,
						children[i]), cut);
			}
		}else{
			// 파일인 경우
			InputStream in = new FileInputStream(sourcelocation);
			OutputStream out = new FileOutputStream(targetdirectory);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			// 파일 삭제
			if(cut){
				sourcelocation.delete();
			}
		}
	}

	public static long getFileSize(String path){
		return getFileSize(new File(path));
	}

	public static long getFileSize(File file){
		// return (new Long(file.length())).intValue();
		// int size = (new Long(file.length())).intValue();
		return file.length();
	}

	// 존재유무
	public static boolean getFileExistence(String file){
		return getFileExistence(new File(file));
	}

	public static boolean getFileExistence(File file){
		return file.exists();
	}

	public static InputStream uploadFile(URL url, File file, String parametername, String filename)
			throws Exception{
		String boundary = "--dkjsei40f9844------haha-djs8dviw--4-s-df-";
		String enter = PropertyUtil.getSeparator();
		// String parametername="file";
		// String filename="a.jpg";
		// String file="/sdcard/3df/a.jpg";
		// URL설정
		// URL url = new
		// URL("http://61.102.198.32:8081/webJSPUtil/MultipartRequest/fileupload.jsp");

		// 새로운 접속을 연다.
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		// conn.set
		// 읽기와 쓰기 모두 가능하게 설정
		conn.setDoInput(true);
		conn.setDoOutput(true);
		// 캐시를 사용하지 않게 설정
		conn.setUseCaches(false);

		// POST타입으로 설정
		conn.setRequestMethod("POST");

		// 헤더 설정
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

		// Output스트림을 열어
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.writeBytes("--" + boundary + enter);
		dos.writeBytes("Content-Disposition: form-data; name=\"" + parametername + "\";filename=\""
				+ filename + "\"" + enter);
		dos.writeBytes(enter);

		byte[] data = readFileToByte(file);
		dos.write(data, 0, data.length);
		// 버퍼사이즈를 설정하여 buffer할당
		// int bytesAvailable = fileInputStream.available();
		// int maxBufferSize = 1024;
		// int bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// byte[] buffer = new byte[bufferSize];
		// //스트림에 작성
		// int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		// while (bytesRead > 0)
		// {
		// // Upload file part(s)
		// dos.write(buffer, 0, bufferSize);
		// bytesAvailable = fileInputStream.available();
		// bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		// }
		dos.writeBytes(enter);
		dos.writeBytes("--" + boundary + "--" + enter);
		// fileInputStream.close();

		// 써진 버퍼를 stream에 출력.
		dos.flush();
		dos.close();
		// 전송. 결과를 수신.
		InputStream is = conn.getInputStream();
		return is;
	}

	// by 같은 패키지안에 있는 파일을 가져온다 
	public static String getFilePath(Class atclass, String filename){
		return atclass.getResource(filename).getFile();
		//String path = testmain.class.getResource("testmain.java").getPath();
	}

	public static boolean isExists(String path){
		return isExists(new File(path));
	}

	public static boolean isExists(File file){
		return file.exists();
	}

	public static boolean mkdirs(String path){
		return mkdirs(new File(path));
	}

	public static boolean mkdirs(File file){
		if(file.exists()){
		}else{
			file.mkdirs();
		}
		return true;
	}

	// public static String readeFileToString(String filepath) throws
	// IOException{
	// return readeFileToString(new File(filepath));
	// }
	// public static String readeFileToString(File file) throws IOException{
	// BufferedReader in = new BufferedReader(new FileReader(file));
	// String returnvalue="";
	// String inputLine;
	// int count=0;
	// while ((inputLine = in.readLine()) != null) {
	// if(count!=0)
	// returnvalue += '\n';
	// returnvalue += inputLine;
	// count++;
	// }
	// in.close();
	// return returnvalue;
	// }

	// public static void writeFile(String filepath,String info) throws
	// IOException{
	// writeFile(new File(filepath),info,false);
	// }
	// public static void writeFile(String filepath,String info,boolean
	// wantAppend) throws IOException{
	// writeFile(new File(filepath),info,wantAppend);
	// }
	// public static void writeFile(File file,String info) throws IOException{
	// writeFile(file,info,false);
	// }
	// public static void writeFile(File file,String info,boolean wantAppend)
	// throws IOException{
	// OutputStreamWriter writer = new OutputStreamWriter( new
	// FileOutputStream(file,wantAppend),"UTF-8");
	// writer.write(info);
	// writer.close();
	// }

	// finger
	/*
	 * MIME은 확장 가능하다. MIME 표준은 새로운 content-type과 또 다른 MIME 속성 값을 등록할 수 있는 방법을
	 * 정의하고 있다. MIME의 명시적인 목표 중 하나는 기존 전자우편 시스템과의 호환성이다. MIME을 지원하는 클라이언트에서 비
	 * MIME가 제대로 표시될 수 있고, 반대로 MIME을 지원하지 않는 클라이언트에서 간단한 MIME 메시지가 표시될 수 있다.
	 * Content-Type 이 헤더는 메시지의 타입과 서브타입을 나타낸다. 예를 들면 Content-Type: text/plain
	 */
	public static String getMIMEType(String file){
		return getMIMEType(new File(file));
	}

	public static String getMIMEType(File file){
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		return contentType;
	}
	
	
	//encoding을찾는다 해당 파일.
	//http://code.google.com/p/juniversalchardet/  juniversalchardet-1.0.3.jar
//	Chinese,	ISO-2022-CN,	BIG5,	EUC-TW,	GB18030,	HZ-GB-23121,	Cyrillic,	ISO-8859-5
//	KOI8-R,	WINDOWS-1251,	MACCYRILLIC,	IBM866,	IBM855,	Greek,	ISO-8859-7,	WINDOWS-1253,	Hebrew,	ISO-8859-8
//	WINDOWS-1255,	Japanese,	ISO-2022-JP,	SHIFT_JIS,	EUC-JP,	Korean,	ISO-2022-KR,	EUC-KR,	Unicode,	UTF-8
//	UTF-16BE / UTF-16LE,	UTF-32BE / UTF-32LE / X-ISO-10646-UCS-4-34121 / X-ISO-10646-UCS-4-21431,	Others,	WINDOWS-1252
    public static String findEncoding(String path) throws IOException{
    	return findEncoding(new File(path));
    }
    public static String findEncoding(File file) throws IOException{
    	byte[] buf = new byte[4096]; 
		//String fileName = args[0]; 
		java.io.FileInputStream fis = new java.io.FileInputStream(file); 

		// (1) //juniversalchardet-1.0.3.jar
		UniversalDetector detector = new UniversalDetector(null); 

		// (2) 
		int nread; 
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) { 
		detector.handleData(buf, 0, nread); 
		} 
		// (3) 
		detector.dataEnd(); 

		// (4) 
		String encoding = detector.getDetectedCharset(); 
		return encoding;
//		if (encoding != null) { 
//		System.out.println("Detected encoding = " + encoding); 
//		} else { 
//		System.out.println("No encoding detected."); 
//		} 

    }
    public static String getPath(Class c) throws IOException{
    	return c.getResource("").getPath();
    }
	
}
