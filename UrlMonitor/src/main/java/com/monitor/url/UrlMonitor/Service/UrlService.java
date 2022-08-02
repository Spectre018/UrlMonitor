package com.monitor.url.UrlMonitor.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.monitor.url.UrlMonitor.helper.UrlCount;

@Service
public class UrlService {

    public static final Map<String, String> longToShortMap = new ConcurrentHashMap<>();
    static final Map<String, Integer> longAndCount = new ConcurrentHashMap<>();
    static int counter = 0;

    // Encodes a URL to a shortened URL.
    public String storeUrl(String longUrl)
	{
        if(longToShortMap.containsKey(longUrl))
        {
            return longToShortMap.get(longUrl);
        }
        String shortUrl = convertDecimalToBase62(counter++) + "a";
        longToShortMap.put(longUrl, shortUrl);
        longAndCount.put(longUrl, 0);
        return shortUrl;
    }

    
    // Returns the visit count of the url
    public int count(String longUrl) {
    	int visit = -1;
    	if(longAndCount.containsKey(longUrl)) {
    		visit = longAndCount.get(longUrl);
    		return visit;
    	}
    	return visit;
    }
    
    // Check for a short url 
    public String getLongToShortUrl(String longUrl) {
    	String shortKey = null;
    	if(longToShortMap.containsKey(longUrl)) {
    		shortKey = longToShortMap.get(longUrl);
    		longAndCount.put(longUrl, longAndCount.get(longUrl)+1);
    	}
    	return shortKey;
    }
    
    
    // Returns List all urls with visit count
    public static Map<String, Integer> getListOfUrls(int page, int size)
    {
    	// collection to collect urls and its current visit count to implement pagination
    	List<UrlCount> longCountList = Collections.synchronizedList(new ArrayList<UrlCount>());
    	for(Map.Entry<String, Integer> value : longAndCount.entrySet()) {
    		longCountList.add(new UrlCount(value.getKey(), value.getValue()));
    	}

    	int longCountList_size = longCountList.size();
    	int countUptoLastPage = size*(page-1);
    	boolean lastPage = longCountList_size-countUptoLastPage<=size;
    	boolean outOfBound = countUptoLastPage > longCountList_size;
    	int start = size*(page-1);
    	int end = longCountList_size%size;
    	Map<String, Integer> listMap = new ConcurrentHashMap<>();
    	if(outOfBound) {  // when url index is out of bound
    		System.out.println("outofbound");
    		return listMap;
    	}
    	if(lastPage) {    // When the url is in the last page
    		start = size*(page-1);
    		end = longCountList_size;
    		for(int i=start;i<end;i++) {
    			listMap.put(longCountList.get(i).url, longCountList.get(i).count);
    		}
    		return listMap;
    	}
    	start = size*(page-1);
    	end = size*(page-1) + size;
    	
    	for(int i=start; i<end;i++) {
    		listMap.put(longCountList.get(i).url, longCountList.get(i).count);
    	}
    	return listMap;
    	
    	

    }
    
    
    // Base 62 converter.
    private String convertDecimalToBase62(int n)
    {
        final char[] BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        
		while(n > 0)
		{
            sb.append(BASE62[n % 62]);
            n /= 62;
        }
        return sb.reverse().toString();
    }

    

}
