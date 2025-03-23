from pygame import Surface

from src.engine.event import EventStore


class GameState:

    IN_GAME = 0

    def __init__(self, state_id: int, es: EventStore):
        self.state_id = state_id
        self.es = es

    def tick(self):
        pass

    def render(self, screen_size: tuple[int, int]) -> Surface:
        s = Surface(screen_size)
        s.convert()
        return s
