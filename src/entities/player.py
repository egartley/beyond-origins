import pygame.image

from src.engine.animation import Animation
from src.engine.game_state import GameState
from src.engine.sprite import AnimatedSprite


class Player(AnimatedSprite):

    def __init__(self, game_state: GameState):
        full_sheet = pygame.image.load("res/images/entities/player-default.png")
        full_sheet.convert_alpha()
        left_sheet = full_sheet.subsurface((0, 0, 120, 44))
        right_sheet = full_sheet.subsurface((0, 44, 120, 44))
        left = Animation(game_state.es, 100, left_sheet, 4, True)
        right = Animation(game_state.es, 100, right_sheet, 4, True)
        super().__init__(30, 44, [left, right])

        self.speed = 100
        self.set_position(100.0, 100.0)
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

    def tick(self, delta: float):
        super().tick(delta)
        if self.up:
            self.move_y(-self.speed, delta)
        elif self.down:
            self.move_y(self.speed, delta)

        if self.left:
            self.move_x(-self.speed, delta)
            if self.animation_index == 1:
                self.set_animation(0)
        elif self.right:
            self.move_x(self.speed, delta)
            if self.animation_index == 0:
                self.set_animation(1)

        if not any([self.up, self.down, self.left, self.right]):
            self.animation.stop()
        elif not self.animation.is_running:
            self.animation.start()
