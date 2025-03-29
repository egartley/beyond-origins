from typing import List

import pygame.sprite
from pygame import Surface
from src.engine.animation import Animation


class Sprite(pygame.sprite.Sprite):

    def __init__(self, width: int = 32, height: int = 32):
        super().__init__()
        self.x, self.y = 0.0, 0.0
        self.speed = 100
        self.image = pygame.Surface([width, height])
        self.rect = pygame.rect.Rect(self.x, self.y, width, height)

    def set_position(self, x: float, y: float):
        self.x = x
        self.y = y
        self.rect.move_ip(int(self.x), int(self.y))

    def move_x(self, offset: float, delta: float):
        self.x += offset * delta
        self.rect.left = int(self.x)

    def move_y(self, offset: float, delta: float):
        self.y += offset * delta
        self.rect.top = int(self.y)

    def tick(self, delta: float):
        pass

    def render(self, surface: Surface):
        surface.blit(self.image, (int(self.x), int(self.y)))
        # pygame.draw.rect(surface, (255, 255, 255), self.rect, 1)


class AnimatedSprite(Sprite):

    def __init__(self, width: int, height: int, animations: List[Animation]):
        super().__init__(width, height)
        self.animations = animations
        self.animation_index = 0
        self.animation = self.animations[self.animation_index]
        self.image = self.animation.get_frame()

    def tick(self, delta: float):
        self.sync_animation()

    def sync_animation(self):
        frame = self.animation.get_frame()
        if self.image != frame:
            self.image = frame

    def set_animation(self, index: int):
        if 0 <= index < len(self.animations):
            self.animation.stop()
            self.animation_index = index
            self.animation = self.animations[self.animation_index]
            self.animation.start()
            self.sync_animation()
