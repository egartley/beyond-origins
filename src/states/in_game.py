import pygame
from pygame import Surface

from src.engine.game_state import GameState
from src.engine.level import Level
from src.levels.debug import DebugLevel


class InGameState(GameState):

    def __init__(self, screen: Surface, debug: bool=False):
        super().__init__(screen)
        self.debug = debug
        self.level = Level("", self)
        self.load_level(DebugLevel(self))
        self.debug_font = pygame.font.SysFont("Consolas", 16)

    def load_level(self, level: Level):
        self.level = level
        self.level.load()

    def tick(self, delta: float):
        self.level.tick(delta)

    def render(self, surface: Surface):
        self.level.render(surface)
        if self.debug:
            ds = self.debug_font.render(f"camera=({int(self.level.cam_x)}, {int(self.level.cam_y)})",
                                        True, "white")
            surface.blit(ds, (32, 56))
            ds = self.debug_font.render(f"pos=({int(self.level.player.x)}, {int(self.level.player.y)})",
                                        True, "white")
            surface.blit(ds, (32, 80))
            ds = self.debug_font.render(f"vel_x={self.level.player.vel_x:.2f}, vel_y={self.level.player.vel_y:.2f}",
                                        True, "white")
            surface.blit(ds, (32, 104))
