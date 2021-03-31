package main.java.com.union.myWebApp;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.com.union.service.MLGraphService;



@RestController
public class MLGraphController {
    private Logger logger=LoggerFactory.getLogger(MLGraphController.class);

    @RequestMapping(value = "/server", method = RequestMethod.POST,
          consumes = MediaType.APPLICATION_JSON_VALUE)

	public String postBody(@RequestBody String input) throws Exception {
    	MLGraphService graphService;
    	
        logger.info("Server has received request to get graph must lead nodes");
    	
    	try {
        	graphService = new MLGraphService(input);
    	}
		catch (Exception e){
			return "nError: " + e.getMessage();
		}
    	return "\nResult: " + graphService.getGraphMustLead();
    }
}

