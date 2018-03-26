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
        String filePath = "C://Users/Administrator/Desktop/初赛文档/练习数据/data_2015_1.txt";
    //    String filePath =	"C://Users/Administrator/Desktop/test.txt";
        //预测数量
        int MAXIMUM = 0;
        int[] mCpu ={1,1,1,2,2,2,4,4,4,8,8,8,16,16,16};
        int[] mMem ={1,2,4,2,4,8,4,8,16,8,16,32,16,32,64};
        
        Map<Integer, List<Integer>> virtualNumber = ReadTxt.readTxtFile(filePath);
        /**
         * virtualNumberContinuity:连续的各个时间的该虚拟机数量
         * coeffientMap:模型中的系数  x(t) = a[0] + a[1]*x + a[2]*x^2 + a[3]*x^3   a[0]a[1]a[2]a[3]
         */
        Map<Integer, int[]> virtualNumberContinuity = GetNumberOfVirtual.getNumberOfVirtual(virtualNumber);  
        Map<Integer, double[]> coeffientMap = new HashMap<>(); 
        //每一种机型的平均数
        Map<Integer, Double> predictAverage = new HashMap<>();
        //每种虚拟机数量
        Map<Integer, Integer> predict = new HashMap<>();
        //物理服务器放置情况
        List<Map<Integer, Integer>> serverPredict = new ArrayList<>();
        
        
        //求解系数
        for(Integer key:virtualNumberContinuity.keySet()) {
//        	最小二乘失败
//			int[] x = new int[virtualNumberContinuity.get(key).length];
//        	//默认从第一天开始
//        	for(int i = 0;i<virtualNumberContinuity.get(key).length;i++) {
//        		x[i] = i + 1;
//        	}
//        	int[] y = virtualNumberContinuity.get(key);
//        	System.out.println(Arrays.toString(virtualNumberContinuity.get(key)));
//        	double[] x_double = IntToDouble.IntToDouble(x);
//        	double[] y_double = IntToDouble.IntToDouble(y);
//        	//求解系数
//        	double[] coeffient = CurveFitting.MultiLine(x_double, y_double, y.length, 2);
//        	coeffientMap.put(key, coeffient);
//        	System.out.println("第"+key+"个虚拟机的模型:"+coeffient[0]+"+"+coeffient[1]+"x+"+coeffient[2]+"x^2");
        	int sum = 0;
        	for(int i = 0;i<virtualNumberContinuity.get(key).length;i++) {
        		sum += virtualNumberContinuity.get(key)[i];
        	}
        	double average = sum/virtualNumberContinuity.get(key).length;
        	predictAverage.put(key, average);
        }
        //读取物理CPU 内存大小
        Scanner scanner = new Scanner(System.in);
        String read = scanner.nextLine();
        int cpu = Integer.parseInt(read);
        read = scanner.nextLine();
        int memery = Integer.parseInt(read);
        System.out.println(cpu);
        //读取开始预测时间
        int predictStart =80;
        int predictEnd = 90;
//        for(int i : coeffientMap.keySet()){
//        	double[] coeffient = coeffientMap.get(i);
//        	//maxNumber 预测时间内最大数量    minNumber 最小数量
//        	double minNumber = coeffient[0] + coeffient[1]*predictStart 
//        			+ coeffient[2]*Math.pow(predictStart, 2);
//        	double maxNumber = coeffient[0] + coeffient[1]*predictEnd 
//        			+ coeffient[2]*Math.pow(predictEnd, 2);
//        	int predictNum = (int)Math.ceil(maxNumber - minNumber);
//        	predict.put(i, predictNum);
//        	MAXIMUM += predictNum;
//        }
	    for(int i : predictAverage.keySet()){
	    	//maxNumber 预测时间内最大数量    minNumber 最小数量
	    	double minNumber = predictAverage.get(i) * predictStart ;
	    	double maxNumber = predictAverage.get(i) * predictEnd ;
	    	int predictNum = (int)Math.ceil(maxNumber - minNumber);
	    	predict.put(i, predictNum);
	    	MAXIMUM += predictNum;
	    }
        System.out.println("所有虚拟机预测数量为："+MAXIMUM);
        //打印预测结果
        for(int i : predict.keySet()){
        	System.out.println("flavor"+i+" "+predict.get(i));
        }
        
        //这里选择要预测资源的优化纬度,假如为cpu
        int server = 1;
        //初始化一个物理服务器的剩余资源
        int rest = 0;
        boolean isAsk = true;
        int index = 0;
        while(isAsk){
        	index++;
//        	System.out.println("第"+index+"次申请物理服务器");
        	Map<Integer, Integer> bag = new HashMap<>();
        	isAsk = false;
        	rest = cpu;
        	for(int current = predict.size(); current > 0 ; current--){
        		//当前位置虚拟机数量
        		int var = predict.get(current);
//        		System.out.println("当前剩余"+rest+" "+current+"虚拟机此时有"+var+"台");
        		//如果虚拟机数量大于0
        		if (var > 0) {
        			for(int k = 1; k <= var; k++){
        				if(k == var && k * mCpu[current-1] <= rest){
        					bag.put(current, k);
        					rest = rest - mCpu[current-1]*k;
        					predict.put(current, predict.get(current)-k);
//        					System.out.println(current+"型号全部放置。"+"剩余："+predict.get(current)+"剩下空间："+rest);
   
        				}
        				//如果当前物理服务器的剩余资源大于等于当前虚拟机的资源时 则放入
        				else if(k * mCpu[current-1] > rest){
        					//当前有剩余则继续请求申请物理服务器
        					isAsk = true;
        					//至少能装下一台时
        					if(k != 1){
        						bag.put(current, k-1);
            					rest = rest - mCpu[current-1]*(k-1);
            					predict.put(current, predict.get(current)-k+1);
//            					System.out.println("剩余："+predict.get(current)+"剩下空间："+rest);
        					}
        					break;
        				}
        			}
				}
        		//检测到剩余空间为0时，再次申请物理机
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
