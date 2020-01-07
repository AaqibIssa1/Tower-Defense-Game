package edu.coms.sr2.backend.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.coms.sr2.backend.game.Game;
import edu.coms.sr2.backend.game.GameService;

@RestController
public class GameController {

	@Autowired
	GameService gameService;
	
	@GetMapping("/games/player/{id}")
	public int getGameId(@PathVariable int id) {
		Game game = gameService.getGameOfPlayer(id);
		return game != null? game.getGameId() : -1;
	}
}
