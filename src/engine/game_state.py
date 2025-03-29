from pygame import Surface
from pygame.rect import Rect

from src.engine.event import EventStore
from src.engine.key import KeyStore


class GameState:

    def __init__(self, size: tuple[int, int]):
        self.surface = Surface(size)
        self.surface.convert()
        self.es = EventStore()
        self.ks = KeyStore()

    def tick(self, delta: float):
        pass

    def render(self) -> list[Rect | None]:
        self.surface.fill((0, 0, 0))
        return [None]
