from pygame import Surface
from pygame.rect import Rect

from src.engine.event import EventStore
from src.engine.image import ImageStore
from src.engine.key import KeyStore


class GameState:

    def __init__(self):
        self.es = EventStore()
        self.ks = KeyStore()
        self.images = ImageStore()
        self.refresh_surface = True

    def tick(self, delta: float):
        pass

    def render(self, surface: Surface) -> list[Rect | None]:
        if self.refresh_surface:
            self.refresh_surface = False
            return [surface.get_rect()]
        return [None]
