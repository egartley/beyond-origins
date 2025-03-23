from pygame import Surface

from src.engine.event import EventStore
from src.engine.game_state import GameState
from src.entities.player import Player


class InGameState(GameState):

    def __init__(self, es: EventStore):
        super().__init__(GameState.IN_GAME, es)
        self.player = Player(es)

    def tick(self):
        self.player.tick()

    def render(self, screen_size: tuple[int, int]) -> Surface:
        s = super().render(screen_size)
        s.blit(self.player.render(), (100, 100))
        return s
