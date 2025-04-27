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
        background = Surface(self.screen.get_size(), pygame.SRCALPHA)
        background.fill((0, 0, 0, 128))
        placeholder = Surface((1280, 720)).convert()
        placeholder.fill((128, 128, 128))
        self.menu_surface = Surface(self.screen.get_size(), pygame.SRCALPHA)
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
        s = [self.level.render()]
        if self.menu_visible:
            s.append((self.menu_surface, (0, 0)))
        if self.debug:
            pos = self.debug_font.render(f"cam=({int(self.level.cam_x)}, {int(self.level.cam_y)}), "
                                         f"pos=({int(self.level.player.x)}, {int(self.level.player.y)})",
                                         True, "white")
            vel = self.debug_font.render(f"vx={self.level.player.vel_x:.2f}, vy={self.level.player.vel_y:.2f}",
                                         True, "white")
            s.extend([(pos, (32, 56)), (vel, (32, 80))])
        surface.blits(s, doreturn=False)
