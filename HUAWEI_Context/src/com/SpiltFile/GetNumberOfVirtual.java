package com.SpiltFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNumberOfVirtual {
	public static Map<Integer, int[]> getNumberOfVirtual(Map<Integer, List<Integer>> virtualNumber){
		Map<Integer, int[]> virtualNumberContinuity = new HashMap<>();
		for(int i = 1;i<16;i++){
	    	List<Integer> mList = virtualNumber.get(i);
	    	int listLength = mList.size();
	    	if(listLength == 0){
	    		int[] temp = new int[1];
	    		virtualNumberContinuity.put(i, temp);
	    		continue;
	    	}
	    	List<Integer> dp = new ArrayList<Integer>();
	    	int count = 0;
	    	int maxNumberInList = mList.get(listLength-1);
	    	int[] temp = new int[maxNumberInList];
	    	for(int j=0;j<listLength;j++){
	    		temp[mList.get(j)-1]++;
	    	}
	    	for(int k = 1;k<maxNumberInList;k++) {
	    		temp[k] = temp[k-1] +temp[k];
	    	}
	    	virtualNumberContinuity.put(i, temp);
	    	
	    }
		return virtualNumberContinuity;
	}
}
