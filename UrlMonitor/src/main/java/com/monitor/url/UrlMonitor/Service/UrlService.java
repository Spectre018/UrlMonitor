package com.monitor.url.UrlMonitor.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class UrlService {

	static final Map<String, String> shortToLongMap = new ConcurrentHashMap<>();
	static final Map<String, Integer> shortToCount = new ConcurrentHashMap<>();
    static final Map<String, String> longToShortMap = new ConcurrentHashMap<>();
    public static final Map<String, Integer> longAndCount = new ConcurrentHashMap<>();
    static int counter = 0;

    // Encodes a URL to a shortened URL.
    public String encode(String longUrl)
	{
        if(longToShortMap.containsKey(longUrl))
            return longToShortMap.get(longUrl);
        
//        String shortUrl = convertDecimalToBase62(counter++);
        String shortUrl = convertDecimalToBase62(counter++) + "a";
        
        shortToLongMap.put(shortUrl, longUrl);
        longToShortMap.put(longUrl, shortUrl);
        shortToCount.put(shortUrl, 0);
        longAndCount.put(longUrl, 0);
        System.out.println("shortUrl = "+shortUrl);
        return shortUrl;
    }

    public int count(String longUrl) {
    	String shortUrl="";
    	if(longToShortMap.size()>0) {
    		shortUrl = longToShortMap.get(longUrl);
    	}
    	int visit = 0;
    	if(shortUrl!=null) {
    		if(shortToCount.containsKey(shortUrl)) {
    		visit = shortToCount.get(shortUrl);
    		}
    	}
    	return visit;
    	
    }
    
    // Check for a short url 
    public String getLongToShortUrl(String longUrl) {
    	if(longToShortMap.size()>0) {
    		String shortUrl = longToShortMap.get(longUrl);
        	shortToCount.put(shortUrl, shortToCount.get(shortUrl)+1);
        	longAndCount.put(longUrl, longAndCount.get(longUrl)+1);
        	return shortUrl;
    	}
    	return null;
    }
    
    
    // List all urls with visit count
    public void allUrlsAndCounts() {
    	for(Map.Entry<String, String> list : longToShortMap.entrySet()) {
    		String longUrl = list.getKey();
    		int count = shortToCount.get(list.getValue());
    		System.out.println("longUrl = "+longUrl+"  "+"count = "+count);
    	}
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
