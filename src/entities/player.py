import pygame.image
from pygame import Surface, Rect

from src.engine.animation import Animation
from src.engine.game_state import GameState
from src.engine.hover_control import HoverControl
from src.engine.sprite import AnimatedSprite
from src.images import Image


class Player(AnimatedSprite):

    def __init__(self, game_state: GameState):
        super().__init__(30, 44)
        full_sheet = game_state.images.get(Image.PLAYER_NEW_TEMP)
        left_sheet = full_sheet.subsurface((0, 0, 96, 64))
        right_sheet = full_sheet.subsurface((0, 64, 96, 64))
        left = Animation(game_state.es, 100, left_sheet, 2, True)
        right = Animation(game_state.es, 100, right_sheet, 2, True)
        self.add_animations([left, right])

        self.hover = HoverControl(game_state.es, self, 4, 100)
        self.hover.start()
        self.shadow_surface = Surface((self.rect.width, 12), pygame.SRCALPHA)
        self.shadow_surface.set_alpha(40)
        self.shadow_surface.convert_alpha()

        self.speed = 150
        self.up, self.down, self.left, self.right = False, False, False, False
        game_state.ks.register_down_hook(pygame.K_w, lambda: self.key_move(pygame.K_w, True))
        game_state.ks.register_down_hook(pygame.K_a, lambda: self.key_move(pygame.K_a, True))
        game_state.ks.register_down_hook(pygame.K_s, lambda: self.key_move(pygame.K_s, True))
        game_state.ks.register_down_hook(pygame.K_d, lambda: self.key_move(pygame.K_d, True))
        game_state.ks.register_up_hook(pygame.K_w, lambda: self.key_move(pygame.K_w, False))
        game_state.ks.register_up_hook(pygame.K_a, lambda: self.key_move(pygame.K_a, False))
        game_state.ks.register_up_hook(pygame.K_s, lambda: self.key_move(pygame.K_s, False))
        game_state.ks.register_up_hook(pygame.K_d, lambda: self.key_move(pygame.K_d, False))

    def key_move(self, key: int, key_down: bool):
        if key == pygame.K_w:
            self.up = key_down
        elif key == pygame.K_a:
            self.left = key_down
        elif key == pygame.K_s:
            self.down = key_down
        elif key == pygame.K_d:
            self.right = key_down

    def draw_shadow(self, surface: Surface) -> Rect:
        sr = Rect(int(self.x + self.x_offset), int(self.y + self.rect.height + 4), 36 + self.y_offset, 12)
        if self.shadow_surface.get_width() != sr.width or self.shadow_surface.get_height() != sr.height:
            self.shadow_surface.fill((0, 0, 0, 0))
            self.shadow_surface.set_alpha(40 + (self.y_offset * 2))
            new_x = (self.shadow_surface.get_width() / 2) - (sr.width / 2)
            pygame.draw.ellipse(self.shadow_surface, (0, 0, 0), (new_x, 0, sr.width, sr.height))
        return surface.blit(self.shadow_surface, (sr.x, sr.y))

    def tick(self, delta: float):
        super().tick(delta)
        if self.up:
            self.move_y(-self.speed, delta)
        elif self.down:
            self.move_y(self.speed, delta)

        if self.left:
            self.move_x(-self.speed, delta)
            if self.current_animation_index == 1:
                self.set_animation(0)
        elif self.right:
            self.move_x(self.speed, delta)
            if self.current_animation_index == 0:
                self.set_animation(1)

        if any([self.up, self.down, self.left, self.right]):
            if self.animation.is_running:
                self.animation.start()
        elif self.animation.is_running:
            self.animation.stop()

    def render(self, surface: Surface):
        super().render(surface)
        self.draw_shadow(surface)
