from pygame import Rect, Surface

from src.engine.game_state import GameState
from src.engine.level import Level
from src.entities.player import Player


class DebugLevel(Level):

    def __init__(self, game_state: GameState):
        super().__init__("debug", game_state)
        self.player = Player(game_state)

    def tick(self, delta: float):
        self.player.tick(delta)

    def render(self, surface: Surface) -> list[Rect | None]:
        super().render(surface)
        r = self.player.render(surface)
        return [r]
