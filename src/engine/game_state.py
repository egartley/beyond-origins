from pygame import Surface

from src.engine.event import EventStore
from src.engine.image import ImageStore
from src.engine.key import KeyStore


class GameState:

    def __init__(self):
        self.es = EventStore()
        self.ks = KeyStore()
        self.images = ImageStore()

    def tick(self, delta: float):
        pass

    def render(self, surface: Surface):
        pass
