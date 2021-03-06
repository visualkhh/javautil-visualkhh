package khh.communication.tcp.nio.worker;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;


import khh.communication.tcp.nio.NioCommunication;
import khh.conversion.util.ConversionUtil;
import khh.debug.LogK;
import khh.filter.Filter;
import khh.listener.action.ActionListener;
import khh.std.Action;
import khh.util.Util;

abstract public class NioWorker{
	
	public static final int MODE_DISABLE 		= -1;
	public static final int MODE_FIREST_NONE 	= 0;
	public static final int MODE_FIREST_RW		= 1;//SelectionKey.OP_READ | SelectionKey.OP_WRITE;
	public static final int MODE_FIREST_W		= 2;//SelectionKey.OP_WRITE;
	public static final int MODE_FIREST_R		= 3;//SelectionKey.OP_READ;
	//public static final int MODE_ONLY_TELEGRAM	= 4;//SelectionKey.OP_READ;
	private int firestMode 						= MODE_FIREST_R;
	private SocketChannel socketChannel			= null;
	private LogK log 							= LogK.getInstance();
	private NioCommunication niocommunication 					= null;
	private int readTimeout_ms =5000;
	private SelectionKey selectionKey; 
	
	public NioWorker(int firestMode) {
	    setFirestMode(firestMode);
    }
	public NioWorker() {
    }
	
	final synchronized public void write(String data) throws IOException{
		write(data.getBytes());
	}
	final synchronized public void write(byte[] data) throws IOException{
		ByteBuffer bytebuff = ByteBuffer.allocateDirect(data.length);
		bytebuff.put(data);
		bytebuff.clear();
		write(bytebuff);
	}	
	
	//여기서 알아서 처리혀라.
	abstract public void execute(SelectionKey selectionKey) throws Exception;
	abstract public void receiveTelegram(HashMap<String,Object> telegram,SelectionKey selectionKey) throws Exception;
	
	final synchronized public int write(ByteBuffer data) throws IOException{
		return write(data,getSocketChannel());
	}
	
	final synchronized public int write(ByteBuffer data,SelectionKey selectionKey) throws IOException{
		return write(data,(SocketChannel)selectionKey.channel());
	}
	final synchronized public int write(ByteBuffer data,SocketChannel socketChannel) throws IOException{
		int write_length=0;
		try{
//		    System.out.println( log.toHexlog(data));
//		    DebugUtil.trace(ByteUtil.toByteArray(data), "---------");
//		    System.out.println( log.toHexlog(data));
			log.debug("Write Before  "+isConnected(),data);
			write_length = socketChannel.write(data);
			log.debug("Write after OK!  "+isConnected()+"  size:"+write_length);
//			log.debug("2) WriteLength "+write_length);
		}catch(IOException e){
			log.debug("Exception : Write Fail Socket=null  isConnected? -> "+isConnected());
			throw e;
		}
		return write_length;
	}
	
	
	
	final synchronized public  int read(byte[] buffer) throws IOException{
		return read(buffer,getReadTimeout());
	}
	final synchronized public  int read(byte[] buffer, int timeout_daly_ms) throws IOException{
		ByteBuffer bytebuff  = ByteBuffer.allocateDirect(buffer.length);
		bytebuff.clear();
		int r = read(bytebuff, timeout_daly_ms);
		bytebuff.clear();
		bytebuff.get(buffer);
		bytebuff.clear();
		return r;
	}
	
	final synchronized public  int read(ByteBuffer buffer) throws IOException{
		return read(buffer,getReadTimeout());
	}
	final synchronized public  int read(ByteBuffer buffer, int timeout_daly_ms) throws IOException{
		return read(buffer,timeout_daly_ms,getSocketChannel());
	}
	final synchronized public  int read(ByteBuffer buffer,SelectionKey selectionKey) throws IOException{
		return read(buffer,getReadTimeout(),selectionKey);
	}
	final synchronized public  int read(ByteBuffer buffer, int timeout_daly_ms,SelectionKey selectionKey) throws IOException{
		return read(buffer,timeout_daly_ms,(SocketChannel)selectionKey.channel());
	}
	final synchronized public  int read(ByteBuffer buffer, int timeout_daly_ms,SocketChannel socketChannel) throws IOException{
		int len = 0;
		long start_mm = System.currentTimeMillis();
		while (true){
			len = socketChannel.read(buffer);
			if(len > 0){
				if ( buffer.position() == buffer.limit() ) {
					return len;
				}
			}
			//timeout Chk
			if (Util.isTimeOver(start_mm, timeout_daly_ms)) {
				log.debug("readBlock Time-Out : readbuffer Info"+ buffer.toString()+" daly_ms:"+timeout_daly_ms);
				throw new SocketTimeoutException("readBlock Time-Out  readbuffer Info"+buffer.toString()+" daly_ms:"+timeout_daly_ms+"  isConnected:"+getSocketChannel().isConnected());
			}
			
			try{
				Thread.sleep(1);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}	
	
	
	final synchronized public  String readString() throws IOException{
		String s=null;
		ByteBuffer buffer = read();
		buffer.flip();
		return ConversionUtil.toString(buffer);
	}
	
	
	final synchronized public  ByteBuffer read() throws IOException{
		return read(getSocketChannel());
	}
	
	
//	final synchronized public  ByteBuffer readNolimit(ByteBuffer buffer) throws IOException{
//		return read(buffer,getSocketChannel());
//	}

	final synchronized public  ByteBuffer read(SocketChannel socketChannel) throws IOException{
		int len = 1;
		int oneSize=1024;
		ByteBuffer buff = ByteBuffer.allocate(oneSize);
		while(getSelectionKey().isReadable() && len>0){
			len = socketChannel.read(buff);
			if(len==oneSize){
				buff.flip();
				ByteBuffer newBuff = ByteBuffer.allocate(buff.remaining()+oneSize);
				newBuff.put(buff);
				buff = newBuff;
			}
		}
		return buff; 

	}
	
	
	
	
	
	
	
	
	
	public int getReadTimeout(){
		return readTimeout_ms;
	}
	public void setReadTimeout(int readTimeout_ms){
		this.readTimeout_ms = readTimeout_ms;
	}
	
	
	
	
	
	
	
	
	
	
	final public boolean isConnected(){
		if ( getSocketChannel() == null )
			return false;
		return getSocketChannel().isConnected()   ;//&& socket.isOpen()&&socket.finishConnect();
	}
	final public void setSelectionKey(SelectionKey selectionkey){
		this.selectionKey = selectionkey;
	}
	
	public SelectionKey getSelectionKey() {
		return selectionKey;
	}
	final public void setSocketChannel(SocketChannel socketChannel){
		this.socketChannel = socketChannel;
	}
	final public SocketChannel getSocketChannel(){
		return this.socketChannel;
	}
	final public int getFirestMode(){
		return firestMode;
	}
	final public void setFirestMode(int firestMode){
		this.firestMode = firestMode;
	}
	@Override
	protected void finalize() throws Throwable{
		getSocketChannel().close();
		super.finalize();
	}
	
	
	final synchronized public void close(){
		close(getSocketChannel());
	}
	final synchronized public void close(SocketChannel socket){
		try{
			if(socket!=null){
				socket.close();
				log.debug("socket close: "+socket);
			}
		}catch (IOException e1){
		}
	}
	public NioCommunication getNioCommunication(){
		return niocommunication;
	}

	public void setNioCommunication(NioCommunication niocommunication){
		this.niocommunication = niocommunication;
	}
	
	public void sendActionEventToListener(Action event){
		getNioCommunication().sendActionEventToListener(event);
	}
	public void sendActionEventToListener(Filter<ActionListener> filter,Action event){
		getNioCommunication().sendActionEventToListener(filter,event);
	}
	
}
