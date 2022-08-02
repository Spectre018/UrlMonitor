package com.monitor.url.UrlMonitor.Controller;


import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.url.UrlMonitor.Service.UrlService;

@RestController
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to url encoder!";
	}
	
	// Stores the the urls in the database woth initial count value as 0 along with short key
	@GetMapping("/storeurl")
	public ResponseEntity<Object> storeUrl(@RequestBody @RequestParam("url") String url) {
		if(url.length()==0) {
			return new ResponseEntity<Object>(Collections.singletonMap("Please add a valid url", url), HttpStatus.BAD_REQUEST);
		}
		urlService.storeUrl(url);
		return new ResponseEntity<Object>(Collections.singletonMap("added-url", url), HttpStatus.OK);
	}
	
	// Returns the short key for all the url if present in the database
	@GetMapping("/geturl")
	public ResponseEntity<Object> getUrl(@RequestBody @RequestParam("url") String url) {
		if(url.length()==0) {
			return new ResponseEntity<Object>(Collections.singletonMap("Please add a valid url", url), HttpStatus.BAD_REQUEST);
		}
		String res=urlService.getLongToShortUrl(url);
		if(res!=null) {
			return new ResponseEntity<Object>(Collections.singletonMap("shorturl ", res), HttpStatus.OK);
		}
			
		return new ResponseEntity<Object>(Collections.singletonMap("url not found", url), HttpStatus.BAD_REQUEST);

	}
	
	// Return the visit count for all the urls so far
	@GetMapping("/count")
	public ResponseEntity<Object> visitCount(@RequestParam("url") String url) {
		int count = urlService.count(url);;
		if(count==-1) {
			return new ResponseEntity<Object>(Collections.singletonMap("url not found", url), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Object>(Collections.singletonMap("visit count", count), HttpStatus.OK);
	}
	
	// Returns the list of all the urls with visit count using pagination  
	@GetMapping("/list")
	public ResponseEntity<Map<String, Integer>> getAllUrl(@RequestParam("page") int page, @RequestParam("size") int size){
	
		Map<String , Integer> all = UrlService.getListOfUrls(page, size);
		if(all.size()==0) {
			 return new ResponseEntity<>(all,HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(all,HttpStatus.OK);
	}
}
