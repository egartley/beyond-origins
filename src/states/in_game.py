from src.engine.game_state import GameState
from src.entities.player import Player


class InGameState(GameState):

    def __init__(self, size):
        super().__init__(size)
        self.player = Player(self)

    def tick(self, delta: float):
        self.player.tick(delta)

    def render(self):
        super().render()
        self.player.render(self.surface)
        return [self.player.rect]
