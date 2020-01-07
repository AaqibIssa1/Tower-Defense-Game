package edu.coms.sr2.backend;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.coms.sr2.backend.game.Game;
import edu.coms.sr2.backend.game.GameService;
import edu.coms.sr2.backend.http.controllers.GameController;

public class GameControllerTests {
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	@Mock(name = "gameService")
	GameService mockedGameService;
	@Mock
	Game game;
	@InjectMocks
	GameController mockedGameController;
	
	@Test
	public void testGetGameId_InGame() {
		int playerId = 8;
		int gameId = 1232;
		Mockito.when(mockedGameService.getGameOfPlayer(playerId)).thenReturn(game);
		Mockito.when(game.getGameId()).thenReturn(gameId);
		
		assertEquals(gameId, mockedGameController.getGameId(playerId));
	}
	
	@Test
	public void testGetGameId_NotInGame() {
		int playerId = 8;
		Mockito.when(mockedGameService.getGameOfPlayer(playerId)).thenReturn(null);
		
		assertEquals(-1, mockedGameController.getGameId(playerId));
	}
}
