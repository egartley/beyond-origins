import pygame
from pygame import Surface

from src.engine.game_state import GameState
from src.engine.level import Level
from src.levels.debug import DebugLevel


class InGameState(GameState):

    def __init__(self, screen: Surface, debug: bool=False):
        super().__init__(screen)
        self.debug = debug
        self.level = None
        self.debug_font = pygame.font.SysFont("Consolas", 16)
        self.menu_visible = False
        self.menu_surface = None
        self.ks.register_up_hook(pygame.K_e, lambda: self.toggle_menu())
        self.load_level(DebugLevel(self))

    def load_level(self, level: Level):
        self.level = level
        self.level.load()
        self.set_ui_surfaces()

    def set_ui_surfaces(self):
        background = Surface(self.screen.get_size(), pygame.SRCALPHA).convert_alpha()
        background.fill((0, 0, 0, 128))
        placeholder = Surface((1280, 720)).convert()
        placeholder.fill((128, 128, 128))
        self.menu_surface = Surface(self.screen.get_size(), pygame.SRCALPHA).convert_alpha()
        self.menu_surface.blit(background, (0, 0))
        cx = (self.menu_surface.get_width() // 2) - (placeholder.get_width() // 2)
        cy = (self.menu_surface.get_height() // 2) - (placeholder.get_height() // 2)
        self.menu_surface.blit(placeholder, (cx, cy))

    def toggle_menu(self):
        self.menu_visible = not self.menu_visible

    def tick(self, delta: float):
        if not self.menu_visible:
            self.level.tick(delta)

    def render(self, surface: Surface):
        self.level.render(surface)
        if self.menu_visible:
            surface.blit(self.menu_surface, (0, 0))
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
