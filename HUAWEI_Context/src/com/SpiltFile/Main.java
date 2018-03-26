package com.SpiltFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	   
    public static void main(String argv[]){
        String filePath = "C://Users/Administrator/Desktop/�����ĵ�/��ϰ����/data_2015_1.txt";
    //    String filePath =	"C://Users/Administrator/Desktop/test.txt";
        //Ԥ������
        int MAXIMUM = 0;
        int[] mCpu ={1,1,1,2,2,2,4,4,4,8,8,8,16,16,16};
        int[] mMem ={1,2,4,2,4,8,4,8,16,8,16,32,16,32,64};
        
        Map<Integer, List<Integer>> virtualNumber = ReadTxt.readTxtFile(filePath);
        /**
         * virtualNumberContinuity:�����ĸ���ʱ��ĸ����������
         * coeffientMap:ģ���е�ϵ��  x(t) = a[0] + a[1]*x + a[2]*x^2 + a[3]*x^3   a[0]a[1]a[2]a[3]
         */
        Map<Integer, int[]> virtualNumberContinuity = GetNumberOfVirtual.getNumberOfVirtual(virtualNumber);  
        Map<Integer, double[]> coeffientMap = new HashMap<>(); 
        //ÿһ�ֻ��͵�ƽ����
        Map<Integer, Double> predictAverage = new HashMap<>();
        //ÿ�����������
        Map<Integer, Integer> predict = new HashMap<>();
        //����������������
        List<Map<Integer, Integer>> serverPredict = new ArrayList<>();
        
        
        //���ϵ��
        for(Integer key:virtualNumberContinuity.keySet()) {
//        	��С����ʧ��
//			int[] x = new int[virtualNumberContinuity.get(key).length];
//        	//Ĭ�ϴӵ�һ�쿪ʼ
//        	for(int i = 0;i<virtualNumberContinuity.get(key).length;i++) {
//        		x[i] = i + 1;
//        	}
//        	int[] y = virtualNumberContinuity.get(key);
//        	System.out.println(Arrays.toString(virtualNumberContinuity.get(key)));
//        	double[] x_double = IntToDouble.IntToDouble(x);
//        	double[] y_double = IntToDouble.IntToDouble(y);
//        	//���ϵ��
//        	double[] coeffient = CurveFitting.MultiLine(x_double, y_double, y.length, 2);
//        	coeffientMap.put(key, coeffient);
//        	System.out.println("��"+key+"���������ģ��:"+coeffient[0]+"+"+coeffient[1]+"x+"+coeffient[2]+"x^2");
        	int sum = 0;
        	for(int i = 0;i<virtualNumberContinuity.get(key).length;i++) {
        		sum += virtualNumberContinuity.get(key)[i];
        	}
        	double average = sum/virtualNumberContinuity.get(key).length;
        	predictAverage.put(key, average);
        }
        //��ȡ����CPU �ڴ��С
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        int cpu = Integer.parseInt(read);
        read = scanner.nextLine();
        int memery = Integer.parseInt(read);
        System.out.println(cpu);
        //��ȡ��ʼԤ��ʱ��
        int predictStart =80;
        int predictEnd = 90;
//        for(int i : coeffientMap.keySet()){
//        	double[] coeffient = coeffientMap.get(i);
//        	//maxNumber Ԥ��ʱ�����������    minNumber ��С����
//        	double minNumber = coeffient[0] + coeffient[1]*predictStart 
//        			+ coeffient[2]*Math.pow(predictStart, 2);
//        	double maxNumber = coeffient[0] + coeffient[1]*predictEnd 
//        			+ coeffient[2]*Math.pow(predictEnd, 2);
//        	int predictNum = (int)Math.ceil(maxNumber - minNumber);
//        	predict.put(i, predictNum);
//        	MAXIMUM += predictNum;
//        }
	    for(int i : predictAverage.keySet()){
	    	//maxNumber Ԥ��ʱ�����������    minNumber ��С����
	    	double minNumber = predictAverage.get(i) * predictStart ;
	    	double maxNumber = predictAverage.get(i) * predictEnd ;
	    	int predictNum = (int)Math.ceil(maxNumber - minNumber);
	    	predict.put(i, predictNum);
	    	MAXIMUM += predictNum;
	    }
        System.out.println("���������Ԥ������Ϊ��"+MAXIMUM);
        //��ӡԤ����
        for(int i : predict.keySet()){
        	System.out.println("flavor"+i+" "+predict.get(i));
        }
        
        //����ѡ��ҪԤ����Դ���Ż�γ��,����Ϊcpu
        int server = 1;
        //��ʼ��һ�������������ʣ����Դ
        int rest = 0;
        boolean isAsk = true;
        int index = 0;
        while(isAsk){
        	index++;
//        	System.out.println("��"+index+"���������������");
        	Map<Integer, Integer> bag = new HashMap<>();
        	isAsk = false;
        	rest = cpu;
        	for(int current = predict.size(); current > 0 ; current--){
        		//��ǰλ�����������
        		int var = predict.get(current);
//        		System.out.println("��ǰʣ��"+rest+" "+current+"�������ʱ��"+var+"̨");
        		//����������������0
        		if (var > 0) {
        			for(int k = 1; k <= var; k++){
        				if(k == var && k * mCpu[current-1] <= rest){
        					bag.put(current, k);
        					rest = rest - mCpu[current-1]*k;
        					predict.put(current, predict.get(current)-k);
//        					System.out.println(current+"�ͺ�ȫ�����á�"+"ʣ�ࣺ"+predict.get(current)+"ʣ�¿ռ䣺"+rest);
   
        				}
        				//�����ǰ�����������ʣ����Դ���ڵ��ڵ�ǰ���������Դʱ �����
        				else if(k * mCpu[current-1] > rest){
        					//��ǰ��ʣ������������������������
        					isAsk = true;
        					//������װ��һ̨ʱ
        					if(k != 1){
        						bag.put(current, k-1);
            					rest = rest - mCpu[current-1]*(k-1);
            					predict.put(current, predict.get(current)-k+1);
//            					System.out.println("ʣ�ࣺ"+predict.get(current)+"ʣ�¿ռ䣺"+rest);
        					}
        					break;
        				}
        			}
				}
        		//��⵽ʣ��ռ�Ϊ0ʱ���ٴ����������
        		if(rest == 0 ){
        			break;
        		}
        		
        	}
        	
        	serverPredict.add(bag);
        	
        }
        System.out.println(index);
        for(int i = 0;i < index;i++){
        	StringBuffer stringBuffer = new StringBuffer();
        	for(int num:serverPredict.get(i).keySet()){
        		stringBuffer.append(" "+"flavor"+num+" "+serverPredict.get(i).get(num));
        	}
        	System.out.println((i+1)+stringBuffer.toString());
        }
        
        
        
    }
}
