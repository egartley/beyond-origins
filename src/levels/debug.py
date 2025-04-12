from src.engine.game_state import GameState
from src.engine.level import Level
from src.entities.player import Player


class DebugLevel(Level):

    def __init__(self, game_state: GameState):
        super().__init__("debug", game_state)
        self.set_control_entity(Player(game_state))
        self.start_rel_x = 300
        self.start_rel_y = 300
