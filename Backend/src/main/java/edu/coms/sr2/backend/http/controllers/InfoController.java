package edu.coms.sr2.backend.http.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.coms.sr2.backend.App;


/**
 * Controller for elements of the REST API that can be categorized as simple
 * information services
 * @author Nathan
 *
 */
@RestController
public class InfoController 
{
	@GetMapping("/version")
	public String getVersion() {
		return App.class.getPackage().getImplementationVersion();
	}
}
