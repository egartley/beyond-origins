from src.engine.game_state import GameState
from src.engine.level import Level
from src.entities.player import Player


class DebugLevel(Level):

    def __init__(self, game_state: GameState):
        super().__init__("debug", game_state)
        self.player = Player(game_state)
        self.player_start_x = 480
        self.player_start_y = 830
