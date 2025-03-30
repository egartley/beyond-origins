from pygame import Rect

from src.engine.game_state import GameState
from src.entities.player import Player


class InGameState(GameState):

    def __init__(self, size):
        super().__init__(size)
        self.player = Player(self)

    def tick(self, delta: float):
        self.player.tick(delta)

    def render(self) -> list[Rect | None]:
        super().render()
        r = self.player.render(self.surface)
        return [r]
