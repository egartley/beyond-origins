import pygame.image
from pygame import Surface, Rect

from src.engine.event import EventHook
from src.engine.animation import Animation
from src.engine.game_state import GameState
from src.engine.oscillator import Oscillator
from src.engine.level_entity import LevelEntity
from src.images import Image


class DashEvent(EventHook):
    def __init__(self, callback):
        self.event_triggered = callback


class Player(LevelEntity):

    def __init__(self, game_state: GameState):
        super().__init__(30, 44)
        self.es = game_state.es
        full_sheet = game_state.images.get(Image.PLAYER_NEW_TEMP, True)
        left_sheet = full_sheet.subsurface((0, 0, 96, 64))
        right_sheet = full_sheet.subsurface((0, 64, 96, 64))
        left = Animation(self.es, 100, left_sheet, 2, True)
        right = Animation(self.es, 100, right_sheet, 2, True)
        self.add_animations([left, right])

        self.hover = Oscillator(self.es, 4, -4, 100)
        self.hover.start()
        self.shadow_surface = Surface((self.rect.width, 16), pygame.SRCALPHA).convert_alpha()

        self.jump_vel_up, self.jump_vel_down, self.max_jump_height = 125, 175, 26
        self.in_air, self.falling, self.jump_height = False, False, 0
        self.dash_duration, self.dash_modifier = 250, 3
        self.dash_cooldown, self.can_dash = 175, True
        self.speed = 200
        self.accel, self.decel = 900, 1000
        game_state.ks.register_down_hook(pygame.K_w, lambda: self.key_move(pygame.K_w, True))
        game_state.ks.register_down_hook(pygame.K_a, lambda: self.key_move(pygame.K_a, True))
        game_state.ks.register_down_hook(pygame.K_s, lambda: self.key_move(pygame.K_s, True))
        game_state.ks.register_down_hook(pygame.K_d, lambda: self.key_move(pygame.K_d, True))
        game_state.ks.register_down_hook(pygame.K_SPACE, lambda: self.key_move(pygame.K_SPACE, True))
        game_state.ks.register_down_hook(pygame.K_LSHIFT, lambda: self.key_move(pygame.K_LSHIFT, True))
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
        elif key == pygame.K_SPACE and key_down:
            self.start_jump()
        elif key == pygame.K_LSHIFT and key_down:
            self.start_dash()

    def start_jump(self):
        if not self.in_air:
            self.in_air = True
            self.jump_height = 0
            self.hover.stop()

    def stop_jump(self):
        self.in_air = False
        self.falling = False
        self.jump_height = 0
        self.hover.start()

    def start_dash(self):
        if self.can_dash:
            self.can_dash = False
            eid = self.es.add_event(DashEvent(self.stop_dash), self.dash_duration, 1)
            self.es.start_event(eid)
            self.speed *= self.dash_modifier
            self.accel *= self.dash_modifier * 0.5

    def stop_dash(self):
        self.speed /= self.dash_modifier
        self.accel /= self.dash_modifier * 0.5
        eid = self.es.add_event(DashEvent(self.allow_dash), self.dash_cooldown, 1)
        self.es.start_event(eid)

    def allow_dash(self):
        self.can_dash = True

    def draw_shadow(self, surface: Surface):
        sr = Rect(int(self.x + self.x_offset), int(self.rect.bottom + 4 + self.jump_height),
                  36 + self.y_offset - self.jump_height, 12 + int((self.y_offset - self.jump_height) / 6))
        if self.shadow_surface.get_size() != sr.size:
            self.shadow_surface.fill((0, 0, 0, 0))
            self.shadow_surface.set_alpha(40 + int((self.y_offset - self.jump_height) * 1.3))
            new_x = (self.shadow_surface.get_width() / 2) - (sr.width / 2)
            new_y = (self.shadow_surface.get_height() / 2) - (sr.height / 2)
            pygame.draw.ellipse(self.shadow_surface, (0, 0, 0), (new_x, new_y, sr.width, sr.height))
        surface.blit(self.shadow_surface, (sr.x, sr.y))

    def tick(self, delta: float):
        super().tick(delta)
        if not self.in_air:
            self.y_offset = self.hover.value
        else:
            cy = self.y
            self.move_y(delta, (1 if self.falling else -1) * (self.jump_vel_down if self.falling else self.jump_vel_up))
            self.jump_height += cy - self.y
            if self.jump_height >= self.max_jump_height and not self.falling:
                self.falling = True
            if self.falling and self.jump_height <= 0:
                self.stop_jump()
        if self.left and self.animation_index == 1:
            self.set_animation(0)
        elif self.right and self.animation_index == 0:
            self.set_animation(1)
        if any([self.up, self.down, self.left, self.right]):
            if not self.animation.is_running:
                self.animation.start()
        elif self.animation.is_running:
            self.animation.stop()

    def render(self, surface: Surface):
        self.draw_shadow(surface)
        super().render(surface)
