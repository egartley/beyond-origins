from pygame import Rect, Surface

from src.engine.game_state import GameState
from src.engine.level import Level
from src.levels.debug import DebugLevel


class InGameState(GameState):

    def __init__(self,):
        super().__init__()
        self.level = None
        self.load_level(DebugLevel(self))

    def load_level(self, level: Level):
        self.level = level
        self.level.load()
        self.refresh_surface = True

    def tick(self, delta: float):
        self.level.tick(delta)

    def render(self, surface: Surface) -> list[Rect | None]:
        return super().render(surface) + self.level.render(surface)
