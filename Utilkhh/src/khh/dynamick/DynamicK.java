package khh.dynamick;

import java.io.File;
import java.util.ArrayList;

import khh.debug.LogK;
import khh.std.adapter.AdapterMap;
import khh.xml.XMLparser;
import khh.std.Standard;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DynamicK {
	private AdapterMap<String,File> configlist 			= null;
	private AdapterMap<String, DynamicClass> classlist 	= null;
	private String rootElementName						= "/";
	private LogK log = LogK.getInstance();
	public DynamicK() {
        configlist      = new AdapterMap<String, File>();
        classlist       = new AdapterMap<String, DynamicClass>();
    }
    
    
    
    public void addConfigFile(String realpath) throws Exception{
        if(realpath!=null){
            addConfigFile(new File(realpath));
        }
    }
    public void addConfigFile(File file) throws Exception{
        if(file!=null && file.exists() && file.isFile()){
            configlist.add(file.getAbsolutePath(),file);
        }
    }
    
    
    
    public void setting() {
        if(configlist!=null && configlist.size()>0){
            setConfig();
        }
    }
    
    
    
    
    
    

  public String getRootElementName() {
		return rootElementName;
	}



	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}







private String classxpath="/class";
  private void setConfig() {
      
      //Global settingConfig
      // 우선 전체적인 config파일에서  뭐든 class파일을 불러서 넣는다.
      for (int configCnt = 0; configCnt < configlist.size(); configCnt++) {
          try{
              log.debug("ConfigFile["+configCnt+"]   size("+configlist.size()+")");
              XMLparser parser = new XMLparser(configlist.get(configCnt));
              
              //global class
              //String contextpath = getRootElementName()+classxpath+"[@id]";
              String contextpath = getRootElementName()+classxpath;
              
              NodeList nodes = parser.getNodes(contextpath);
              log.debug("Class Global Element  xpath:"+contextpath+ "   size:"+nodes.getLength());
              
              
              for (int classCnt = 0; classCnt < nodes.getLength(); classCnt++) {
            	  Node atClassNode = nodes.item(classCnt);
                  DynamicClass dynamicClass = new DynamicClass();
                  dynamicClass.setNode(nodes.item(classCnt));
                  classlist.add(dynamicClass.getAttribute("id"), dynamicClass);
              }
              parser.close();
          }catch (Exception e) {
                  log.error("Class Global Element Error ",e);
          }
      }
      
      
      //재귀적 DynamicClass 생성
      for (int i = 0; i < classlist.size(); i++){
          try{
              getClass(classlist.get(i));
          }catch (Exception e) {
            log.error("재귀적 DynamicClass 생성 Error ",e);
          }
      }
      
      
      /*
      //Class 생성
      for (int i = 0; i < classlist.size(); i++){
          try{
              String id = classlist.get(i).getAttribute("id");
              String classpath = classlist.get(i).getAttribute("classpath");
             
              classlist.get(i).newClassObject();
			//getClass(classlist.get(i));
              
          }catch (Exception e) {
            log.error("Class 생성 Error ",e);
          }finally{
          }
      }
      
      
      
      //methoad 실행
      for (int i = 0; i < classlist.size(); i++){
          try{
              String id = classlist.get(i).getAttribute("id");
              String classpath = classlist.get(i).getAttribute("classpath");
             
              classlist.get(i).executeMethoad();
			//getClass(classlist.get(i));
              
          }catch (Exception e) {
            log.error("methoad 실행 Error ",e);
          }finally{
          }
      }
      
        log.debug(classlist.size());
  */
  }

  
  public AdapterMap<String, DynamicClass> getType(String typeValue){
	  return getEqualsAttribute("type",typeValue);
  }
  public AdapterMap<String, DynamicClass> getEqualsAttribute(String attributeName, String attributeValue){
	  AdapterMap<String, DynamicClass> list = new AdapterMap<String, DynamicClass>();
	  AdapterMap<String, DynamicClass> dclassList = getClasslist();
	  for (int i = 0; i < dclassList.size(); i++) {
		  try{
			  String key = dclassList.getKey(i);
			  DynamicClass dclass = dclassList.get(i); 
			  if(attributeValue.equals(dclass.getAttribute(attributeName))){
					list.add(key, dclass);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
	  }
	  
	  return list;
  }

  public void newClassObject(){
	  newClassObject(getClasslist());
  }
  public void newClassObject(AdapterMap<String, DynamicClass> classlist){
      for (int i = 0; i < classlist.size(); i++){
          try{
              //String id = classlist.get(i).getAttribute("id");
              //String classpath = classlist.get(i).getAttribute("classpath");
              newClassObject(classlist.get(i));
          }catch (Exception e) {
            log.error("Class 생성 Error ",e);
          }finally{
          }
      }
      
  }
  
  private void newClassObject(DynamicClass dclass){
	  try{
		  dclass.newClassObject();
      }catch (Exception e) {
        log.error("methoad 실행 Error ",e);
      }finally{
      }
  }
  
  public void executeMethod(){
	  executeMethod(getClasslist());
  }
  public void executeMethod(AdapterMap<String, DynamicClass> classlist){
      for (int i = 0; i < classlist.size(); i++){
          try{
              //String id = classlist.get(i).getAttribute("id");
              //String classpath = classlist.get(i).getAttribute("classpath");
              executeMethod(classlist.get(i));
          }catch (Exception e) {
            log.error("methoad List 실행 Error ("+i+"번째)",e);
          }finally{
          }
      }
  }
  private void executeMethod(DynamicClass dclass){
	  try{
		  dclass.executeMethod();
      }catch (Exception e) {
        log.error("methoad 실행 Error ",e);
      }finally{
      }
  }
  
  
  

    public void getClass(DynamicClass dclass) throws Exception {
        Node node = dclass.getNode();
        NodeList list = node.getChildNodes();    
        
        ArrayList<DynamicClass> constructorParameter = new ArrayList<DynamicClass>();
        AdapterMap<Node, ArrayList<DynamicClass>> methoadParameter = new AdapterMap<Node, ArrayList<DynamicClass>>();
//        AdapterMap<String, Standard<String, ArrayList<DynamicClass>> > methoadParameter = new AdapterMap<String, Standard<String, ArrayList<DynamicClass>> >();
        
          for(int i = 0 ; i<list.getLength() ; i++) {
              
            Node classSubNode = list.item(i);
            //Element만. constructor, methoad
            if( classSubNode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
            }else{
                continue;
            }
                    
            
            
             if("constructor".equals(classSubNode.getNodeName()) ||  "constructor"==classSubNode.getNodeName()){
                NodeList conSubList = classSubNode.getChildNodes();
                for(int y = 0; y < conSubList.getLength(); y++){
                    Node conSubNode = conSubList.item(y);
                    if( conSubNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE ){
                    }else{
                        continue;
                    }
                    
                    
                    DynamicClass sdclass = new DynamicClass(conSubNode);
                    String ref = sdclass.getAttribute("ref");
                    
                    
                    if(ref!=null && ref.length()>0){
                        sdclass = classlist.get(ref);
                    }
                    constructorParameter.add(sdclass);
                    getClass(sdclass);
                }
                 
                 
             }else if("method".equals(classSubNode.getNodeName()) ||  "method"==classSubNode.getNodeName()){
                 ArrayList<DynamicClass> methodSubClassList= new ArrayList<DynamicClass>();
                 NodeList methodSubList = classSubNode.getChildNodes();
                 for(int y = 0; y < methodSubList.getLength(); y++){
                     Node methodSubNode = methodSubList.item(y);
//                     log.debug(methoadSubNode.getNodeName()+"     ("+methoadSubNode.getNodeType()+")");
                     if( methodSubNode.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE){
                     }else{
                         continue;
                     }
                     DynamicClass sdclass = new DynamicClass(methodSubNode);
                     String ref = sdclass.getAttribute("ref");
                     if(ref!=null && ref.length()>0){
                         sdclass = classlist.get(ref);
                     }
                     methodSubClassList.add(sdclass);
                     getClass(sdclass);
                 }
                 //중요 methoad도 ID를부여
//                 String methoadId = ((Attr)classSubNode.getAttributes().getNamedItem("id")).getValue();
//                 String methoadName = ((Attr)classSubNode.getAttributes().getNamedItem("name")).getValue();
//                 methoadParameter.add(methoadId, new Standard<String,ArrayList<DynamicClass>>(methoadName, methoadSubClassList));
                 methoadParameter.add(classSubNode, methodSubClassList);
                 
                 
             }
          }
          
          dclass.setConstructorParameter(constructorParameter);
          dclass.setMethodParameter(methoadParameter);
        
//        return null;
    }
  
  
  
  
  
  
  
  
//    
//    private String classxpath="/dynamick/class";
//    private void setConfig() {
//        
//        //Global settingConfig
//        // 우선 전체적인 config파일에서  뭐든 class파일을 불러서 넣는다.
//        for (int i = 0; i < configlist.size(); i++) {
//            try{
//                log.debug("ConfigFile["+i+"]   size("+configlist.size()+")");
//                XMLparser parser = new XMLparser(configlist.get(i));
//                
//                //global class
//                int classsize = parser.getInt("count("+classxpath+")");
//                log.debug("Class Global Element size : "+classsize+" path:"+classxpath);
//                for (int j = 1; j <= classsize; j++) {
//                    String contextpath      =   classxpath+"["+j+"]";
//                    //String constructorpath  =   classxpath+"["+j+"]/constructor";
//                    String id               =   parser.getString(contextpath+"/@id");
//                    String classpath        =   parser.getString(contextpath+"/@classpath");
//                    String ref              =   parser.getString(contextpath+"/@ref");
//                    
//                    DynamicClass dynamicClass = new DynamicClass();
//                    dynamicClass.setId(id);
//                    dynamicClass.setClassPath(classpath);
//                    dynamicClass.setRef(ref);
//                    classlist.add(id, dynamicClass);
//                }
//            }catch (Exception e) {
//                try {
//                    log.error("Class Global Element Error ",e);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//        
//        
//        // 
//        for (int i = 0; i < configlist.size(); i++) {
//            try{
//                log.debug("ConfigFile["+i+"]   size("+configlist.size()+")");
//                XMLparser parser = new XMLparser(configlist.get(i));
//                
//                //global class
//                int classsize = parser.getInt("count("+classxpath+")");
//                log.debug("Class Element size : "+classsize+" path:"+classxpath);
//                for (int j = 1; j <= classsize; j++) {
//                    String contextpath      =   classxpath+"["+j+"]";
//                    String constructorpath  =   classxpath+"["+j+"]/constructor";
//                    String methoadpath      =   classxpath+"["+j+"]/methoad";
//                    int    constructorsize  =   parser.getInt("count("+constructorpath+")");
//                    int    methoadsize      =   parser.getInt("count("+methoadpath+")");
//                    String id               =   parser.getString(contextpath+"/@id");
//                    String classpath        =   parser.getString(contextpath+"/@classpath");
//                    String ref              =   parser.getString(contextpath+"/@ref");
//                    
//                    DynamicClass classAt = classlist.get(id);
//                    
//                    //Constructor
//                    for (int c = 1; c <= constructorsize; c++) {
//                        String c_contextpath  =   constructorpath+"["+c+"]/class";
//                        int    c_class_size   =   parser.getInt("count("+c_contextpath+")");
//                        log.debug("constructor Class Element size : "+constructorsize+" path:"+c_contextpath+"    sub_class_size("+c_class_size+")");
//                        
//                        //Constructor sub Class
//                        ArrayList<DynamicClass> constructorParameter = new ArrayList<DynamicClass>();
//                        for (int cc = 1; cc <= c_class_size; cc++) {
//                            String cc_id               =   parser.getString(c_contextpath+"/@id");
//                            String cc_classpath        =   parser.getString(c_contextpath+"/@classpath");
//                            String cc_ref              =   parser.getString(c_contextpath+"/@ref");
//                            
//                            DynamicClass dynamicClass = null;
//                            if(cc_ref==null){
//                                dynamicClass = new DynamicClass();
//                                dynamicClass.setId(cc_id);
//                                dynamicClass.setClassPath(cc_classpath);
//                                dynamicClass.setRef(cc_ref);
//                            }else{
//                                dynamicClass = classlist.get(cc_ref);
//                            }
//                            constructorParameter.add(dynamicClass);
//                        }
//                        classAt.setConstructorParameter(constructorParameter);
//                    }
//                    
//                    
//                    
//                    //methoad
//                    AdapterMap<String, ArrayList<DynamicClass>> methoads = new AdapterMap<String, ArrayList<DynamicClass>>(); 
//                    for (int m = 1; m <= methoadsize; m++) {
//                        String m_name         =   parser.getString(methoadpath+"["+m+"]/@name");
//                        String m_contextpath  =   methoadpath+"["+m+"]/class";
//                        int    m_class_size   =   parser.getInt("count("+m_contextpath+")");
//                        log.debug("methoads Class Element size : "+m_class_size+" path:"+m_contextpath+"     m_class_size("+m_class_size+")");
//                        
//                        //Constructor sub Class
//                        ArrayList<DynamicClass> methoadParameter = new ArrayList<DynamicClass>();
//                        for (int cc = 1; cc <= m_class_size; cc++) {
//                            String cc_id               =   parser.getString(m_contextpath+"/@id");
//                            String cc_classpath        =   parser.getString(m_contextpath+"/@classpath");
//                            String cc_ref              =   parser.getString(m_contextpath+"/@ref");
//                            
//                            DynamicClass dynamicClass = null;
//                            if(cc_ref==null){
//                                dynamicClass = new DynamicClass();
//                                dynamicClass.setId(cc_id);
//                                dynamicClass.setClassPath(cc_classpath);
//                                dynamicClass.setRef(cc_ref);
//                            }else{
//                                dynamicClass = classlist.get(cc_ref);
//                            }
//                            methoadParameter.add(dynamicClass);
//                        }
//                        methoads.add(m_name, methoadParameter);
//                    }
//                    classAt.setMethoadParameter(methoads);
//                    
//                    
//                    
//                    
//
//                }
//            }catch (Exception e) {
//                try {
//                    log.error("sub  Class Error ",e);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//    }




    public AdapterMap<String, DynamicClass> getClasslist() {
        return classlist;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
