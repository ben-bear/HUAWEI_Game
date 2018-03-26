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
    	//����һ�����鱣��15�������������
    	Map<Integer, List<Integer>> virtual = new HashMap<>();
    	
    	try {	
        	
        	for(int i=1;i<16;i++){
        		List<Integer> temp  = new ArrayList<>();
        		virtual.put(i, temp);
        	}
        	
        	System.out.println();
        	//��������� ��id���ʹ���ʱ��
        		String name ;
        		String id;
        		String time;
                String encoding="utf-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//���ǵ������ʽ
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    
                   
                    while((lineTxt = bufferedReader.readLine()) != null){
                        String[] info = lineTxt.split(" ");
                        String[] preInfo = info[0].split("\t");
                        id = preInfo[0];
                        name = preInfo[1];
                        time = preInfo[2]+info[1];
                        //temptime ����ʱ��    virtualName ������ͺ�\
                        /**
                         * ����ֻ������һ���� 
                         * �ĳɶ���� tempTime = Integer.parseInt(preInfo[2].split("-")[1])*30+Integer.parseInt(preInfo[2].split("-")[2]);
                         */
                        int tempTime = (Integer.parseInt(preInfo[2].split("-")[1])-1)*30+Integer.parseInt(preInfo[2].split("-")[2]);;
                        int virtualName = Integer.parseInt(name.split("flavor")[1]);
                        
                        if(virtualName>0 && virtualName <=15){
                        	List<Integer> currentList = virtual.get(virtualName);
                        	currentList.add(tempTime);
                        	virtual.put(virtualName, currentList);
                        }
//                        System.out.println("������ţ�"+virtualName+"����ʱ�䣺"+tempTime);
                    }
                    
                    read.close();
        }else{
            System.out.println("�Ҳ���ָ�����ļ�");
        }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݳ���");
            e.printStackTrace();
        }
      return virtual;
    }
   
}