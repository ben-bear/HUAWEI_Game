package com.SpiltFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;
import javax.xml.crypto.Data;

import org.omg.CORBA.portable.ValueBase;


public class ReadTxt {
    public static Map<Integer, List<Integer>> readTxtFile(String filePath){
    	//开辟一个数组保存15个虚拟机的数量
    	Map<Integer, List<Integer>> virtual = new HashMap<>();
    	
    	try {	
        	
        	for(int i=1;i<16;i++){
        		List<Integer> temp  = new ArrayList<>();
        		virtual.put(i, temp);
        	}
        	
        	System.out.println();
        	//虚拟机名称 ，id，和创建时间
        		String name ;
        		String id;
        		String time;
                String encoding="utf-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    
                   
                    while((lineTxt = bufferedReader.readLine()) != null){
                        String[] info = lineTxt.split(" ");
                        String[] preInfo = info[0].split("\t");
                        id = preInfo[0];
                        name = preInfo[1];
                        time = preInfo[2]+info[1];
                        //temptime 创建时间    virtualName 虚拟机型号\
                        /**
                         * 这里只处理了一个月 
                         * 改成多个月 tempTime = Integer.parseInt(preInfo[2].split("-")[1])*30+Integer.parseInt(preInfo[2].split("-")[2]);
                         */
                        int tempTime = (Integer.parseInt(preInfo[2].split("-")[1])-1)*30+Integer.parseInt(preInfo[2].split("-")[2]);;
                        int virtualName = Integer.parseInt(name.split("flavor")[1]);
                        
                        if(virtualName>0 && virtualName <=15){
                        	List<Integer> currentList = virtual.get(virtualName);
                        	currentList.add(tempTime);
                        	virtual.put(virtualName, currentList);
                        }
//                        System.out.println("机器编号："+virtualName+"创建时间："+tempTime);
                    }
                    
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
      return virtual;
    }
   
}