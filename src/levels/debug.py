from pygame import Rect, Surface

from src.engine.game_state import GameState
from src.engine.level import Level
from src.entities.player import Player


class DebugLevel(Level):

    def __init__(self, game_state: GameState):
        super().__init__("debug", game_state)
        self.player = Player(game_state)
        self.player.set_position(100, 100)

    def tick(self, delta: float):
        super().tick(delta)
        self.player.tick(delta)

    def render(self, surface: Surface):
        super().render(surface)
        self.player.render(surface)
